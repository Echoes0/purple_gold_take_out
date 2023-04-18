package com.itl.purple_gold.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.purple_gold.entity.Orders;

public interface OrderService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);
}
