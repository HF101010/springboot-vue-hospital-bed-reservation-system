package com.example.web.entity;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

import org.apache.commons.beanutils.BeanUtils;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.web.dto.AppUserDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@TableName("`user`")
public class AppUser extends BaseEntity {

    /**
     * 账号
     */
    @JsonProperty("UserName")
    @TableField(value = "username", updateStrategy = FieldStrategy.ALWAYS)
    private String UserName;
    /**
     * 密码
     */
    @JsonProperty("Password")
    @TableField(value = "password", updateStrategy = FieldStrategy.ALWAYS)
    private String Password;

    /**
     * 邮箱
     */
    @JsonProperty("Email")
    @TableField(value = "email", updateStrategy = FieldStrategy.ALWAYS)
    private String Email;
    /**
     * 头像
     */
    @JsonProperty("ImageUrls")
    @TableField(value = "image_urls", updateStrategy = FieldStrategy.ALWAYS)
    private String ImageUrls;
    /**
     * 名称
     */
    @JsonProperty("Name")
    @TableField(value = "real_name", updateStrategy = FieldStrategy.ALWAYS)
    private String Name;
    /**
     * 手机号码
     */
    @JsonProperty("PhoneNumber")
    @TableField(value = "phone", updateStrategy = FieldStrategy.ALWAYS)
    private String PhoneNumber;
    /**
     * 出生年月（仅日期）
     */
    @JsonProperty("Birth")
    @TableField(value = "birth", updateStrategy = FieldStrategy.ALWAYS)
    private LocalDate Birth;

    /**
     * 账号角色
     */
    @JsonProperty("RoleType")
    @TableField(value = "role_type", updateStrategy = FieldStrategy.ALWAYS)
    private Integer RoleType;
    /**
     * 把账号实体转换成账号传输模型
     */
    public AppUserDto MapToDto() throws InvocationTargetException, IllegalAccessException {
        AppUserDto AppUserDto = new AppUserDto();
        BeanUtils.copyProperties(AppUserDto, this);
        return AppUserDto;
    }

}
