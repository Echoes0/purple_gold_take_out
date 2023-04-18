package com.itl.purple_gold.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.purple_gold.dto.SetmealDto;
import com.itl.purple_gold.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 根据id获取套餐信息和菜品信息
     * @param id
     * @return
     */
    SetmealDto getByIdWithDish(Long id);

    /**
     * 新增套餐，及套餐的菜品
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    void removeWithDish(List<Long> ids);

    /**
     * 更新套餐信息和菜品信息
     * @param setmealDto
     */
    void updateWithDish(SetmealDto setmealDto);
}
