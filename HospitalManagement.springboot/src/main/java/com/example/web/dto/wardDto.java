package com.example.web.dto;
import com.example.web.enums.*;
import com.example.web.tools.dto.BaseDto;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Date;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.example.web.entity.*;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
/**
 * 病区信息类
 */
@Data
public class wardDto extends BaseDto
{

    
     
    /**
     * 病区名称
     */ 
    @JsonProperty("ward_name")
    private String ward_name;
    
     
    /**
     * 所属科室
     */ 
    @JsonProperty("department")
    private String department;
    
     
    /**
     * 病区总床位数
     */ 
    @JsonProperty("total_beds")
    private Integer total_beds;          
    
     
    /**
     * 可用床位数
     */ 
    @JsonProperty("available_beds")
    private Integer available_beds;          
    
     
    /**
     * 备注信息
     */ 
    @JsonProperty("remark")
    private String remark;

 	 /**
     * 把病区信息传输模型转换成病区信息实体
     */
    public ward MapToEntity() throws InvocationTargetException, IllegalAccessException {
        ward ward= new ward();
     
         BeanUtils.copyProperties(ward,this);
        
        return ward;
    }

}
