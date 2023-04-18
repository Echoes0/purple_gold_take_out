package com.itl.purple_gold.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.purple_gold.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
