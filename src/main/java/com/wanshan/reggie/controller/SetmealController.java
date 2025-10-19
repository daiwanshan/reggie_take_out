package com.wanshan.reggie.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.wanshan.reggie.common.CustomException;
import com.wanshan.reggie.common.R;
import com.wanshan.reggie.dto.SetmealDto;
import com.wanshan.reggie.pojo.Category;
import com.wanshan.reggie.pojo.Setmeal;
import com.wanshan.reggie.pojo.SetmealDish;
import com.wanshan.reggie.service.SetmealDishService;
import com.wanshan.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author: Li JinHua
 * @date: 2025/9/25 下午2:52
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;

    @PostMapping
    @Transactional
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        Setmeal setmeal = BeanUtil.copyProperties(setmealDto, Setmeal.class);
        setmealService.save(setmeal);

        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes().stream().map(item -> {
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
        return R.success("新增套餐成功");
    }

    @GetMapping("/page")
    public R<Page<SetmealDto>> page(Integer page, Integer pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<Setmeal>()
                .like(name != null, Setmeal::getName, name)
                .orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, wrapper);
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        List<Setmeal> setmealList = pageInfo.getRecords();
        List<SetmealDto> setmealDtoList = BeanUtil.copyToList(setmealList, SetmealDto.class).stream()
                .map(item -> {
                    Category category = Db.lambdaQuery(Category.class)
                            .eq(Category::getId, item.getCategoryId())
                            .one();
                    item.setCategoryName(category.getName());
                    return item;
                }).collect(Collectors.toList());

        dtoPage.setRecords(setmealDtoList);
        return R.success(dtoPage);
    }

    @DeleteMapping
    @Transactional
    public R<String> delete(@RequestParam List<Long> ids) {
        Long count = Db.lambdaQuery(Setmeal.class)
                .in(Setmeal::getId, ids)
                .eq(Setmeal::getStatus, 1)
                .count();
        if (count > 0) {
            throw new CustomException("当前套餐起售中，不能删除");
        }
        setmealService.removeBatchByIds(ids);

        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<SetmealDish>()
                .in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(wrapper);
        return R.success("批量删除套餐成功");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id) {
        Setmeal setmeal = setmealService.getById(id);
        SetmealDto setmealDto = BeanUtil.copyProperties(setmeal, SetmealDto.class);

        List<SetmealDish> setmealDishList = Db.lambdaQuery(SetmealDish.class)
                .eq(SetmealDish::getSetmealId, id)
                .list();
        setmealDto.setSetmealDishes(setmealDishList);
        return R.success(setmealDto);
    }

    @PutMapping
    @Transactional
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        setmealService.updateById(setmealDto);

        Long id = setmealDto.getId();
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<SetmealDish>()
                .eq(SetmealDish::getSetmealId, id);
        setmealDishService.remove(wrapper);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes().stream()
                .map(item -> {
                    item.setSetmealId(id);
                    return item;
                }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
        return R.success("修改套餐成功");
    }

    @PostMapping("/status/{status}")
    public R<String> startOrStop(@PathVariable Integer status, @RequestParam List<Long> ids) {
        Db.lambdaUpdate(Setmeal.class)
                .set(Setmeal::getStatus, status)
                .in(Setmeal::getId, ids)
                .orderByDesc(Setmeal::getUpdateTime)
                .update();
        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<SetmealDto>> list(Setmeal setmeal){
        List<Setmeal> setmealList = Db.lambdaQuery(Setmeal.class)
                .eq(Setmeal::getCategoryId, setmeal.getCategoryId())
                .eq(Setmeal::getStatus, 1)
                .orderByDesc(Setmeal::getUpdateTime)
                .list();

        List<SetmealDto> setmealDtos = BeanUtil.copyToList(setmealList, SetmealDto.class);
        List<SetmealDto> setmealDtoList = setmealDtos.stream().map(item -> {
            Category category = Db.lambdaQuery(Category.class)
                    .eq(Category::getId, item.getCategoryId())
                    .one();
            if (category != null) {
                item.setCategoryName(category.getName());
            }

            List<SetmealDish> setmealDishList = Db.lambdaQuery(SetmealDish.class)
                    .eq(SetmealDish::getSetmealId, item.getId())
                    .list();
            item.setSetmealDishes(setmealDishList);
            return item;
        }).collect(Collectors.toList());


        return R.success(setmealDtoList);
    }
}
