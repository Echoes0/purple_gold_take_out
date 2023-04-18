package com.itl.purple_gold.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.purple_gold.entity.OrderDetail;
import com.itl.purple_gold.mapper.OrderDetailMapper;
import com.itl.purple_gold.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}