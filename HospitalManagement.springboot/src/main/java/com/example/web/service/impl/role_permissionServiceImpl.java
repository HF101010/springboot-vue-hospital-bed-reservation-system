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
 * 角色与权限关联功能实现类
 */
@Service
public class role_permissionServiceImpl extends ServiceImpl<role_permissionMapper, role_permission> implements role_permissionService {

	 /**
     * 操作数据库user表mapper对象
     */
    @Autowired
    private userMapper userMapper;
    /**
     * 操作数据库的role_permission表mapper对象
     */
    @Autowired
    private role_permissionMapper role_permissionMapper;
    @Autowired
    private permissionMapper  permissionMapper;                        

  
   /**
     * 构建表查询sql
     */
    private LambdaQueryWrapper<role_permission> BuilderQuery(role_permissionPagedInput input) {
       //声明一个支持角色与权限关联查询的(拉姆达)表达式
        LambdaQueryWrapper<role_permission> queryWrapper = Wrappers.<role_permission>lambdaQuery()
                .eq(input.getId() != null && input.getId() != 0, role_permission::getId, input.getId());
   //如果前端搜索传入查询条件则拼接查询条件
      

 
    
      return queryWrapper;
    }
  
    /**
     * 处理角色与权限关联对于的外键数据
     */
   private List<role_permissionDto> DispatchItem(List<role_permissionDto> items) throws InvocationTargetException, IllegalAccessException {
          
       for (role_permissionDto item : items) {           
          	            
           //查询出关联的permission表信息           
            permission  permission_idEntity= permissionMapper.selectById(item.getPermission_id());
            item.setPermission_idDto(permission_idEntity!=null?permission_idEntity.MapToDto():new permissionDto());              
       }
       
     return items; 
   }
  
    /**
     * 角色与权限关联分页查询
     */
    @SneakyThrows
    @Override
    public PagedResult<role_permissionDto> List(role_permissionPagedInput input) {
			//构建where条件+排序
        LambdaQueryWrapper<role_permission> queryWrapper = BuilderQuery(input);
        // 动态排序处理
        if (input.getSortItem() != null) {
            // 根据字段名动态排序
            queryWrapper.last("ORDER BY " + input.getSortItem().getFieldName()
                    + (input.getSortItem().getIsAsc() ? " ASC" : " DESC"));
        } else {
            // 默认按ID从大到小排序
            queryWrapper = queryWrapper.orderByDesc(role_permission::getId);
        }

        //构建一个分页查询的model
        Page<role_permission> page = new Page<>(input.getPage(), input.getLimit());
         //从数据库进行分页查询获取角色与权限关联数据
        IPage<role_permission> pageRecords= role_permissionMapper.selectPage(page, queryWrapper);
        //获取所有满足条件的数据行数
        Long totalCount= role_permissionMapper.selectCount(queryWrapper);
        //把role_permission实体转换成role_permission传输模型
        List<role_permissionDto> items= Extension.copyBeanList(pageRecords.getRecords(),role_permissionDto.class);

		   DispatchItem(items);
        //返回一个分页结构给前端
        return PagedResult.GetInstance(items,totalCount);

    }
  
    /**
     * 单个角色与权限关联查询
     */
    @SneakyThrows
    @Override
    public role_permissionDto Get(role_permissionPagedInput input) {
       if(input.getId()==null)
        {
         return new role_permissionDto();
        }
      
       PagedResult<role_permissionDto> pagedResult = List(input);
        return pagedResult.getItems().stream().findFirst().orElse(new role_permissionDto()); 
    }

    /**
     *角色与权限关联创建或者修改
     */
    @SneakyThrows
    @Override
    public role_permissionDto CreateOrEdit(role_permissionDto input) {
        //声明一个角色与权限关联实体
        role_permission role_permission=input.MapToEntity();  
        //调用数据库的增加或者修改方法
        saveOrUpdate(role_permission);
        //把传输模型返回给前端
        return role_permission.MapToDto();
    }
    /**
     * 角色与权限关联删除
     */
    @Override
    public void Delete(IdInput input) {
        role_permission entity = role_permissionMapper.selectById(input.getId());
        role_permissionMapper.deleteById(entity);
    }

    /**
     * 角色与权限关联批量删除
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
