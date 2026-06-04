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
 * 入出院登记功能的Service接口的定义清单
 */
public interface admissionService extends IService<admission> {

    /**
     * 入出院登记的分页查询方法接口定义
     */
    public PagedResult<admissionDto> List(admissionPagedInput input) ;
    /**
     * 入出院登记的新增或者修改方法接口定义
     */
    public admissionDto CreateOrEdit(admissionDto input);

     /**
     * 获取入出院登记信息
     */
    public admissionDto Get(admissionPagedInput input);
 	 /**
     * 入出院登记删除
     */
    public void Delete(IdInput input);

    /**
     * 入出院登记批量删除
     */
    public void BatchDelete(IdsInput input);
  

}
