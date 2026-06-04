package com.example.web.controller;
import com.example.web.SysConst;
import com.example.web.dto.*;
import com.example.web.dto.query.*;
import com.example.web.entity.*;
import com.example.web.mapper.*;
import com.example.web.service.*;
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
 * 角色与权限关联控制器 
 */
@RestController()
@RequestMapping("/role_permission")
public class role_permissionController {
    @Autowired
    private  role_permissionService role_permissionService;
    @Autowired
    private role_permissionMapper role_permissionMapper;
    /**
     * 角色与权限关联分页查询
     */
    @RequestMapping(value = "/List", method = RequestMethod.POST)
    @SneakyThrows
    public PagedResult<role_permissionDto> List(@RequestBody role_permissionPagedInput input)  {
        return role_permissionService.List(input);
    }
     /**
     * 单个角色与权限关联查询接口
     */
    @RequestMapping(value = "/Get", method = RequestMethod.POST)
    @SneakyThrows
    public role_permissionDto Get(@RequestBody role_permissionPagedInput input) {

        return role_permissionService.Get(input);
    }
  
    /**
     * 角色与权限关联创建或则修改
     */
    @RequestMapping(value = "/CreateOrEdit", method = RequestMethod.POST)
    public role_permissionDto CreateOrEdit(@RequestBody role_permissionDto input) throws Exception {
        return role_permissionService.CreateOrEdit(input);
    }
    /**
     * 角色与权限关联删除
     */
    @RequestMapping(value = "/Delete", method = RequestMethod.POST)
    public void Delete(@RequestBody IdInput input)
    {
        role_permissionService.Delete(input);
    }

    /**
     * 角色与权限关联批量删除
     */
    @RequestMapping(value = "/BatchDelete", method = RequestMethod.POST)
    public void BatchDelete(@RequestBody IdsInput input)
    {
        role_permissionService.BatchDelete(input);
    }
  

 
}
