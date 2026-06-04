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
 * 床位预约功能的Service接口的定义清单
 */
public interface bed_reservationService extends IService<bed_reservation> {

    /**
     * 床位预约的分页查询方法接口定义
     */
    public PagedResult<bed_reservationDto> List(bed_reservationPagedInput input) ;
    /**
     * 床位预约的新增或者修改方法接口定义
     */
    public bed_reservationDto CreateOrEdit(bed_reservationDto input);

     /**
     * 获取床位预约信息
     */
    public bed_reservationDto Get(bed_reservationPagedInput input);
 	 /**
     * 床位预约删除
     */
    public void Delete(IdInput input);

    /**
     * 床位预约批量删除
     */
    public void BatchDelete(IdsInput input);
  
    /**
     * 获取当前用户的预约列表
     */
    public PagedResult<bed_reservationDto> MyList(bed_reservationPagedInput input);

    /**
     * 审核预约（管理员审核通过/拒绝）
     */
    public bed_reservationDto Audit(bed_reservationDto input);

}
