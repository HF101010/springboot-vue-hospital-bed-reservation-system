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
 * 床位信息功能的Service接口的定义清单
 */
public interface bedService extends IService<bed> {

    /**
     * 床位信息的分页查询方法接口定义
     */
    public PagedResult<bedDto> List(bedPagedInput input) ;
    /**
     * 床位信息的新增或者修改方法接口定义
     */
    public bedDto CreateOrEdit(bedDto input);

     /**
     * 获取床位信息信息
     */
    public bedDto Get(bedPagedInput input);
 	 /**
     * 床位信息删除
     */
    public void Delete(IdInput input);

    /**
     * 床位信息批量删除
     */
    public void BatchDelete(IdsInput input);
  

}
