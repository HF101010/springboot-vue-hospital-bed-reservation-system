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
 * 用户信息功能的Service接口的定义清单
 */
public interface userService extends IService<user> {

    /**
     * 用户信息的分页查询方法接口定义
     */
    public PagedResult<userDto> List(userPagedInput input) ;
    /**
     * 用户信息的新增或者修改方法接口定义
     */
    public userDto CreateOrEdit(userDto input);

     /**
     * 获取用户信息信息
     */
    public userDto Get(userPagedInput input);
 	 /**
     * 用户信息删除
     */
    public void Delete(IdInput input);

    /**
     * 用户信息批量删除
     */
    public void BatchDelete(IdsInput input);

    /**
     * 用户登录
     */
    public String SignIn(userDto input);

    /**
     * 注册
     */
    public userDto Register(userDto input);

    /**
     * 找回密码
     */
    void ForgetPassword(userDto input);

    /**
     * 修改密码
     */
    void ChangePassword(userDto input);

    /**
     * 重置密码
     */
    void ResetPassword(userDto input);

    /**
     * 用户导出
     */
    public void Export(@RequestParam String query, HttpServletResponse response) throws IOException;

}
