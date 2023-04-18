package com.itl.purple_gold.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.purple_gold.common.CustomerException;
import com.itl.purple_gold.entity.Category;
import com.itl.purple_gold.entity.Dish;
import com.itl.purple_gold.entity.Setmeal;
import com.itl.purple_gold.mapper.CategoryMapper;
import com.itl.purple_gold.service.CategoryService;
import com.itl.purple_gold.service.DishService;
import com.itl.purple_gold.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService{
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除菜品分类
     * @param id
     */
    @Override
    public void remove(Long id) {

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper= new LambdaQueryWrapper<>();

        //添加查询条件，根据id查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);

        int count1 = dishService.count(dishLambdaQueryWrapper);

        //查询分类是否关联了菜品
        if (count1>0){
            //关联了菜品
            throw new CustomerException("当前分类关联了菜品，不能删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper= new LambdaQueryWrapper<>();

        //添加查询条件，根据id查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);

        int count2 = dishService.count(dishLambdaQueryWrapper);

        //查询分类是否关联了套餐
        if (count2>0){
            //关联了套餐
            throw new CustomerException("当前分类关联了套餐，不能删除");
        }
        //正常删除分类
        super.removeById(id);
    }
}
