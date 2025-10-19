package com.wanshan.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wanshan.reggie.pojo.Category;

import java.util.List;

/**
* @author wan'shan
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service
* @createDate 2025-09-23 16:33:13
*/
public interface CategoryService extends IService<Category> {

    public void remove(Long id) throws Exception;
}
