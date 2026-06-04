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
 * 权限信息表
 */
@Data
@TableName("`permission`")
public class permission extends BaseEntity {

      
  	  /**
     * 权限名称
     */  
    @JsonProperty("permission_name")
    @TableField(value="permission_name",updateStrategy = FieldStrategy.ALWAYS)
    private String permission_name;
      
  	  /**
     * 权限编码（用于后端权限控制）
     */  
    @JsonProperty("permission_code")
    @TableField(value="permission_code",updateStrategy = FieldStrategy.ALWAYS)
    private String permission_code;
      
  	  /**
     * 权限描述
     */  
    @JsonProperty("description")
    @TableField(value="description",updateStrategy = FieldStrategy.ALWAYS)
    private String description;
  
    /**
     * 把权限信息实体转换成权限信息传输模型
     */
    public permissionDto MapToDto() throws InvocationTargetException, IllegalAccessException {
        permissionDto permissionDto = new permissionDto();
       
        BeanUtils.copyProperties(permissionDto,this);
       
        return permissionDto;
    }

}
