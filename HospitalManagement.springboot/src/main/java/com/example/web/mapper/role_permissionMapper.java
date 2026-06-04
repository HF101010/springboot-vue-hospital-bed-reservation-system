package com.example.web.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.web.entity.*;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色与权限关联表对应的Mapper
 */
@Mapper
public interface role_permissionMapper  extends BaseMapper<role_permission> {

}
