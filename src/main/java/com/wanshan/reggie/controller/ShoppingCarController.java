package com.wanshan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.wanshan.reggie.common.BaseContext;
import com.wanshan.reggie.common.R;
import com.wanshan.reggie.mapper.ShoppingCartMapper;
import com.wanshan.reggie.pojo.Dish;
import com.wanshan.reggie.pojo.ShoppingCart;
import com.wanshan.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: Li JinHua
 * @date: 2025/10/9 下午7:23
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCarController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, currentId);

        if(shoppingCart.getDishId() != null){
            wrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        }else {
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart dishOrSetmeal = shoppingCartService.getOne(wrapper);

        if(dishOrSetmeal != null){
            int count = dishOrSetmeal.getNumber();
            dishOrSetmeal.setNumber(count + 1);
            shoppingCartService.updateById(dishOrSetmeal);
        }else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            dishOrSetmeal = shoppingCart;
        }

        return R.success(dishOrSetmeal);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        List<ShoppingCart> list = Db.lambdaQuery(ShoppingCart.class)
                .eq(ShoppingCart::getUserId, BaseContext.getCurrentId())
                .orderByDesc(ShoppingCart::getCreateTime)
                .list();

        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, currentId);
        shoppingCartService.remove(wrapper);
        return R.success("清空购物车成功");
    }

    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart){
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        if(shoppingCart.getDishId() != null){
            wrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        }else {
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart dishOrSetmeal = shoppingCartService.getOne(wrapper);
        Integer count = dishOrSetmeal.getNumber() - 1;
        if(count == 0){
            shoppingCartService.removeById(dishOrSetmeal);
            R.success("删除成功");
        }else {
            dishOrSetmeal.setNumber(count);
            shoppingCartService.updateById(dishOrSetmeal);
        }

        return R.success("删除成功");
    }
}
