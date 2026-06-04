package com.example.web.controller;
import com.example.web.SysConst;
import com.example.web.dto.*;
import com.example.web.dto.query.*;
import com.example.web.entity.*;
import com.example.web.mapper.*;
import com.example.web.service.*;
import com.example.web.tools.BaseContext;
import com.example.web.tools.dto.*;
import com.example.web.tools.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.SneakyThrows;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletResponse;
/**
 * 用户信息控制器 
 */
@RestController()
@RequestMapping("/user")
public class userController {
    @Autowired
    private  userService userService;
    @Autowired
    private userMapper userMapper;
    /**
     * 用户信息分页查询
     */
    @RequestMapping(value = "/List", method = RequestMethod.POST)
    @SneakyThrows
    public PagedResult<userDto> List(@RequestBody userPagedInput input)  {
        return userService.List(input);
    }
     /**
     * 单个用户信息查询接口
     */
    @RequestMapping(value = "/Get", method = RequestMethod.POST)
    @SneakyThrows
    public userDto Get(@RequestBody userPagedInput input) {

        return userService.Get(input);
    }
  
    /**
     * 用户信息创建或则修改
     */
    @RequestMapping(value = "/CreateOrEdit", method = RequestMethod.POST)
    public userDto CreateOrEdit(@RequestBody userDto input) throws Exception {
        return userService.CreateOrEdit(input);
    }
    /**
     * 用户信息删除
     */
    @RequestMapping(value = "/Delete", method = RequestMethod.POST)
    public void Delete(@RequestBody IdInput input)
    {
        userService.Delete(input);
    }

    /**
     * 用户信息批量删除
     */
    @RequestMapping(value = "/BatchDelete", method = RequestMethod.POST)
    public void BatchDelete(@RequestBody IdsInput input)
    {
        userService.BatchDelete(input);
    }

    /**
     * 用户登录
     */
    @RequestMapping(value = "/SignIn", method = RequestMethod.POST)
    public ResponseData<String> SignIn(@RequestBody userDto input, HttpServletRequest request) {
        String token = userService.SignIn(input);
        return ResponseData.GetResponseDataInstance(token, "登录成功", true);
    }

    /**
     * 获取用户信息（通过token）
     */
    @SneakyThrows
    @RequestMapping(value = "/GetByToken", method = RequestMethod.POST)
    public userDto GetByToken(@RequestHeader("Authorization") String token) {
        Integer userId = BaseContext.getCurrentUserDto().getUserId();
        userPagedInput queryInput = new userPagedInput();
        queryInput.setId(userId);
        userDto userDto = userService.Get(queryInput);
        return userDto;
    }

    /**
     * 用户注册接口
     */
    @RequestMapping(value = "/Register", method = RequestMethod.POST)
    public userDto Register(@RequestBody userDto input) throws Exception {
        return userService.Register(input);
    }

    /**
     * 找回密码
     */
    @RequestMapping(value = "/ForgetPassword", method = RequestMethod.POST)
    public void ForgetPassword(@RequestBody userDto input) throws Exception {
        userService.ForgetPassword(input);
    }

    /**
     * 修改密码
     */
    @RequestMapping(value = "/ChangePassword", method = RequestMethod.POST)
    public void ChangePassword(@RequestBody userDto input) throws Exception {
        userService.ChangePassword(input);
    }

    /**
     * 重置密码
     */
    @RequestMapping(value = "/ResetPassword", method = RequestMethod.POST)
    public void ResetPassword(@RequestBody userDto input) throws Exception {
        userService.ResetPassword(input);
    }

    /**
     * 用户导出
     */
    @RequestMapping(value = "/Export", method = RequestMethod.GET)
    public void Export(@RequestParam String query, HttpServletResponse response) throws IOException {
        userService.Export(query, response);
    }
}
