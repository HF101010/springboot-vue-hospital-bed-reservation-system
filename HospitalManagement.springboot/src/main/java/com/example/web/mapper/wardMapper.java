package com.example.web.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.web.entity.*;
import org.apache.ibatis.annotations.Mapper;

/**
 * 病区信息表对应的Mapper
 */
@Mapper
public interface wardMapper  extends BaseMapper<ward> {

}
