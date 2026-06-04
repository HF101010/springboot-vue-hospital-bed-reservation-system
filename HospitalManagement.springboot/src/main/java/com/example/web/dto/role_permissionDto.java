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
 * 角色与权限关联类
 */
@Data
public class role_permissionDto extends BaseDto
{

    
     
    /**
     * 角色类型
     */ 
    @JsonProperty("role")
    private String role;
    
     
    /**
     * 权限
     */ 
    @JsonProperty("permission_id")
    private String permission_id;

     @JsonProperty("permission_idDto") 
    private permissionDto permission_idDto;                        
   
 	 /**
     * 把角色与权限关联传输模型转换成角色与权限关联实体
     */
    public role_permission MapToEntity() throws InvocationTargetException, IllegalAccessException {
        role_permission role_permission= new role_permission();
     
         BeanUtils.copyProperties(role_permission,this);
        
        return role_permission;
    }

}
