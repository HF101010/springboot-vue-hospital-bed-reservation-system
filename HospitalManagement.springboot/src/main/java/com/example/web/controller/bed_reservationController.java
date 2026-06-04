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
 * 床位预约控制器 
 */
@RestController()
@RequestMapping("/bed_reservation")
public class bed_reservationController {
    @Autowired
    private  bed_reservationService bed_reservationService;
    @Autowired
    private bed_reservationMapper bed_reservationMapper;
    /**
     * 床位预约分页查询
     */
    @RequestMapping(value = "/List", method = RequestMethod.POST)
    @SneakyThrows
    public PagedResult<bed_reservationDto> List(@RequestBody bed_reservationPagedInput input)  {
        return bed_reservationService.List(input);
    }
     /**
     * 单个床位预约查询接口
     */
    @RequestMapping(value = "/Get", method = RequestMethod.POST)
    @SneakyThrows
    public bed_reservationDto Get(@RequestBody bed_reservationPagedInput input) {

        return bed_reservationService.Get(input);
    }
  
    /**
     * 床位预约创建或则修改
     */
    @RequestMapping(value = "/CreateOrEdit", method = RequestMethod.POST)
    public bed_reservationDto CreateOrEdit(@RequestBody bed_reservationDto input) throws Exception {
        return bed_reservationService.CreateOrEdit(input);
    }
    /**
     * 床位预约删除
     */
    @RequestMapping(value = "/Delete", method = RequestMethod.POST)
    public void Delete(@RequestBody IdInput input)
    {
        bed_reservationService.Delete(input);
    }

    /**
     * 床位预约批量删除
     */
    @RequestMapping(value = "/BatchDelete", method = RequestMethod.POST)
    public void BatchDelete(@RequestBody IdsInput input)
    {
        bed_reservationService.BatchDelete(input);
    }
  
    /**
     * 获取当前用户的预约列表
     */
    @RequestMapping(value = "/MyList", method = RequestMethod.POST)
    @SneakyThrows
    public PagedResult<bed_reservationDto> MyList(@RequestBody bed_reservationPagedInput input)  {
        return bed_reservationService.MyList(input);
    }

    /**
     * 审核预约（管理员审核通过/拒绝）
     */
    @RequestMapping(value = "/Audit", method = RequestMethod.POST)
    public bed_reservationDto Audit(@RequestBody bed_reservationDto input) throws Exception {
        return bed_reservationService.Audit(input);
    }

 
}
