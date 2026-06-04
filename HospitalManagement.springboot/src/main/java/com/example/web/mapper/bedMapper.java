package com.example.web.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.web.entity.*;
import org.apache.ibatis.annotations.Mapper;

/**
 * 床位信息表对应的Mapper
 */
@Mapper
public interface bedMapper  extends BaseMapper<bed> {

}
