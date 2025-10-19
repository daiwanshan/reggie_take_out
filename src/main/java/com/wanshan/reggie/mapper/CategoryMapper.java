package com.wanshan.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanshan.reggie.pojo.Category;
import org.apache.ibatis.annotations.Mapper;

/**
* @author wan'shan
* @description 针对表【category(菜品及套餐分类)】的数据库操作Mapper
* @createDate 2025-09-23 16:33:13
* @Entity com.wanshan.reggie.pojo.Category
*/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}




