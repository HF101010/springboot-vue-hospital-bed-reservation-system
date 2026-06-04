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
 * 角色与权限关联表
 */
@Data
@TableName("`role_permission`")
public class role_permission extends BaseEntity {

      
  	  /**
     * 角色类型
     */  
    @JsonProperty("role")
    @TableField(value="role",updateStrategy = FieldStrategy.ALWAYS)
    private String role;
      
  	  /**
     * 权限
     */  
    @JsonProperty("permission_id")
    @TableField(value="permission_id",updateStrategy = FieldStrategy.ALWAYS)
    private String permission_id;
  
    /**
     * 把角色与权限关联实体转换成角色与权限关联传输模型
     */
    public role_permissionDto MapToDto() throws InvocationTargetException, IllegalAccessException {
        role_permissionDto role_permissionDto = new role_permissionDto();
       
        BeanUtils.copyProperties(role_permissionDto,this);
       
        return role_permissionDto;
    }

}
