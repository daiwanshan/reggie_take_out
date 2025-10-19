package com.wanshan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.wanshan.reggie.common.CustomException;
import com.wanshan.reggie.common.R;
import com.wanshan.reggie.dto.DishDto;
import com.wanshan.reggie.pojo.Category;
import com.wanshan.reggie.pojo.Dish;
import com.wanshan.reggie.pojo.Setmeal;
import com.wanshan.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Li JinHua
 * @date: 2025/9/23 下午4:34
 */

/**
 * 分类管理
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     *
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("category:{}", category);
        categoryService.save(category);
        return R.success("新增菜品分类成功");
    }

    /**
     * 分类信息分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        log.info("page = {}, pageSize = {}", page, pageSize);
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<Category>()
                .orderByAsc(Category::getSort);
        Page<Category> categoryPage = categoryService.page(pageInfo, wrapper);
        log.info("pageInfo:{}", pageInfo);
        return R.success(pageInfo);
    }

    /**
     * 根据id删除分类
     *
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids) throws Exception {
        log.info("删除分类，id为：{}", ids);
        Category category = categoryService.getById(ids);
        if (category.getType() == 1) {
            Long count = Db.lambdaQuery(Dish.class)
                    .eq(Dish::getCategoryId, category.getId())
                    .count();
            if (count > 0) {
                throw new CustomException("当前分类下关联了菜品，不能删除");
            }
        } else if (category.getType() == 2) {
            Long count = Db.lambdaQuery(Setmeal.class)
                    .eq(Setmeal::getCategoryId, category.getId())
                    .count();
            if (count > 0) {
                throw new CustomException("当前分类下关联了套餐，不能删除");
            }
        }

        categoryService.remove(ids);
        return R.success("分类信息删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("修改分类信息：{}", category);
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<Category>()
                .eq(category.getType() != null,Category::getType, category.getType())
                .orderByAsc(Category::getSort)
                .orderByDesc(Category::getUpdateTime);
        List<Category> categoryList = categoryService.list(wrapper);
        return R.success(categoryList);
    }
}
