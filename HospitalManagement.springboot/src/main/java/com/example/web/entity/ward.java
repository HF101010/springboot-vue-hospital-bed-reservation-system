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
 * 病区信息表
 */
@Data
@TableName("`ward`")
public class ward extends BaseEntity {

      
  	  /**
     * 病区名称
     */  
    @JsonProperty("ward_name")
    @TableField(value="ward_name",updateStrategy = FieldStrategy.ALWAYS)
    private String ward_name;
      
  	  /**
     * 所属科室
     */  
    @JsonProperty("department")
    @TableField(value="department",updateStrategy = FieldStrategy.ALWAYS)
    private String department;
      
    /**
     * 病区总床位数
     */  
    @JsonProperty("total_beds")
    @TableField(value="total_beds",updateStrategy = FieldStrategy.ALWAYS)
    private Integer total_beds;          
      
    /**
     * 可用床位数
     */  
    @JsonProperty("available_beds")
    @TableField(value="available_beds",updateStrategy = FieldStrategy.ALWAYS)
    private Integer available_beds;          
      
  	  /**
     * 备注信息
     */  
    @JsonProperty("remark")
    @TableField(value="remark",updateStrategy = FieldStrategy.ALWAYS)
    private String remark;
  
    /**
     * 把病区信息实体转换成病区信息传输模型
     */
    public wardDto MapToDto() throws InvocationTargetException, IllegalAccessException {
        wardDto wardDto = new wardDto();
       
        BeanUtils.copyProperties(wardDto,this);
       
        return wardDto;
    }

}
