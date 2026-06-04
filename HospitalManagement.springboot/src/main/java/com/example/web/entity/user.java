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
 * 用户信息表
 */
@Data
@TableName("`user`")
public class user extends BaseEntity {

      
  	  /**
     * 用户名
     */  
    @JsonProperty("username")
    @TableField(value="username",updateStrategy = FieldStrategy.ALWAYS)
    private String username;
      
  	  /**
     * 登录密码（加密存储）
     */  
    @JsonProperty("password")
    @TableField(value="password",updateStrategy = FieldStrategy.ALWAYS)
    private String password;
      
  	  /**
     * 真实姓名
     */  
    @JsonProperty("real_name")
    @TableField(value="real_name",updateStrategy = FieldStrategy.ALWAYS)
    private String real_name;
      
  	  /**
     * 性别
     */  
    @JsonProperty("gender")
    @TableField(value="gender",updateStrategy = FieldStrategy.ALWAYS)
    private String gender;
      
  	  /**
     * 联系电话
     */  
    @JsonProperty("phone")
    @TableField(value="phone",updateStrategy = FieldStrategy.ALWAYS)
    private String phone;
      
  	  /**
     * 用户角色
     */  
    @JsonProperty("role")
    @TableField(value="role",updateStrategy = FieldStrategy.ALWAYS)
    private String role;
      
    /**
     * 账号状态：1启用，0禁用
     */  
    @JsonProperty("status")
    @TableField(value="status",updateStrategy = FieldStrategy.ALWAYS)
    private Boolean status;          
  
    /**
     * 把用户信息实体转换成用户信息传输模型
     */
    public userDto MapToDto() throws InvocationTargetException, IllegalAccessException {
        userDto userDto = new userDto();
       
        BeanUtils.copyProperties(userDto,this);
       
        return userDto;
    }

}
