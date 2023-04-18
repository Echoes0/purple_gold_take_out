package com.itl.purple_gold.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.purple_gold.common.R;
import com.itl.purple_gold.entity.Category;
import com.itl.purple_gold.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> login( @RequestBody Category category ) {

        log.info("新增分类成功：{}",category);

        categoryService.save(category);
        return R.success("新增分类成功");
    }


    /**
     * 菜品分类信息分页查询
     * Page：MybatisPlus提供的返回类型
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        log.info("菜品分类分页查询，第{}页,每页{}条",page,pageSize);

        //分页构造器
        Page<Category> pageInfo=new Page<>(page,pageSize);

        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper= new LambdaQueryWrapper<>();

        //排序条件
        queryWrapper.orderByAsc(Category::getSort);

        //查询
        categoryService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long id ) {

        log.info("删除菜品分类，id：{}",id);

        categoryService.remove(id);
        return R.success("分类信息删除成功");
    }

    /**
     * 修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> delete(@RequestBody Category category ) {

        log.info("修改菜品分类信息，{}",category);

        categoryService.updateById(category);
        return R.success("分类信息修改成功");
    }


    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category ) {

        log.info("菜品分类查询");

        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper= new LambdaQueryWrapper<>();

        //添加条件
        queryWrapper.eq((category.getType())!=null,Category::getType,category.getType());

        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        //查询
        List<Category> list = categoryService.list(queryWrapper);

        return R.success(list);
    }


}
