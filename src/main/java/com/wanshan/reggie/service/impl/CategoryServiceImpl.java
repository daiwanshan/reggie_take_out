package com.wanshan.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanshan.reggie.common.CustomException;
import com.wanshan.reggie.pojo.Category;
import com.wanshan.reggie.pojo.Dish;
import com.wanshan.reggie.pojo.Setmeal;
import com.wanshan.reggie.service.CategoryService;
import com.wanshan.reggie.mapper.CategoryMapper;
import com.wanshan.reggie.service.DishService;
import com.wanshan.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
* @author wan'shan
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
* @createDate 2025-09-23 16:33:13
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) throws Exception {
        // 查询当前分类是否关联了菜品，如果关联，抛出一个业务异常
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<Dish>()
                .eq(Dish::getCategoryId, id);
        int count = (int) dishService.count(wrapper);
        if(count > 0){
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        // 查询当前分类是否关联了套餐，如果关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> wrapper1 = new LambdaQueryWrapper<Setmeal>()
                .eq(Setmeal::getCategoryId, id);
        int count1 = (int) setmealService.count(wrapper1);
        if(count1 > 0 ){
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        //正常删除分类
        removeById( id);
    }
}




