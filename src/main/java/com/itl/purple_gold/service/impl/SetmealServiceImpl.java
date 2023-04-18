package com.itl.purple_gold.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.purple_gold.dto.DishDto;
import com.itl.purple_gold.dto.SetmealDto;
import com.itl.purple_gold.entity.Dish;
import com.itl.purple_gold.entity.DishFlavor;
import com.itl.purple_gold.entity.Setmeal;
import com.itl.purple_gold.entity.SetmealDish;
import com.itl.purple_gold.mapper.SetmealMapper;
import com.itl.purple_gold.service.SetmealDishService;
import com.itl.purple_gold.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itl.purple_gold.common.CustomerException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 根据id获取套餐信息和菜品信息
     * @param id
     * @return
     */
    @Override
    public SetmealDto getByIdWithDish(Long id) {
        //查询套餐基本信息
        Setmeal setmeal = this.getById(id);

        SetmealDto setmealDto=new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);

        //查询套餐菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> dishes =setmealDishService.list(queryWrapper);

        setmealDto.setSetmealDishes(dishes);
        return setmealDto;
    }

    /**
     * 新增套餐，及套餐的菜品
     * @param setmealDto
     */
    @Transactional//事务控制
    @Override
    public void saveWithDish(SetmealDto setmealDto) {

        //保存套餐基本信息
        this.save(setmealDto);

        //保存套餐的菜品信息
        Long setmealId = setmealDto.getId();
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();

        dishes=dishes.stream().map((item)->{
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表
        setmealDishService.saveBatch(dishes);//批量保存

    }

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where id in (1,2,3) and status = 1
        //查询套餐状态，确定是否可用删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);
        if(count > 0){
            //如果不能删除，抛出一个业务异常
            throw new CustomerException("套餐正在售卖中，不能删除");
        }

        //如果可以删除，先删除套餐表中的数据---setmeal
        this.removeByIds(ids);

        //delete from setmeal_dish where setmeal_id in (1,2,3)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        //删除关系表中的数据----setmeal_dish
        setmealDishService.remove(lambdaQueryWrapper);
    }

    /**
     * 更新套餐信息和菜品信息
     * @param setmealDto
     */
    @Transactional //事务控制注解
    public void updateWithDish(SetmealDto setmealDto) {

        //更新套餐基本信息
        this.updateById(setmealDto);

        //清空菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());

        setmealDishService.remove(queryWrapper);

        //新增菜品信息
        //套餐id
        Long setmealId = setmealDto.getId();

        List<SetmealDish> dishes= setmealDto.getSetmealDishes();

        dishes=dishes.stream().map((item)->{
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());

        //保存套餐菜品数据到套餐菜品表
        setmealDishService.saveBatch(dishes);//批量保存
    }
}
