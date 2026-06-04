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
import com.example.web.tools.BaseContext;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
/**
 * 系统通知功能实现类
 */
@Service
public class notificationServiceImpl extends ServiceImpl<notificationMapper, notification> implements notificationService {

    /**
     * 操作数据库的notification表mapper对象
     */
    @Autowired
    private notificationMapper notificationMapper;
    @Autowired
    private userMapper  userMapper;                        

  
   /**
     * 构建表查询sql
     */
    private LambdaQueryWrapper<notification> BuilderQuery(notificationPagedInput input) {
       //声明一个支持系统通知查询的(拉姆达)表达式
        LambdaQueryWrapper<notification> queryWrapper = Wrappers.<notification>lambdaQuery()
                .eq(input.getId() != null && input.getId() != 0, notification::getId, input.getId());
   //如果前端搜索传入查询条件则拼接查询条件
        if (Extension.isNotNullOrEmpty(input.getKeyword())) {
            queryWrapper.like(notification::getTitle, input.getKeyword());
        }
        if (input.getIsRead() != null) {
            queryWrapper.eq(notification::getIs_read, input.getIsRead());
        }
    
      return queryWrapper;
    }
  
    /**
     * 处理系统通知对于的外键数据
     */
   private List<notificationDto> DispatchItem(List<notificationDto> items) throws InvocationTargetException, IllegalAccessException {
          
       for (notificationDto item : items) {           
          	            
           //查询出关联的user表信息           
            user  receiver_idEntity= userMapper.selectById(item.getReceiver_id());
            item.setReceiver_idDto(receiver_idEntity!=null?receiver_idEntity.MapToDto():new userDto());              
           
          	            
           //查询出关联的user表信息           
            user  sender_idEntity= userMapper.selectById(item.getSender_id());
            item.setSender_idDto(sender_idEntity!=null?sender_idEntity.MapToDto():new userDto());              
       }
       
     return items; 
   }
  
    /**
     * 系统通知分页查询
     */
    @SneakyThrows
    @Override
    public PagedResult<notificationDto> List(notificationPagedInput input) {
			//构建where条件+排序
        LambdaQueryWrapper<notification> queryWrapper = BuilderQuery(input);
        // 动态排序处理
        if (input.getSortItem() != null) {
            // 根据字段名动态排序
            queryWrapper.last("ORDER BY " + input.getSortItem().getFieldName()
                    + (input.getSortItem().getIsAsc() ? " ASC" : " DESC"));
        } else {
            // 默认按ID从大到小排序
            queryWrapper = queryWrapper.orderByDesc(notification::getId);
        }

        //构建一个分页查询的model
        Page<notification> page = new Page<>(input.getPage(), input.getLimit());
         //从数据库进行分页查询获取系统通知数据
        IPage<notification> pageRecords= notificationMapper.selectPage(page, queryWrapper);
        //获取所有满足条件的数据行数
        Long totalCount= notificationMapper.selectCount(queryWrapper);
        //把notification实体转换成notification传输模型
        List<notificationDto> items= Extension.copyBeanList(pageRecords.getRecords(),notificationDto.class);

		   DispatchItem(items);
        //返回一个分页结构给前端
        return PagedResult.GetInstance(items,totalCount);

    }
  
    /**
     * 单个系统通知查询
     */
    @SneakyThrows
    @Override
    public notificationDto Get(notificationPagedInput input) {
       if(input.getId()==null)
        {
         return new notificationDto();
        }
      
       PagedResult<notificationDto> pagedResult = List(input);
        return pagedResult.getItems().stream().findFirst().orElse(new notificationDto()); 
    }

    /**
     *系统通知创建或者修改
     */
    @SneakyThrows
    @Override
    public notificationDto CreateOrEdit(notificationDto input) {
        //声明一个系统通知实体
        notification notification=input.MapToEntity();  
        
        // 如果是新增通知，确保create_time被设置
        boolean isNew = (input.getId() == null || input.getId() == 0);
        if (isNew && notification.getCreate_time() == null) {
            notification.setCreate_time(LocalDateTime.now());
        }
        
        //调用数据库的增加或者修改方法
        saveOrUpdate(notification);
        
        // 重新查询以确保返回最新数据（特别是新增时获取ID和所有字段）
        notification savedNotification = null;
        if (notification.getId() != null) {
            savedNotification = notificationMapper.selectById(notification.getId());
        }
        //把传输模型返回给前端
        return savedNotification != null ? savedNotification.MapToDto() : notification.MapToDto();
    }
    /**
     * 系统通知删除
     */
    @Override
    public void Delete(IdInput input) {
        notification entity = notificationMapper.selectById(input.getId());
        notificationMapper.deleteById(entity);
    }

    /**
     * 系统通知批量删除
     */
    @Override
    public void BatchDelete(IdsInput input) {
        for (Integer id : input.getIds()) {
            IdInput idInput = new IdInput();
            idInput.setId(id);
            Delete(idInput);
        }
    }

    /**
     * 获取当前用户的通知列表
     */
    @SneakyThrows
    @Override
    public PagedResult<notificationDto> MyList(notificationPagedInput input) {
        // 获取当前用户ID
        Integer currentUserId = BaseContext.getCurrentUserDto().getUserId();
        if (currentUserId == null) {
            throw new CustomException("请先登录");
        }
        
        // 构建查询条件，只查询当前用户的通知
        LambdaQueryWrapper<notification> queryWrapper = BuilderQuery(input);
        queryWrapper.eq(notification::getReceiver_id, currentUserId.toString());
        
        // 动态排序处理
        if (input.getSortItem() != null) {
            queryWrapper.last("ORDER BY " + input.getSortItem().getFieldName()
                    + (input.getSortItem().getIsAsc() ? " ASC" : " DESC"));
        } else {
            queryWrapper = queryWrapper.orderByDesc(notification::getId);
        }

        // 构建分页查询
        Page<notification> page = new Page<>(input.getPage(), input.getLimit());
        IPage<notification> pageRecords = notificationMapper.selectPage(page, queryWrapper);
        Long totalCount = notificationMapper.selectCount(queryWrapper);
        List<notificationDto> items = Extension.copyBeanList(pageRecords.getRecords(), notificationDto.class);

        DispatchItem(items);
        return PagedResult.GetInstance(items, totalCount);
    }

    /**
     * 标记通知为已读
     */
    @Override
    public void MarkAsRead(IdInput input) {
        notification notification = notificationMapper.selectById(input.getId());
        if (notification == null) {
            throw new CustomException("通知不存在");
        }
        
        // 验证是否为当前用户的通知
        Integer currentUserId = BaseContext.getCurrentUserDto().getUserId();
        if (currentUserId == null || !currentUserId.toString().equals(notification.getReceiver_id())) {
            throw new CustomException("无权操作此通知");
        }
        
        notification.setIs_read(true);
        notificationMapper.updateById(notification);
    }
}
