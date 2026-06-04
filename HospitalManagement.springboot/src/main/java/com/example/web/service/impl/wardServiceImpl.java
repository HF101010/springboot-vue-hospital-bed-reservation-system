package com.example.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.web.dto.BedPoolDto;
import com.example.web.dto.query.wardPagedInput;
import com.example.web.dto.wardDto;
import com.example.web.entity.bed;
import com.example.web.entity.ward;
import com.example.web.mapper.bedMapper;
import com.example.web.mapper.wardMapper;
import com.example.web.service.wardService;
import com.example.web.tools.Extension;
import com.example.web.tools.StatusUtil;
import com.example.web.tools.dto.IdInput;
import com.example.web.tools.dto.IdsInput;
import com.example.web.tools.dto.PagedResult;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class wardServiceImpl extends ServiceImpl<wardMapper, ward> implements wardService {

    @Autowired
    private wardMapper wardMapper;

    @Autowired
    private bedMapper bedMapper;

    private LambdaQueryWrapper<ward> BuilderQuery(wardPagedInput input) {
        LambdaQueryWrapper<ward> queryWrapper = Wrappers.<ward>lambdaQuery()
                .eq(input.getId() != null && input.getId() != 0, ward::getId, input.getId());

        if (Extension.isNotNullOrEmpty(input.getWardName())) {
            queryWrapper.like(ward::getWard_name, input.getWardName());
        }
        if (Extension.isNotNullOrEmpty(input.getDepartment())) {
            queryWrapper.like(ward::getDepartment, input.getDepartment());
        }
        if (Extension.isNotNullOrEmpty(input.getKeyword())) {
            queryWrapper.like(ward::getRemark, input.getKeyword());
        }
        return queryWrapper;
    }

    @SneakyThrows
    @Override
    public PagedResult<wardDto> List(wardPagedInput input) {
        LambdaQueryWrapper<ward> queryWrapper = BuilderQuery(input);
        if (input.getSortItem() != null) {
            queryWrapper.last("ORDER BY " + input.getSortItem().getFieldName()
                    + (input.getSortItem().getIsAsc() ? " ASC" : " DESC"));
        } else {
            queryWrapper = queryWrapper.orderByDesc(ward::getId);
        }

        Page<ward> page = new Page<>(input.getPage(), input.getLimit());
        IPage<ward> pageRecords = wardMapper.selectPage(page, queryWrapper);
        Long totalCount = wardMapper.selectCount(queryWrapper);
        List<wardDto> items = Extension.copyBeanList(pageRecords.getRecords(), wardDto.class);
        return PagedResult.GetInstance(items, totalCount);
    }

    @SneakyThrows
    @Override
    public wardDto Get(wardPagedInput input) {
        if (input.getId() == null) {
            return new wardDto();
        }
        return List(input).getItems().stream().findFirst().orElse(new wardDto());
    }

    @SneakyThrows
    @Override
    public wardDto CreateOrEdit(wardDto input) {
        ward entity = input.MapToEntity();
        saveOrUpdate(entity);
        return entity.MapToDto();
    }

    @Override
    public void Delete(IdInput input) {
        ward entity = wardMapper.selectById(input.getId());
        wardMapper.deleteById(entity);
    }

    @Override
    public void BatchDelete(IdsInput input) {
        for (Integer id : input.getIds()) {
            IdInput idInput = new IdInput();
            idInput.setId(id);
            Delete(idInput);
        }
    }

    @SneakyThrows
    @Override
    public BedPoolDto getBedPoolByDepartment(String department) {
        if (Extension.isNullOrEmpty(department)) {
            return new BedPoolDto();
        }

        BedPoolDto bedPoolDto = new BedPoolDto();
        bedPoolDto.setDepartment(department);

        List<ward> wards = wardMapper.selectList(
                Wrappers.<ward>lambdaQuery()
                        .eq(ward::getDepartment, department)
                        .orderByAsc(ward::getId)
        );

        if (wards == null || wards.isEmpty()) {
            bedPoolDto.setWards(new ArrayList<>());
            bedPoolDto.setTotalBeds(0);
            bedPoolDto.setAvailableBeds(0);
            bedPoolDto.setOccupiedBeds(0);
            bedPoolDto.setMaintenanceBeds(0);
            return bedPoolDto;
        }

        List<BedPoolDto.WardBedInfoDto> wardBedInfoList = new ArrayList<>();
        int totalBeds = 0;
        int availableBeds = 0;
        int occupiedBeds = 0;
        int maintenanceBeds = 0;

        for (ward wardEntity : wards) {
            BedPoolDto.WardBedInfoDto wardBedInfo = new BedPoolDto.WardBedInfoDto();
            wardBedInfo.setWardId(wardEntity.getId());
            wardBedInfo.setWardName(wardEntity.getWard_name());

            List<bed> beds = bedMapper.selectList(
                    Wrappers.<bed>lambdaQuery()
                            .eq(bed::getWard_id, String.valueOf(wardEntity.getId()))
                            .orderByDesc(bed::getId)
            );

            List<BedPoolDto.BedInfoDto> bedInfoList = new ArrayList<>();
            int wardAvailable = 0;
            int wardOccupied = 0;
            int wardMaintenance = 0;

            for (bed bedEntity : beds) {
                String normalizedStatus = StatusUtil.normalizeBedStatus(bedEntity.getStatus());

                BedPoolDto.BedInfoDto bedInfo = new BedPoolDto.BedInfoDto();
                bedInfo.setBedId(bedEntity.getId());
                bedInfo.setBedNumber(bedEntity.getBed_number());
                bedInfo.setStatus(normalizedStatus);
                bedInfo.setStatusText(StatusUtil.bedStatusLabel(normalizedStatus));
                bedInfo.setRemark(bedEntity.getRemark());
                bedInfoList.add(bedInfo);

                if (StatusUtil.BED_AVAILABLE.equals(normalizedStatus)) {
                    wardAvailable++;
                    availableBeds++;
                } else if (StatusUtil.BED_OCCUPIED.equals(normalizedStatus)) {
                    wardOccupied++;
                    occupiedBeds++;
                } else if (StatusUtil.BED_MAINTENANCE.equals(normalizedStatus)) {
                    wardMaintenance++;
                    maintenanceBeds++;
                }
            }

            wardBedInfo.setBeds(bedInfoList);
            wardBedInfo.setTotalBeds(bedInfoList.size());
            wardBedInfo.setAvailableBeds(wardAvailable);
            wardBedInfo.setOccupiedBeds(wardOccupied);
            wardBedInfo.setMaintenanceBeds(wardMaintenance);
            wardBedInfoList.add(wardBedInfo);
            totalBeds += bedInfoList.size();
        }

        bedPoolDto.setWards(wardBedInfoList);
        bedPoolDto.setTotalBeds(totalBeds);
        bedPoolDto.setAvailableBeds(availableBeds);
        bedPoolDto.setOccupiedBeds(occupiedBeds);
        bedPoolDto.setMaintenanceBeds(maintenanceBeds);
        return bedPoolDto;
    }
}
