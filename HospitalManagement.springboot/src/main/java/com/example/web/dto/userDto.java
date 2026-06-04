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
 * 用户信息类
 */
@Data
public class userDto extends BaseDto
{

    
     
    /**
     * 用户名
     */ 
    @JsonProperty("username")
    private String username;
    
     
    /**
     * 登录密码（加密存储）
     */ 
    @JsonProperty("password")
    private String password;

    /**
     * 原始密码（用于修改密码时验证）
     */
    @JsonProperty("orginPassword")
    private String orginPassword;
    
     
    /**
     * 真实姓名
     */ 
    @JsonProperty("real_name")
    private String real_name;
    
     
    /**
     * 性别
     */ 
    @JsonProperty("gender")
    private String gender;
    
     
    /**
     * 联系电话
     */ 
    @JsonProperty("phone")
    private String phone;
    
     
    /**
     * 用户角色
     */ 
    @JsonProperty("role")
    private String role;
    
     
    /**
     * 账号状态：1启用，0禁用
     */ 
    @JsonProperty("status")
    private Boolean status;          

 	 /**
     * 把用户信息传输模型转换成用户信息实体
     */
    public user MapToEntity() throws InvocationTargetException, IllegalAccessException {
        user user= new user();
     
         BeanUtils.copyProperties(user,this);
        
        return user;
    }

}
