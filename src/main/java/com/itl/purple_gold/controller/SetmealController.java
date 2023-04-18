package com.itl.purple_gold.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.purple_gold.common.R;
import com.itl.purple_gold.dto.SetmealDto;
import com.itl.purple_gold.entity.Category;
import com.itl.purple_gold.entity.Setmeal;
import com.itl.purple_gold.service.CategoryService;
import com.itl.purple_gold.service.SetmealDishService;
import com.itl.purple_gold.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;


    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){

        log.info("新增套餐，{}",setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("套餐信息分页查询，第{}页,每页{}条，条件：{}",page,pageSize,name);

        //分页构造器
        Page<Setmeal> pageInfo=new Page(page,pageSize);
        Page<SetmealDto> setmealDtoPage=new Page<>();
        //条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper= new LambdaQueryWrapper<>();
        //过滤条件
        queryWrapper.like(!StringUtils.isEmpty(name),Setmeal::getName,name);
        //排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        //查询
        setmealService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");

        List<Setmeal> records=pageInfo.getRecords();

        List<SetmealDto> list=records.stream().map((item)->{
            SetmealDto setmealDto=new SetmealDto();

            //将套餐的属性拷贝到SetmealDto
            BeanUtils.copyProperties(item,setmealDto);

            Long categoryId = item.getCategoryId();//套餐的分类id
            Category category = categoryService.getById(categoryId);//套餐分类对象
            if (category!=null){
                String categoryName = category.getName();
                //添加套餐分类的名称
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);

        return R.success(setmealDtoPage);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("删除套餐，ids:{}",ids);

        setmealService.removeWithDish(ids);

        return R.success("套餐数据删除成功");
    }

    /**
     * 获取多个套餐
     * * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        log.info("setmeal:{}", setmeal);
        //条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(setmeal.getName()), Setmeal::getName, setmeal.getName());
        queryWrapper.eq(null != setmeal.getCategoryId(), Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(null != setmeal.getStatus(), Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        return R.success(setmealService.list(queryWrapper));
    }

    /**
     * 套餐售卖状态修改
     * @param ids
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> changeStatus(@RequestParam List<Long> ids, @PathVariable int status){
        for (Long id : ids) {

            log.info("套餐{}修改售卖状态为{}",id,status);

            Setmeal setmeal=setmealService.getById(id);
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);
        }

        return R.success("套餐售卖状态修改成功");
    }

    /**
     * 根据id查询套餐信息和套餐菜品信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id){

        log.info("查询套餐信息和套餐菜品信息,id:{}",id);
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        return R.success(setmealDto);
    }

    /**
     * 修改套餐信息和菜品信息
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        log.info("修改套餐，{}",setmealDto);

        setmealService.updateWithDish(setmealDto);
        return R.success("修改套餐成功");
    }

}
