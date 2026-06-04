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
 * 床位信息类
 */
@Data
public class bedDto extends BaseDto
{

    
     
    /**
     * 床位编号
     */ 
    @JsonProperty("bed_number")
    private String bed_number;
    
     
    /**
     * 所属病区
     */ 
    @JsonProperty("ward_id")
    private String ward_id;
    
     
    /**
     * 床位状态
     */ 
    @JsonProperty("status")
    private String status;
    
     
    /**
     * 备注
     */ 
    @JsonProperty("remark")
    private String remark;

     @JsonProperty("ward_idDto") 
    private wardDto ward_idDto;                        
   
 	 /**
     * 把床位信息传输模型转换成床位信息实体
     */
    public bed MapToEntity() throws InvocationTargetException, IllegalAccessException {
        bed bed= new bed();
     
         BeanUtils.copyProperties(bed,this);
        
        return bed;
    }

}
