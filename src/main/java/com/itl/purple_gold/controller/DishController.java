package com.itl.purple_gold.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.purple_gold.common.R;
import com.itl.purple_gold.dto.DishDto;
import com.itl.purple_gold.entity.Category;
import com.itl.purple_gold.entity.Dish;
import com.itl.purple_gold.entity.DishFlavor;
import com.itl.purple_gold.entity.Employee;
import com.itl.purple_gold.service.CategoryService;
import com.itl.purple_gold.service.DishFlavorService;
import com.itl.purple_gold.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    DishService dishService;

    @Autowired
    DishFlavorService dishFlavorService;

    @Autowired
    CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info("新增菜品，{}",dishDto);

        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 菜品信息分页查询
     * Page：MybatisPlus提供的返回类型
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("菜品信息分页查询，第{}页,每页{}条，条件：{}",page,pageSize,name);

        //分页构造器
        Page<Dish> pageInfo=new Page(page,pageSize);
        Page<DishDto> dishDtoPage=new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper= new LambdaQueryWrapper<>();
        //过滤条件
        queryWrapper.like(!StringUtils.isEmpty(name),Dish::getName,name);
        //排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //查询
        dishService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records=pageInfo.getRecords();

        List<DishDto> list=records.stream().map((item)->{
            DishDto dishDto=new DishDto();

            //将菜品的属性拷贝到DishDto
            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//菜品的分类id
            Category category = categoryService.getById(categoryId);//菜品分类对象
            if (category!=null){
                String categoryName = category.getName();
                //添加菜品分类的名称
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }

    /**
     * 修改菜品信息和口味信息
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info("修改菜品，{}",dishDto);

        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

//    /**
//     * 根据条件查询菜品
//     * @param dish
//     * @return
//     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish) {
//
//        log.info("菜品查询");
//
//        //条件构造器
//        LambdaQueryWrapper<Dish> queryWrapper= new LambdaQueryWrapper<>();
//
//        //添加条件
//        queryWrapper.eq((dish.getCategoryId())!=null,Dish::getCategoryId,dish.getCategoryId());
//        //起售状态
//        queryWrapper.eq(Dish::getStatus,1);
//
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        //查询
//        List<Dish> list = dishService.list(queryWrapper);
//
//        return R.success(list);
//    }

    /**
     * 根据条件查询菜品
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {

        log.info("菜品查询");

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper= new LambdaQueryWrapper<>();

        //添加条件
        queryWrapper.eq((dish.getCategoryId())!=null,Dish::getCategoryId,dish.getCategoryId());
        //起售状态
        queryWrapper.eq(Dish::getStatus,1);

        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        //查询
        List<Dish> list = dishService.list(queryWrapper);


        List<DishDto> dishDtoList=list.stream().map((item)->{
            DishDto dishDto=new DishDto();

            //将菜品的属性拷贝到DishDto
            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//菜品的分类id
            Category category = categoryService.getById(categoryId);//菜品分类对象
            if (category!=null){
                String categoryName = category.getName();
                //添加菜品分类的名称
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();

            LambdaQueryWrapper<DishFlavor> queryWrapper1=new LambdaQueryWrapper();
            queryWrapper1.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(queryWrapper1);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }


    /**
     * 菜品售卖状态修改
     * @param ids
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> changeStatus(@RequestParam List<Long> ids, @PathVariable int status){
        for (Long id : ids) {

            log.info("菜品{}修改售卖状态为{}",id,status);

            Dish dish=dishService.getById(id);
            dish.setStatus(status);
            dishService.updateById(dish);
        }

        return R.success("菜品售卖状态修改成功");
    }


    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("删除菜品，ids:{}",ids);

        dishService.removeWithFlavor(ids);

        return R.success("菜品数据数据删除成功");
    }
}
