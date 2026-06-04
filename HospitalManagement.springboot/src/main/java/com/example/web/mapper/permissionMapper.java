package com.example.web.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.web.entity.*;
import org.apache.ibatis.annotations.Mapper;

/**
 * 权限信息表对应的Mapper
 */
@Mapper
public interface permissionMapper  extends BaseMapper<permission> {

}
