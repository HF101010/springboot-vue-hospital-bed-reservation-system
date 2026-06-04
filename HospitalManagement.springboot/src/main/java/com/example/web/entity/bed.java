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
 * 床位信息表
 */
@Data
@TableName("`bed`")
public class bed extends BaseEntity {

      
  	  /**
     * 床位编号
     */  
    @JsonProperty("bed_number")
    @TableField(value="bed_number",updateStrategy = FieldStrategy.ALWAYS)
    private String bed_number;
      
  	  /**
     * 所属病区
     */  
    @JsonProperty("ward_id")
    @TableField(value="ward_id",updateStrategy = FieldStrategy.ALWAYS)
    private String ward_id;
      
  	  /**
     * 床位状态
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
     * 把床位信息实体转换成床位信息传输模型
     */
    public bedDto MapToDto() throws InvocationTargetException, IllegalAccessException {
        bedDto bedDto = new bedDto();
       
        BeanUtils.copyProperties(bedDto,this);
       
        return bedDto;
    }

}
