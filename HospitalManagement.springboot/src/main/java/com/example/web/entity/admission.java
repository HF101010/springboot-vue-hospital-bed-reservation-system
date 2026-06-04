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
 * 入出院登记表
 */
@Data
@TableName("`admission`")
public class admission extends BaseEntity {

      
  	  /**
     * 患者用户
     */  
    @JsonProperty("patient_id")
    @TableField(value="patient_id",updateStrategy = FieldStrategy.ALWAYS)
    private String patient_id;
      
  	  /**
     * 床位
     */  
    @JsonProperty("bed_id")
    @TableField(value="bed_id",updateStrategy = FieldStrategy.ALWAYS)
    private String bed_id;
      
    /**
     * 入院日期
     */  
    @JsonProperty("admission_date")
    @TableField(value="admission_date",updateStrategy = FieldStrategy.ALWAYS)
    @JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using= LocalDateTimeDeserializer.class)
    private LocalDateTime admission_date;             
      
    /**
     * 出院日期
     */  
    @JsonProperty("discharge_date")
    @TableField(value="discharge_date",updateStrategy = FieldStrategy.ALWAYS)
    @JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using= LocalDateTimeDeserializer.class)
    private LocalDateTime discharge_date;             
      
  	  /**
     * 入出院状态
     */  
    @JsonProperty("status")
    @TableField(value="status",updateStrategy = FieldStrategy.ALWAYS)
    private String status;
      
  	  /**
     * 备注
     */  
    @JsonProperty("remark")
    @TableField(value="remark",updateStrategy = FieldStrategy.ALWAYS)
    private String remark;
  
    /**
     * 把入出院登记实体转换成入出院登记传输模型
     */
    public admissionDto MapToDto() throws InvocationTargetException, IllegalAccessException {
        admissionDto admissionDto = new admissionDto();
       
        BeanUtils.copyProperties(admissionDto,this);
       
        return admissionDto;
    }

}
