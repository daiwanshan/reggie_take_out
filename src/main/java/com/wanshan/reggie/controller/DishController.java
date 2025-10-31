package com.wanshan.reggie.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanshan.reggie.common.CustomException;
import com.wanshan.reggie.common.R;
import com.wanshan.reggie.dto.DishDto;
import com.wanshan.reggie.pojo.Category;
import com.wanshan.reggie.pojo.Dish;
import com.wanshan.reggie.pojo.DishFlavor;
import com.wanshan.reggie.service.CategoryService;
import com.wanshan.reggie.service.DishFlavorService;
import com.wanshan.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: Li JinHua
 * @date: 2025/9/25 下午2:51
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    @Transactional
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);

        // 清理缓存
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<Dish>()
                .like(name != null, Dish::getName, name)
                .orderByAsc(Dish::getSort);
        dishService.page(pageInfo, wrapper);
        BeanUtil.copyProperties(pageInfo, dtoPage, "records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dtoList = BeanUtil.copyToList(records, DishDto.class);
        dtoList.stream().forEach(dishDto -> {
            Long categoryId = dishDto.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
        });

        dtoPage.setRecords(dtoList);
        return R.success(dtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        Dish dis = dishService.getById(id);
        DishDto dishDto = BeanUtil.copyProperties(dis, DishDto.class);
        List<DishFlavor> list = Db.lambdaQuery(DishFlavor.class)
                .eq(DishFlavor::getDishId, id)
                .list();
        dishDto.setFlavors(list);
        return R.success(dishDto);
    }

    @PutMapping
    @Transactional
    public R<String> update(@RequestBody DishDto dishDto) {
        //更新前的数据
        Dish dish = dishService.getById(dishDto.getId());
        dishService.updateWithFlavor(dishDto);

        // 清理缓存
        String key1 = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key1);

        // 清除更新前的分类缓存
        if(dish.getCategoryId() != dishDto.getCategoryId()){
            String key2 = "dish_" + dish.getCategoryId() + "_1";
            redisTemplate.delete(key2);
        }

        return R.success("修改菜品成功");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status, @RequestParam List<Long> ids) {
        LambdaUpdateWrapper<Dish> wrapper = new LambdaUpdateWrapper<Dish>()
                .set(Dish::getStatus, status)
                .in(Dish::getId, ids);
        dishService.update(wrapper);
        return R.success("批量修改成功");
    }

    @DeleteMapping
    @Transactional
    public R<String> delete(@RequestParam List<Long> ids) {
        Long count = Db.lambdaQuery(Dish.class)
                .in(Dish::getId, ids)
                .eq(Dish::getStatus, 1)
                .count();
        if (count > 0) {
            throw new CustomException("当前菜品起售中，不能删除");
        }

        dishService.removeBatchByIds(ids);

        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(wrapper);
        return R.success("批量删除成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        List<DishDto> dishDtos = null;
        String key = "dish_" + dish.getCategoryId() + "_1";

        // 取出缓存数据
        dishDtos = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if (dishDtos != null){
            return R.success(dishDtos);
        }

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<Dish>()
                .eq(Dish::getCategoryId, dish.getCategoryId())
                .eq(Dish::getStatus, 1)
                .orderByAsc(Dish::getSort)
                .orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(wrapper);
        List<DishDto> dtoList = BeanUtil.copyToList(list, DishDto.class);

        dishDtos = dtoList.stream().map(dto -> {
            Category category = Db.lambdaQuery(Category.class)
                    .eq(Category::getId, dto.getCategoryId())
                    .one();
            if (category != null) {
                dto.setCategoryName(category.getName());
            }

            List<DishFlavor> dishFlavors = Db.lambdaQuery(DishFlavor.class)
                    .eq(DishFlavor::getDishId, dto.getId())
                    .list();
            dto.setFlavors(dishFlavors);

            return dto;
        }).collect(Collectors.toList());

        // 缓存数据
        redisTemplate.opsForValue().set(key, dishDtos, 60, TimeUnit.MINUTES);

        return R.success(dishDtos);
    }
}
