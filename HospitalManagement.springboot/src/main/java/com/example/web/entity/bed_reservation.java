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
 * 床位预约表
 */
@Data
@TableName("`bed_reservation`")
public class bed_reservation extends BaseEntity {

      
  	  /**
     * 患者用户
     */  
    @JsonProperty("patient_id")
    @TableField(value="patient_id",updateStrategy = FieldStrategy.ALWAYS)
    private String patient_id;
      
  	  /**
     * 预约病区
     */  
    @JsonProperty("ward_id")
    @TableField(value="ward_id",updateStrategy = FieldStrategy.ALWAYS)
    private String ward_id;
      
  	  /**
     * 预约床位
     */  
    @JsonProperty("bed_id")
    @TableField(value="bed_id",updateStrategy = FieldStrategy.ALWAYS)
    private String bed_id;
      
    /**
     * 预约申请时间
     */  
    @JsonProperty("reservation_time")
    @TableField(value="reservation_time",updateStrategy = FieldStrategy.ALWAYS)
    @JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using= LocalDateTimeDeserializer.class)
    private LocalDateTime reservation_time;             
      
  	  /**
     * 预约状态
     */  
    @JsonProperty("status")
    @TableField(value="status",updateStrategy = FieldStrategy.ALWAYS)
    private String status;
      
  	  /**
     * 预约原因
     */  
    @JsonProperty("reason")
    @TableField(value="reason",updateStrategy = FieldStrategy.ALWAYS)
    private String reason;
      
  	  /**
     * 审核管理员
     */  
    @JsonProperty("audit_user_id")
    @TableField(value="audit_user_id",updateStrategy = FieldStrategy.ALWAYS)
    private String audit_user_id;
      
    /**
     * 审核时间
     */  
    @JsonProperty("audit_time")
    @TableField(value="audit_time",updateStrategy = FieldStrategy.ALWAYS)
    @JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using= LocalDateTimeDeserializer.class)
    private LocalDateTime audit_time;             
  
    /**
     * 把床位预约实体转换成床位预约传输模型
     */
    public bed_reservationDto MapToDto() throws InvocationTargetException, IllegalAccessException {
        bed_reservationDto bed_reservationDto = new bed_reservationDto();
       
        BeanUtils.copyProperties(bed_reservationDto,this);
       
        return bed_reservationDto;
    }

}
