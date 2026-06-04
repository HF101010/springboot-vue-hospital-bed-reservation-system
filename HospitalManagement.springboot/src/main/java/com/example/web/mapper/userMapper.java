package com.example.web.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.web.entity.*;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息表对应的Mapper
 */
@Mapper
public interface userMapper  extends BaseMapper<user> {

}
