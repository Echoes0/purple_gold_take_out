package com.itl.purple_gold.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.purple_gold.common.CustomerException;
import com.itl.purple_gold.dto.DishDto;
import com.itl.purple_gold.entity.Dish;
import com.itl.purple_gold.entity.DishFlavor;
import com.itl.purple_gold.mapper.DishMapper;
import com.itl.purple_gold.service.DishFlavorService;
import com.itl.purple_gold.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {

    @Autowired
    DishFlavorService dishFlavorService;
    /**
     * 新增菜品同时添加菜品对应的口味数据
     * @param dishDto
     */
    @Override
    @Transactional //事务控制注解
    public void saveWithFlavor(DishDto dishDto) {

        //保存菜品基本信息到菜品表
        this.save(dishDto);

        //菜品id
        Long dishId = dishDto.getId();

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors=flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表
        dishFlavorService.saveBatch(flavors);//批量保存
    }

    /**
     * 根据id获取菜品信息和口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {

        //查询菜品基本信息
        Dish dish = this.getById(id);

        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询菜品口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(flavors);
        return dishDto;
    }

    /**
     * 更新菜品信息和口味信息
     * @param dishDto
     */
    @Override
    @Transactional //事务控制注解
    public void updateWithFlavor(DishDto dishDto) {

        //更新菜品基本信息
        this.updateById(dishDto);

        //清空口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        //新增口味信息
        //菜品id
        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors=flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表
        dishFlavorService.saveBatch(flavors);//批量保存
    }

    /**
     * 删除菜品，同时需要删除菜品和口味的关联数据
     * @param ids
     */
    @Transactional
    @Override
    public void removeWithFlavor(List<Long> ids) {
        //查询菜品状态，确定是否可用删除
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId,ids);
        queryWrapper.eq(Dish::getStatus,1);

        int count = this.count(queryWrapper);
        if(count > 0){
            //如果不能删除，抛出一个业务异常
            throw new CustomerException("菜品正在售卖中，不能删除");
        }

        //如果可以删除，先删除菜品表中的数据
        this.removeByIds(ids);

        //删除口味相关表的信息
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(DishFlavor::getDishId,ids);
        //删除关系表中的数据----setmeal_dish
        dishFlavorService.remove(lambdaQueryWrapper);
    }
}
