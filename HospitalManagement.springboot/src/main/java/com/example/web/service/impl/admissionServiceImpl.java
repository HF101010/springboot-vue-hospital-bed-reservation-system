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
 * 入出院登记功能实现类
 */
@Service
public class admissionServiceImpl extends ServiceImpl<admissionMapper, admission> implements admissionService {

    /**
     * 操作数据库的admission表mapper对象
     */
    @Autowired
    private admissionMapper admissionMapper;
    @Autowired
    private userMapper  userMapper;                        
    @Autowired
    private bedMapper  bedMapper;                        

  
   /**
     * 构建表查询sql
     */
    private LambdaQueryWrapper<admission> BuilderQuery(admissionPagedInput input) {
       //声明一个支持入出院登记查询的(拉姆达)表达式
        LambdaQueryWrapper<admission> queryWrapper = Wrappers.<admission>lambdaQuery()
                .eq(input.getId() != null && input.getId() != 0, admission::getId, input.getId());
   //如果前端搜索传入查询条件则拼接查询条件
        if (Extension.isNotNullOrEmpty(input.getPatientId())) {
            queryWrapper.eq(admission::getPatient_id, input.getPatientId());
        }
        if (Extension.isNotNullOrEmpty(input.getBedId())) {
            queryWrapper.eq(admission::getBed_id, input.getBedId());
        }
        if (Extension.isNotNullOrEmpty(input.getStatus())) {
            queryWrapper.eq(admission::getStatus, input.getStatus());
        }
        if (Extension.isNotNullOrEmpty(input.getKeyword())) {
            queryWrapper.like(admission::getRemark, input.getKeyword());
        }
      return queryWrapper;
    }
  
    /**
     * 处理入出院登记对于的外键数据
     */
   private List<admissionDto> DispatchItem(List<admissionDto> items) throws InvocationTargetException, IllegalAccessException {
          
       for (admissionDto item : items) {           
          	            
           //查询出关联的user表信息           
            user  patient_idEntity= userMapper.selectById(item.getPatient_id());
            item.setPatient_idDto(patient_idEntity!=null?patient_idEntity.MapToDto():new userDto());              
           
          	            
           //查询出关联的bed表信息           
            bed  bed_idEntity= bedMapper.selectById(item.getBed_id());
            item.setBed_idDto(bed_idEntity!=null?bed_idEntity.MapToDto():new bedDto());              
       }
       
     return items; 
   }
  
    /**
     * 入出院登记分页查询
     */
    @SneakyThrows
    @Override
    public PagedResult<admissionDto> List(admissionPagedInput input) {
			//构建where条件+排序
        LambdaQueryWrapper<admission> queryWrapper = BuilderQuery(input);
        // 动态排序处理
        if (input.getSortItem() != null) {
            // 根据字段名动态排序
            queryWrapper.last("ORDER BY " + input.getSortItem().getFieldName()
                    + (input.getSortItem().getIsAsc() ? " ASC" : " DESC"));
        } else {
            // 默认按ID从大到小排序
            queryWrapper = queryWrapper.orderByDesc(admission::getId);
        }

        //构建一个分页查询的model
        Page<admission> page = new Page<>(input.getPage(), input.getLimit());
         //从数据库进行分页查询获取入出院登记数据
        IPage<admission> pageRecords= admissionMapper.selectPage(page, queryWrapper);
        //获取所有满足条件的数据行数
        Long totalCount= admissionMapper.selectCount(queryWrapper);
        //把admission实体转换成admission传输模型
        List<admissionDto> items= Extension.copyBeanList(pageRecords.getRecords(),admissionDto.class);

		   DispatchItem(items);
        //返回一个分页结构给前端
        return PagedResult.GetInstance(items,totalCount);

    }
  
    /**
     * 单个入出院登记查询
     */
    @SneakyThrows
    @Override
    public admissionDto Get(admissionPagedInput input) {
       if(input.getId()==null)
        {
         return new admissionDto();
        }
      
       PagedResult<admissionDto> pagedResult = List(input);
        return pagedResult.getItems().stream().findFirst().orElse(new admissionDto()); 
    }

    /**
     *入出院登记创建或者修改
     */
    @SneakyThrows
    @Override
    public admissionDto CreateOrEdit(admissionDto input) {
        //声明一个入出院登记实体
        admission admission=input.MapToEntity();  
        //调用数据库的增加或者修改方法
        saveOrUpdate(admission);
        //把传输模型返回给前端
        return admission.MapToDto();
    }
    /**
     * 入出院登记删除
     */
    @Override
    public void Delete(IdInput input) {
        admission entity = admissionMapper.selectById(input.getId());
        admissionMapper.deleteById(entity);
    }

    /**
     * 入出院登记批量删除
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
