package com.itl.purple_gold.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.purple_gold.mapper.ShoppingCartMapper;
import com.itl.purple_gold.service.ShoppingCartService;
import com.itl.purple_gold.entity.ShoppingCart;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
