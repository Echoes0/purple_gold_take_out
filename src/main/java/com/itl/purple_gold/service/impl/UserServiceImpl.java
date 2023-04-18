package com.itl.purple_gold.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.purple_gold.entity.User;
import com.itl.purple_gold.mapper.UserMapper;
import com.itl.purple_gold.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
