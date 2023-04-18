package com.itl.purple_gold.dto;

import com.itl.purple_gold.entity.Setmeal;
import com.itl.purple_gold.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
