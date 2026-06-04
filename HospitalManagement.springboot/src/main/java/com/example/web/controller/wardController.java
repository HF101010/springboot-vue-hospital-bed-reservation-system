package com.example.web.controller;
import com.example.web.dto.*;
import com.example.web.dto.query.*;
import com.example.web.mapper.*;
import com.example.web.service.*;
import com.example.web.tools.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.SneakyThrows;
import java.util.*;
/**
 * 病区信息控制器 
 */
@RestController()
@RequestMapping("/ward")
public class wardController {
    @Autowired
    private  wardService wardService;
    @Autowired
    private wardMapper wardMapper;
    /**
     * 病区信息分页查询
     */
    @RequestMapping(value = "/List", method = RequestMethod.POST)
    @SneakyThrows
    public PagedResult<wardDto> List(@RequestBody wardPagedInput input)  {
        return wardService.List(input);
    }
     /**
     * 单个病区信息查询接口
     */
    @RequestMapping(value = "/Get", method = RequestMethod.POST)
    @SneakyThrows
    public wardDto Get(@RequestBody wardPagedInput input) {

        return wardService.Get(input);
    }
  
    /**
     * 病区信息创建或则修改
     */
    @RequestMapping(value = "/CreateOrEdit", method = RequestMethod.POST)
    public wardDto CreateOrEdit(@RequestBody wardDto input) throws Exception {
        return wardService.CreateOrEdit(input);
    }
    /**
     * 病区信息删除
     */
    @RequestMapping(value = "/Delete", method = RequestMethod.POST)
    public void Delete(@RequestBody IdInput input)
    {
        wardService.Delete(input);
    }

    /**
     * 病区信息批量删除
     */
    @RequestMapping(value = "/BatchDelete", method = RequestMethod.POST)
    public void BatchDelete(@RequestBody IdsInput input)
    {
        wardService.BatchDelete(input);
    }
  
    /**
     * 查询某个科室的床位池信息
     */
    @RequestMapping(value = "/GetBedPool", method = RequestMethod.POST)
    @SneakyThrows
    public BedPoolDto GetBedPool(@RequestBody Map<String, String> request) {
        String department = request.get("department");
        return wardService.getBedPoolByDepartment(department);
    }

 
}
