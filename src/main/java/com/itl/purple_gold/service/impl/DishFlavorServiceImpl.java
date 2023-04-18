package com.itl.purple_gold.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.purple_gold.entity.DishFlavor;
import com.itl.purple_gold.mapper.DishFlavorMapper;
import com.itl.purple_gold.service.DishFlavorService;
import com.itl.purple_gold.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
