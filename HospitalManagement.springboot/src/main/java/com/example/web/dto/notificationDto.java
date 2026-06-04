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
 * 系统通知类
 */
@Data
public class notificationDto extends BaseDto
{

    
     
    /**
     * 通知标题
     */ 
    @JsonProperty("title")
    private String title;
    
     
    /**
     * 通知内容
     */ 
    @JsonProperty("content")
    private String content;
    
     
    /**
     * 发送人
     */ 
    @JsonProperty("sender_id")
    private String sender_id;
    
     
    /**
     * 接收人
     */ 
    @JsonProperty("receiver_id")
    private String receiver_id;
    
     
    /**
     * 发送时间
     */ 
    @JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using= LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("create_time")
    private LocalDateTime create_time;             
    
     
    /**
     * 是否已读：0未读，1已读
     */ 
    @JsonProperty("is_read")
    private Boolean is_read;          

     @JsonProperty("receiver_idDto") 
    private userDto receiver_idDto;                        
   
     @JsonProperty("sender_idDto") 
    private userDto sender_idDto;                        
   
 	 /**
     * 把系统通知传输模型转换成系统通知实体
     */
    public notification MapToEntity() throws InvocationTargetException, IllegalAccessException {
        notification notification= new notification();
     
         BeanUtils.copyProperties(notification,this);
        
        return notification;
    }

}
