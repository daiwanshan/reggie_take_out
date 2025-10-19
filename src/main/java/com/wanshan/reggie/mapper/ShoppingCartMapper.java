package com.wanshan.reggie.mapper;

import com.wanshan.reggie.pojo.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author wan'shan
* @description 针对表【shopping_cart(购物车)】的数据库操作Mapper
* @createDate 2025-10-09 19:20:59
* @Entity com.wanshan.reggie.pojo.ShoppingCart
*/
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

}




