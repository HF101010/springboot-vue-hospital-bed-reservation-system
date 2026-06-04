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
 * 权限信息类
 */
@Data
public class permissionDto extends BaseDto
{

    
     
    /**
     * 权限名称
     */ 
    @JsonProperty("permission_name")
    private String permission_name;
    
     
    /**
     * 权限编码（用于后端权限控制）
     */ 
    @JsonProperty("permission_code")
    private String permission_code;
    
     
    /**
     * 权限描述
     */ 
    @JsonProperty("description")
    private String description;

 	 /**
     * 把权限信息传输模型转换成权限信息实体
     */
    public permission MapToEntity() throws InvocationTargetException, IllegalAccessException {
        permission permission= new permission();
     
         BeanUtils.copyProperties(permission,this);
        
        return permission;
    }

}
