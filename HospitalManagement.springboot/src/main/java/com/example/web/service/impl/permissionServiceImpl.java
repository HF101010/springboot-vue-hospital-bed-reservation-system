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
 * 权限信息功能实现类
 */
@Service
public class permissionServiceImpl extends ServiceImpl<permissionMapper, permission> implements permissionService {

	 /**
     * 操作数据库user表mapper对象
     */
    @Autowired
    private userMapper userMapper;
    /**
     * 操作数据库的permission表mapper对象
     */
    @Autowired
    private permissionMapper permissionMapper;

  
   /**
     * 构建表查询sql
     */
    private LambdaQueryWrapper<permission> BuilderQuery(permissionPagedInput input) {
       //声明一个支持权限信息查询的(拉姆达)表达式
        LambdaQueryWrapper<permission> queryWrapper = Wrappers.<permission>lambdaQuery()
                .eq(input.getId() != null && input.getId() != 0, permission::getId, input.getId());
   //如果前端搜索传入查询条件则拼接查询条件
      

 
    
      return queryWrapper;
    }
  
    /**
     * 处理权限信息对于的外键数据
     */
   private List<permissionDto> DispatchItem(List<permissionDto> items) throws InvocationTargetException, IllegalAccessException {
          
       for (permissionDto item : items) {       }
       
     return items; 
   }
  
    /**
     * 权限信息分页查询
     */
    @SneakyThrows
    @Override
    public PagedResult<permissionDto> List(permissionPagedInput input) {
			//构建where条件+排序
        LambdaQueryWrapper<permission> queryWrapper = BuilderQuery(input);
        // 动态排序处理
        if (input.getSortItem() != null) {
            // 根据字段名动态排序
            queryWrapper.last("ORDER BY " + input.getSortItem().getFieldName()
                    + (input.getSortItem().getIsAsc() ? " ASC" : " DESC"));
        } else {
            // 默认按ID从大到小排序
            queryWrapper = queryWrapper.orderByDesc(permission::getId);
        }

        //构建一个分页查询的model
        Page<permission> page = new Page<>(input.getPage(), input.getLimit());
         //从数据库进行分页查询获取权限信息数据
        IPage<permission> pageRecords= permissionMapper.selectPage(page, queryWrapper);
        //获取所有满足条件的数据行数
        Long totalCount= permissionMapper.selectCount(queryWrapper);
        //把permission实体转换成permission传输模型
        List<permissionDto> items= Extension.copyBeanList(pageRecords.getRecords(),permissionDto.class);

		   DispatchItem(items);
        //返回一个分页结构给前端
        return PagedResult.GetInstance(items,totalCount);

    }
  
    /**
     * 单个权限信息查询
     */
    @SneakyThrows
    @Override
    public permissionDto Get(permissionPagedInput input) {
       if(input.getId()==null)
        {
         return new permissionDto();
        }
      
       PagedResult<permissionDto> pagedResult = List(input);
        return pagedResult.getItems().stream().findFirst().orElse(new permissionDto()); 
    }

    /**
     *权限信息创建或者修改
     */
    @SneakyThrows
    @Override
    public permissionDto CreateOrEdit(permissionDto input) {
        //声明一个权限信息实体
        permission permission=input.MapToEntity();  
        //调用数据库的增加或者修改方法
        saveOrUpdate(permission);
        //把传输模型返回给前端
        return permission.MapToDto();
    }
    /**
     * 权限信息删除
     */
    @Override
    public void Delete(IdInput input) {
        permission entity = permissionMapper.selectById(input.getId());
        permissionMapper.deleteById(entity);
    }

    /**
     * 权限信息批量删除
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
