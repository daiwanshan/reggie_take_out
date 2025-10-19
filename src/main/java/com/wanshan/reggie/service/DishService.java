package com.wanshan.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wanshan.reggie.dto.DishDto;
import com.wanshan.reggie.pojo.Dish;

/**
* @author wan'shan
* @description 针对表【dish(菜品管理)】的数据库操作Service
* @createDate 2025-09-25 14:50:07
*/
public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);

    void updateWithFlavor(DishDto dishDto);
}
