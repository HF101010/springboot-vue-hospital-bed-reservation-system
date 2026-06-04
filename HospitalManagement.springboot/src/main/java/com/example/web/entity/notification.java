package com.example.web.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.sql.Date;
import java.sql.Timestamp;
import lombok.Data;
import java.time.LocalDateTime;
import com.example.web.dto.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
/**
 * 系统通知表
 */
@Data
@TableName("`notification`")
public class notification extends BaseEntity {

      
  	  /**
     * 通知标题
     */  
    @JsonProperty("title")
    @TableField(value="title",updateStrategy = FieldStrategy.ALWAYS)
    private String title;
      
  	  /**
     * 通知内容
     */  
    @JsonProperty("content")
    @TableField(value="content",updateStrategy = FieldStrategy.ALWAYS)
    private String content;
      
  	  /**
     * 发送人
     */  
    @JsonProperty("sender_id")
    @TableField(value="sender_id",updateStrategy = FieldStrategy.ALWAYS)
    private String sender_id;
      
  	  /**
     * 接收人
     */  
    @JsonProperty("receiver_id")
    @TableField(value="receiver_id",updateStrategy = FieldStrategy.ALWAYS)
    private String receiver_id;
      
    /**
     * 发送时间
     */  
    @JsonProperty("create_time")
    @TableField(value="create_time",updateStrategy = FieldStrategy.ALWAYS)
    @JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using= LocalDateTimeDeserializer.class)
    private LocalDateTime create_time;             
      
    /**
     * 是否已读：0未读，1已读
     */  
    @JsonProperty("is_read")
    @TableField(value="is_read",updateStrategy = FieldStrategy.ALWAYS)
    private Boolean is_read;          
  
    /**
     * 把系统通知实体转换成系统通知传输模型
     */
    public notificationDto MapToDto() throws InvocationTargetException, IllegalAccessException {
        notificationDto notificationDto = new notificationDto();
       
        BeanUtils.copyProperties(notificationDto,this);
       
        return notificationDto;
    }

}
