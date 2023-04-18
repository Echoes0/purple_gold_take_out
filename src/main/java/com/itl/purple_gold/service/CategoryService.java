package com.itl.purple_gold.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.purple_gold.entity.Category;

public interface CategoryService extends IService<Category> {
    /**
     * 根据id删除菜品分类
     * @param id
     */
    public void remove(Long id);
}
