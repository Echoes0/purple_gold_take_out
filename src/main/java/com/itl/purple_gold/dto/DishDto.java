package com.itl.purple_gold.dto;

import com.itl.purple_gold.entity.Dish;
import com.itl.purple_gold.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装前端传入的菜品及口味
 */

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
