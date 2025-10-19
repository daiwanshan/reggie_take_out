package com.wanshan.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanshan.reggie.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
* @author wan'shan
* @description 针对表【dish(菜品管理)】的数据库操作Mapper
* @createDate 2025-09-25 14:50:07
* @Entity com.wanshan.reggie.pojo.Dish
*/
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}




