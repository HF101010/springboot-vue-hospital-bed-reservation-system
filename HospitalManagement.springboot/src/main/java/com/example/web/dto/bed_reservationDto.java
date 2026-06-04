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
 * 床位预约类
 */
@Data
public class bed_reservationDto extends BaseDto
{

    
     
    /**
     * 患者用户
     */ 
    @JsonProperty("patient_id")
    private String patient_id;
    
     
    /**
     * 预约病区
     */ 
    @JsonProperty("ward_id")
    private String ward_id;
    
     
    /**
     * 预约床位
     */ 
    @JsonProperty("bed_id")
    private String bed_id;
    
     
    /**
     * 预约申请时间
     */ 
    @JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using= LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("reservation_time")
    private LocalDateTime reservation_time;             
    
     
    /**
     * 预约状态
     */ 
    @JsonProperty("status")
    private String status;
    
    
    /**
     * 预约科室（用于自动分配床位，不直接落库）
     */
    @JsonProperty("department")
    private String department;
    
     
    /**
     * 预约原因
     */ 
    @JsonProperty("reason")
    private String reason;
    
     
    /**
     * 审核管理员
     */ 
    @JsonProperty("audit_user_id")
    private String audit_user_id;
    
     
    /**
     * 审核时间
     */ 
    @JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using= LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("audit_time")
    private LocalDateTime audit_time;             

     @JsonProperty("bed_idDto") 
    private bedDto bed_idDto;                        
   
     @JsonProperty("ward_idDto") 
    private wardDto ward_idDto;                        
   
     @JsonProperty("patient_idDto") 
    private userDto patient_idDto;                        
   
 	 /**
     * 把床位预约传输模型转换成床位预约实体
     */
    public bed_reservation MapToEntity() throws InvocationTargetException, IllegalAccessException {
        bed_reservation bed_reservation= new bed_reservation();
     
         BeanUtils.copyProperties(bed_reservation,this);
        
        return bed_reservation;
    }

}
