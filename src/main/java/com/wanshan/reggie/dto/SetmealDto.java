package com.wanshan.reggie.dto;


import com.wanshan.reggie.pojo.Setmeal;
import com.wanshan.reggie.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
