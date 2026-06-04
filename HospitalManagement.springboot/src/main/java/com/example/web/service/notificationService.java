package com.example.web.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.web.dto.*;
import com.example.web.dto.query.*;
import com.example.web.entity.*;
import com.example.web.tools.dto.*;
import com.example.web.enums.*;
import java.lang.reflect.InvocationTargetException;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import jakarta.servlet.http.HttpServletResponse;
/**
 * 系统通知功能的Service接口的定义清单
 */
public interface notificationService extends IService<notification> {

    /**
     * 系统通知的分页查询方法接口定义
     */
    public PagedResult<notificationDto> List(notificationPagedInput input) ;
    /**
     * 系统通知的新增或者修改方法接口定义
     */
    public notificationDto CreateOrEdit(notificationDto input);

     /**
     * 获取系统通知信息
     */
    public notificationDto Get(notificationPagedInput input);
 	 /**
     * 系统通知删除
     */
    public void Delete(IdInput input);

    /**
     * 系统通知批量删除
     */
    public void BatchDelete(IdsInput input);
  
    /**
     * 获取当前用户的通知列表
     */
    public PagedResult<notificationDto> MyList(notificationPagedInput input);

    /**
     * 标记通知为已读
     */
    public void MarkAsRead(IdInput input);

}
