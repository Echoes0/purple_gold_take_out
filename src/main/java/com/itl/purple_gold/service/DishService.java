package com.itl.purple_gold.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.purple_gold.dto.DishDto;
import com.itl.purple_gold.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    /**
     * 新增菜品同时添加菜品对应的口味数据
     * @param dishDto
     */
    public void saveWithFlavor(DishDto dishDto);

    /**
     * 根据id获取菜品信息和口味信息
     * @param id
     * @return
     */
    public DishDto getByIdWithFlavor(Long id);

    /**
     * 更新菜品信息和口味信息
     * @param dishDto
     */
    public void updateWithFlavor(DishDto dishDto);

    /**
     * 删除菜品，同时需要删除菜品和口味的关联数据
     * @param ids
     */
    void removeWithFlavor(List<Long> ids);
}
