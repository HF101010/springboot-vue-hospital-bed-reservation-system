package com.example.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.web.dto.AppUserDto;
import com.example.web.dto.bedDto;
import com.example.web.dto.bed_reservationDto;
import com.example.web.dto.notificationDto;
import com.example.web.dto.query.bed_reservationPagedInput;
import com.example.web.dto.userDto;
import com.example.web.dto.wardDto;
import com.example.web.entity.AppUser;
import com.example.web.entity.allocation_audit;
import com.example.web.entity.bed;
import com.example.web.entity.bed_reservation;
import com.example.web.entity.user;
import com.example.web.entity.ward;
import com.example.web.mapper.AppUserMapper;
import com.example.web.mapper.allocation_auditMapper;
import com.example.web.mapper.bedMapper;
import com.example.web.mapper.bed_reservationMapper;
import com.example.web.mapper.userMapper;
import com.example.web.mapper.wardMapper;
import com.example.web.service.bed_reservationService;
import com.example.web.service.notificationService;
import com.example.web.tools.BaseContext;
import com.example.web.tools.Extension;
import com.example.web.tools.RedisLockUtil;
import com.example.web.tools.StatusUtil;
import com.example.web.tools.dto.IdInput;
import com.example.web.tools.dto.IdsInput;
import com.example.web.tools.dto.PagedResult;
import com.example.web.tools.exception.CustomException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class bed_reservationServiceImpl extends ServiceImpl<bed_reservationMapper, bed_reservation> implements bed_reservationService {

    @Autowired
    private bed_reservationMapper bed_reservationMapper;
    @Autowired
    private bedMapper bedMapper;
    @Autowired
    private wardMapper wardMapper;
    @Autowired
    private userMapper userMapper;
    @Autowired
    private notificationService notificationService;
    @Autowired
    private AppUserMapper appUserMapper;
    @Autowired
    private RedisLockUtil redisLockUtil;
    @Autowired
    private allocation_auditMapper allocationAuditMapper;

    private LambdaQueryWrapper<bed_reservation> BuilderQuery(bed_reservationPagedInput input) {
        LambdaQueryWrapper<bed_reservation> queryWrapper = Wrappers.<bed_reservation>lambdaQuery()
                .eq(input.getId() != null && input.getId() != 0, bed_reservation::getId, input.getId());

        if (Extension.isNotNullOrEmpty(input.getPatientId())) {
            queryWrapper.eq(bed_reservation::getPatient_id, input.getPatientId());
        }
        if (Extension.isNotNullOrEmpty(input.getWardId())) {
            queryWrapper.eq(bed_reservation::getWard_id, input.getWardId());
        }
        if (Extension.isNotNullOrEmpty(input.getBedId())) {
            queryWrapper.eq(bed_reservation::getBed_id, input.getBedId());
        }
        if (Extension.isNotNullOrEmpty(input.getStatus())) {
            queryWrapper.eq(bed_reservation::getStatus, StatusUtil.normalizeReservationStatus(input.getStatus()));
        }
        if (input.getStartTime() != null) {
            queryWrapper.ge(bed_reservation::getReservation_time, input.getStartTime());
        }
        if (input.getEndTime() != null) {
            queryWrapper.le(bed_reservation::getReservation_time, input.getEndTime());
        }
        if (Extension.isNotNullOrEmpty(input.getKeyword())) {
            queryWrapper.like(bed_reservation::getReason, input.getKeyword());
        }
        return queryWrapper;
    }

    private List<bed_reservationDto> DispatchItem(List<bed_reservationDto> items) throws InvocationTargetException, IllegalAccessException {
        for (bed_reservationDto item : items) {
            bed bedEntity = bedMapper.selectById(item.getBed_id());
            if (bedEntity != null) {
                bedEntity.setStatus(StatusUtil.normalizeBedStatus(bedEntity.getStatus()));
            }
            item.setBed_idDto(bedEntity != null ? bedEntity.MapToDto() : new bedDto());

            ward wardEntity = wardMapper.selectById(item.getWard_id());
            item.setWard_idDto(wardEntity != null ? wardEntity.MapToDto() : new wardDto());

            user patientEntity = userMapper.selectById(item.getPatient_id());
            item.setPatient_idDto(patientEntity != null ? patientEntity.MapToDto() : new userDto());

            item.setStatus(StatusUtil.normalizeReservationStatus(item.getStatus()));
        }
        return items;
    }

    @SneakyThrows
    @Override
    public PagedResult<bed_reservationDto> List(bed_reservationPagedInput input) {
        LambdaQueryWrapper<bed_reservation> queryWrapper = BuilderQuery(input);
        if (input.getSortItem() != null) {
            queryWrapper.last("ORDER BY " + input.getSortItem().getFieldName()
                    + (input.getSortItem().getIsAsc() ? " ASC" : " DESC"));
        } else {
            queryWrapper = queryWrapper.orderByDesc(bed_reservation::getId);
        }

        Page<bed_reservation> page = new Page<>(input.getPage(), input.getLimit());
        IPage<bed_reservation> pageRecords = bed_reservationMapper.selectPage(page, queryWrapper);
        Long totalCount = bed_reservationMapper.selectCount(queryWrapper);
        List<bed_reservationDto> items = Extension.copyBeanList(pageRecords.getRecords(), bed_reservationDto.class);
        DispatchItem(items);
        return PagedResult.GetInstance(items, totalCount);
    }

    @SneakyThrows
    @Override
    public bed_reservationDto Get(bed_reservationPagedInput input) {
        if (input.getId() == null) {
            return new bed_reservationDto();
        }
        return List(input).getItems().stream().findFirst().orElse(new bed_reservationDto());
    }

    @SneakyThrows
    @Override
    public bed_reservationDto CreateOrEdit(bed_reservationDto input) {
        boolean isNew = input.getId() == null || input.getId() == 0;
        if (isNew && Extension.isNullOrEmpty(input.getStatus())) {
            input.setStatus(StatusUtil.RESERVATION_PENDING);
        } else if (Extension.isNotNullOrEmpty(input.getStatus())) {
            input.setStatus(StatusUtil.normalizeReservationStatus(input.getStatus()));
        }

        if (Extension.isNullOrEmpty(input.getBed_id())) {
            input.setBed_id(null);
        }

        String lockKey;
        if (isNew && Extension.isNotNullOrEmpty(input.getBed_id())) {
            lockKey = "bed:lock:" + input.getBed_id();
        } else if (isNew
                && Extension.isNotNullOrEmpty(input.getDepartment())
                && Extension.isNullOrEmpty(input.getWard_id())
                && Extension.isNullOrEmpty(input.getBed_id())) {
            lockKey = "department:lock:" + input.getDepartment();
        } else {
            lockKey = isNew ? "reservation:lock:new" : "reservation:lock:" + input.getId();
        }

        return redisLockUtil.executeWithLock(lockKey, 30, () -> {
            bed selectedBed = null;

            if (isNew
                    && Extension.isNotNullOrEmpty(input.getDepartment())
                    && Extension.isNullOrEmpty(input.getWard_id())
                    && Extension.isNullOrEmpty(input.getBed_id())) {

                List<ward> wards = wardMapper.selectList(
                        Wrappers.<ward>lambdaQuery()
                                .eq(ward::getDepartment, input.getDepartment())
                                .orderByAsc(ward::getId)
                );

                if (wards == null || wards.isEmpty()) {
                    throw new CustomException("当前科室暂无配置病区，请联系管理员");
                }

                List<String> wardIds = new ArrayList<>();
                for (ward wardEntity : wards) {
                    if (wardEntity.getId() != null) {
                        wardIds.add(String.valueOf(wardEntity.getId()));
                    }
                }

                if (wardIds.isEmpty()) {
                    throw new CustomException("当前科室暂无可用病区，请联系管理员");
                }

                selectedBed = bedMapper.selectOne(
                        Wrappers.<bed>lambdaQuery()
                                .in(bed::getWard_id, wardIds)
                                .eq(bed::getStatus, StatusUtil.BED_AVAILABLE)
                                .orderByDesc(bed::getId)
                                .last("LIMIT 1")
                );

                if (selectedBed == null) {
                    throw new CustomException("当前科室暂无可用床位，请稍后再试或选择其他科室");
                }

                bed checkBed = bedMapper.selectById(selectedBed.getId());
                if (checkBed == null || !StatusUtil.BED_AVAILABLE.equals(StatusUtil.normalizeBedStatus(checkBed.getStatus()))) {
                    throw new CustomException("床位已被占用，请重新选择");
                }

                input.setWard_id(selectedBed.getWard_id());
                input.setBed_id(selectedBed.getId() != null ? String.valueOf(selectedBed.getId()) : null);
            } else if (isNew && Extension.isNotNullOrEmpty(input.getBed_id())) {
                bed checkBed = bedMapper.selectById(input.getBed_id());
                if (checkBed == null) {
                    throw new CustomException("床位不存在");
                }
                if (!StatusUtil.BED_AVAILABLE.equals(StatusUtil.normalizeBedStatus(checkBed.getStatus()))) {
                    throw new CustomException("床位已被占用或不可用，请选择其他床位");
                }
                selectedBed = checkBed;
            }

            bed_reservation entity = input.MapToEntity();
            entity.setStatus(StatusUtil.normalizeReservationStatus(input.getStatus()));
            saveOrUpdate(entity);

            if (isNew && selectedBed != null) {
                allocation_audit audit = new allocation_audit();
                audit.setReservation_id(String.valueOf(entity.getId()));
                audit.setBed_id(String.valueOf(selectedBed.getId()));
                audit.setAlgorithm("DEPARTMENT_PRIORITY_V1");
                audit.setReason("按科室筛选可用床位，并按床位ID倒序优先分配");
                allocationAuditMapper.insert(audit);
            }

            if (isNew && StatusUtil.RESERVATION_PENDING.equals(entity.getStatus())) {
                notifyAdminsForNewReservation(entity);
            }

            bed_reservation savedReservation = bed_reservationMapper.selectById(entity.getId());
            return savedReservation != null ? savedReservation.MapToDto() : entity.MapToDto();
        });
    }

    private void notifyAdminsForNewReservation(bed_reservation reservation) {
        List<AppUser> adminUsers = appUserMapper.selectList(
                Wrappers.<AppUser>lambdaQuery().eq(AppUser::getRoleType, 1)
        );

        Integer currentUserId = BaseContext.getCurrentUserDto().getUserId();
        AppUser currentUser = appUserMapper.selectById(currentUserId);
        String userName = currentUser != null ? (currentUser.getName() != null ? currentUser.getName() : currentUser.getUserName()) : "用户";

        ward wardInfo = wardMapper.selectById(reservation.getWard_id());
        bed bedInfo = bedMapper.selectById(reservation.getBed_id());
        String wardName = wardInfo != null ? wardInfo.getWard_name() : "";
        String bedNumber = bedInfo != null ? bedInfo.getBed_number() : "";
        String reservationTimeStr = reservation.getReservation_time() != null ? reservation.getReservation_time().toString() : "";

        for (AppUser admin : adminUsers) {
            notificationDto notification = new notificationDto();
            notification.setTitle("新的床位预约申请");
            notification.setContent(String.format("用户 %s 申请预约 %s 的床位 %s，预约时间：%s，原因：%s",
                    userName, wardName, bedNumber, reservationTimeStr,
                    reservation.getReason() != null ? reservation.getReason() : ""));
            notification.setSender_id(String.valueOf(currentUserId));
            notification.setReceiver_id(String.valueOf(admin.getId()));
            notification.setCreate_time(LocalDateTime.now());
            notification.setIs_read(false);
            notificationService.CreateOrEdit(notification);
        }
    }

    @Override
    public void Delete(IdInput input) {
        bed_reservation entity = bed_reservationMapper.selectById(input.getId());
        bed_reservationMapper.deleteById(entity);
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
    public PagedResult<bed_reservationDto> MyList(bed_reservationPagedInput input) {
        Integer currentUserId = BaseContext.getCurrentUserDto().getUserId();
        if (currentUserId == null) {
            throw new CustomException("请先登录");
        }

        LambdaQueryWrapper<bed_reservation> queryWrapper = BuilderQuery(input);
        queryWrapper.eq(bed_reservation::getPatient_id, String.valueOf(currentUserId));

        if (input.getSortItem() != null) {
            queryWrapper.last("ORDER BY " + input.getSortItem().getFieldName()
                    + (input.getSortItem().getIsAsc() ? " ASC" : " DESC"));
        } else {
            queryWrapper = queryWrapper.orderByDesc(bed_reservation::getId);
        }

        Page<bed_reservation> page = new Page<>(input.getPage(), input.getLimit());
        IPage<bed_reservation> pageRecords = bed_reservationMapper.selectPage(page, queryWrapper);
        Long totalCount = bed_reservationMapper.selectCount(queryWrapper);
        List<bed_reservationDto> items = Extension.copyBeanList(pageRecords.getRecords(), bed_reservationDto.class);
        DispatchItem(items);
        return PagedResult.GetInstance(items, totalCount);
    }

    @SneakyThrows
    @Override
    public bed_reservationDto Audit(bed_reservationDto input) {
        Integer currentAdminId = BaseContext.getCurrentUserDto().getUserId();
        String lockKey = "reservation:audit:lock:" + input.getId();

        return redisLockUtil.executeWithLock(lockKey, 30, () -> {
            bed_reservation reservation = bed_reservationMapper.selectById(input.getId());
            if (reservation == null) {
                throw new CustomException("预约记录不存在");
            }

            if (!StatusUtil.RESERVATION_PENDING.equals(StatusUtil.normalizeReservationStatus(reservation.getStatus()))) {
                throw new CustomException("该预约已被审核，无法重复审核");
            }

            reservation.setStatus(StatusUtil.normalizeReservationStatus(input.getStatus()));
            reservation.setAudit_user_id(String.valueOf(currentAdminId));
            reservation.setAudit_time(LocalDateTime.now());
            saveOrUpdate(reservation);

            if (StatusUtil.RESERVATION_APPROVED.equals(reservation.getStatus()) && Extension.isNotNullOrEmpty(reservation.getBed_id())) {
                bed bedInfo = bedMapper.selectById(reservation.getBed_id());
                if (bedInfo != null) {
                    bedInfo.setStatus(StatusUtil.BED_OCCUPIED);
                    bedMapper.updateById(bedInfo);
                }
            }

            if (StatusUtil.RESERVATION_APPROVED.equals(reservation.getStatus())) {
                ward wardInfo = wardMapper.selectById(reservation.getWard_id());
                bed bedInfo = bedMapper.selectById(reservation.getBed_id());

                notificationDto notification = new notificationDto();
                notification.setTitle("床位预约审核通过");
                notification.setContent(String.format("您的床位预约申请已通过审核。预约信息：%s 的床位 %s，预约时间：%s",
                        wardInfo != null ? wardInfo.getWard_name() : "",
                        bedInfo != null ? bedInfo.getBed_number() : "",
                        reservation.getReservation_time()));
                notification.setSender_id(String.valueOf(currentAdminId));
                notification.setReceiver_id(reservation.getPatient_id());
                notification.setCreate_time(LocalDateTime.now());
                notification.setIs_read(false);
                notificationService.CreateOrEdit(notification);
            } else if (StatusUtil.RESERVATION_REJECTED.equals(reservation.getStatus())) {
                notificationDto notification = new notificationDto();
                notification.setTitle("床位预约审核未通过");
                notification.setContent("很抱歉，您的床位预约申请未通过审核。如有疑问，请联系管理员。");
                notification.setSender_id(String.valueOf(currentAdminId));
                notification.setReceiver_id(reservation.getPatient_id());
                notification.setCreate_time(LocalDateTime.now());
                notification.setIs_read(false);
                notificationService.CreateOrEdit(notification);
            }

            return reservation.MapToDto();
        });
    }
}
