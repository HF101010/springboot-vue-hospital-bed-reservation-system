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
 * 角色与权限关联功能的Service接口的定义清单
 */
public interface role_permissionService extends IService<role_permission> {

    /**
     * 角色与权限关联的分页查询方法接口定义
     */
    public PagedResult<role_permissionDto> List(role_permissionPagedInput input) ;
    /**
     * 角色与权限关联的新增或者修改方法接口定义
     */
    public role_permissionDto CreateOrEdit(role_permissionDto input);

     /**
     * 获取角色与权限关联信息
     */
    public role_permissionDto Get(role_permissionPagedInput input);
 	 /**
     * 角色与权限关联删除
     */
    public void Delete(IdInput input);

    /**
     * 角色与权限关联批量删除
     */
    public void BatchDelete(IdsInput input);
  

}
