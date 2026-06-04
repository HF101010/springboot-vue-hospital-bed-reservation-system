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
 * 入出院登记类
 */
@Data
public class admissionDto extends BaseDto
{

    
     
    /**
     * 患者用户
     */ 
    @JsonProperty("patient_id")
    private String patient_id;
    
     
    /**
     * 床位
     */ 
    @JsonProperty("bed_id")
    private String bed_id;
    
     
    /**
     * 入院日期
     */ 
    @JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using= LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("admission_date")
    private LocalDateTime admission_date;             
    
     
    /**
     * 出院日期
     */ 
    @JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using= LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("discharge_date")
    private LocalDateTime discharge_date;             
    
     
    /**
     * 入出院状态
     */ 
    @JsonProperty("status")
    private String status;
    
     
    /**
     * 备注
     */ 
    @JsonProperty("remark")
    private String remark;

     @JsonProperty("patient_idDto") 
    private userDto patient_idDto;                        
   
     @JsonProperty("bed_idDto") 
    private bedDto bed_idDto;                        
   
 	 /**
     * 把入出院登记传输模型转换成入出院登记实体
     */
    public admission MapToEntity() throws InvocationTargetException, IllegalAccessException {
        admission admission= new admission();
     
         BeanUtils.copyProperties(admission,this);
        
        return admission;
    }

}
