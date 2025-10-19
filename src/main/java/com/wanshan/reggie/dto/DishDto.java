package com.wanshan.reggie.dto;

import com.wanshan.reggie.pojo.Dish;
import com.wanshan.reggie.pojo.DishFlavor;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
