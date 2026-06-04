package com.example.web.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.web.SysConst;
import com.example.web.dto.*;
import com.example.web.dto.query.*;
import com.example.web.entity.*;
import com.example.web.mapper.*;
import com.example.web.enums.*;
import com.example.web.service.*;
import com.example.web.tools.dto.*;
import com.example.web.tools.exception.CustomException;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import lombok.SneakyThrows;
import java.io.IOException;
import com.example.web.tools.*;
import java.text.DecimalFormat;
/**
 * 床位信息功能实现类
 */
@Service
public class bedServiceImpl extends ServiceImpl<bedMapper, bed> implements bedService {

	 /**
     * 操作数据库user表mapper对象
     */
    @Autowired
    private userMapper userMapper;
    /**
     * 操作数据库的bed表mapper对象
     */
    @Autowired
    private bedMapper bedMapper;
    @Autowired
    private wardMapper  wardMapper;                        

  
   /**
     * 构建表查询sql
     */
    private LambdaQueryWrapper<bed> BuilderQuery(bedPagedInput input) {
       //声明一个支持床位信息查询的(拉姆达)表达式
        LambdaQueryWrapper<bed> queryWrapper = Wrappers.<bed>lambdaQuery()
                .eq(input.getId() != null && input.getId() != 0, bed::getId, input.getId());
   //如果前端搜索传入查询条件则拼接查询条件
        if (Extension.isNotNullOrEmpty(input.getBedNumber())) {
            queryWrapper.like(bed::getBed_number, input.getBedNumber());
        }
        if (Extension.isNotNullOrEmpty(input.getWardId())) {
            queryWrapper.eq(bed::getWard_id, input.getWardId());
        }
        if (Extension.isNotNullOrEmpty(input.getStatus())) {
            queryWrapper.eq(bed::getStatus, input.getStatus());
        }
        if (Extension.isNotNullOrEmpty(input.getKeyword())) {
            queryWrapper.like(bed::getRemark, input.getKeyword());
        }
      return queryWrapper;
    }
  
    /**
     * 处理床位信息对于的外键数据
     */
   private List<bedDto> DispatchItem(List<bedDto> items) throws InvocationTargetException, IllegalAccessException {
          
       for (bedDto item : items) {           
          	            
           //查询出关联的ward表信息           
            ward  ward_idEntity= wardMapper.selectById(item.getWard_id());
            item.setWard_idDto(ward_idEntity!=null?ward_idEntity.MapToDto():new wardDto());              
       }
       
     return items; 
   }
  
    /**
     * 床位信息分页查询
     */
    @SneakyThrows
    @Override
    public PagedResult<bedDto> List(bedPagedInput input) {
			//构建where条件+排序
        LambdaQueryWrapper<bed> queryWrapper = BuilderQuery(input);
        // 动态排序处理
        if (input.getSortItem() != null) {
            // 根据字段名动态排序
            queryWrapper.last("ORDER BY " + input.getSortItem().getFieldName()
                    + (input.getSortItem().getIsAsc() ? " ASC" : " DESC"));
        } else {
            // 默认按ID从大到小排序
            queryWrapper = queryWrapper.orderByDesc(bed::getId);
        }

        //构建一个分页查询的model
        Page<bed> page = new Page<>(input.getPage(), input.getLimit());
         //从数据库进行分页查询获取床位信息数据
        IPage<bed> pageRecords= bedMapper.selectPage(page, queryWrapper);
        //获取所有满足条件的数据行数
        Long totalCount= bedMapper.selectCount(queryWrapper);
        //把bed实体转换成bed传输模型
        List<bedDto> items= Extension.copyBeanList(pageRecords.getRecords(),bedDto.class);

        // 确保status字段正确传递（直接从原始实体复制，避免BeanUtils复制问题）
        for (int i = 0; i < items.size() && i < pageRecords.getRecords().size(); i++) {
            bedDto item = items.get(i);
            bed original = pageRecords.getRecords().get(i);
            
            // 方法1：直接从getter获取
            String statusValue = original.getStatus();
            
            // 方法2：如果getter返回null，通过反射获取字段值（可能是数字类型或enum）
            if (statusValue == null || statusValue.isEmpty()) {
                try {
                    java.lang.reflect.Field statusField = original.getClass().getDeclaredField("status");
                    statusField.setAccessible(true);
                    Object rawStatus = statusField.get(original);
                    if (rawStatus != null) {
                        statusValue = String.valueOf(rawStatus).trim();
                    }
                } catch (Exception e) {
                    // 忽略异常
                }
            }
            
            // 方法3：如果还是null，检查DTO中是否已经有值（可能来自BeanUtils.copyProperties）
            if ((statusValue == null || statusValue.isEmpty()) && item.getStatus() != null && !item.getStatus().isEmpty()) {
                statusValue = item.getStatus();
            }
            
            // 设置status字段（确保不为空）
            if (statusValue != null && !statusValue.isEmpty() && !statusValue.equals("null")) {
                item.setStatus(statusValue);
            } else {
                // 如果原始实体也没有status，设置为默认值"1"（可用）
                item.setStatus("1");
            }
        }

		   DispatchItem(items);
        //返回一个分页结构给前端
        return PagedResult.GetInstance(items,totalCount);

    }
  
    /**
     * 单个床位信息查询
     */
    @SneakyThrows
    @Override
    public bedDto Get(bedPagedInput input) {
       if(input.getId()==null)
        {
         return new bedDto();
        }
      
       PagedResult<bedDto> pagedResult = List(input);
        return pagedResult.getItems().stream().findFirst().orElse(new bedDto()); 
    }

    /**
     *床位信息创建或者修改
     */
    @SneakyThrows
    @Override
    public bedDto CreateOrEdit(bedDto input) {
        //声明一个床位信息实体
        bed bed=input.MapToEntity();  
        //调用数据库的增加或者修改方法
        saveOrUpdate(bed);
        //把传输模型返回给前端
        return bed.MapToDto();
    }
    /**
     * 床位信息删除
     */
    @Override
    public void Delete(IdInput input) {
        bed entity = bedMapper.selectById(input.getId());
        bedMapper.deleteById(entity);
    }

    /**
     * 床位信息批量删除
     */
    @Override
    public void BatchDelete(IdsInput input) {
        for (Integer id : input.getIds()) {
            IdInput idInput = new IdInput();
            idInput.setId(id);
            Delete(idInput);
        }
    }
}
