package com.wanshan.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanshan.reggie.dto.DishDto;
import com.wanshan.reggie.pojo.Dish;
import com.wanshan.reggie.pojo.DishFlavor;
import com.wanshan.reggie.service.DishFlavorService;
import com.wanshan.reggie.service.DishService;
import com.wanshan.reggie.mapper.DishMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author wan'shan
* @description 针对表【dish(菜品管理)】的数据库操作Service实现
* @createDate 2025-09-25 14:50:07
*/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService{

    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品基本信息
        save(dishDto);
        Long dishId = dishDto.getId();

        // 保存菜品口味信息
        List<DishFlavor> flavors = dishDto.getFlavors().stream()
                .map(item -> {
                    item.setDishId(dishId);
                    return item;
                }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<DishFlavor>()
                .eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(wrapper);

        List<DishFlavor> flavors = dishDto.getFlavors().stream()
                .map(dishFlavor -> {
                    dishFlavor.setDishId(dishDto.getId());
                    return dishFlavor;
                }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}




