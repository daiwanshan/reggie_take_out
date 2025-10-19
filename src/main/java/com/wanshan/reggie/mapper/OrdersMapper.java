package com.wanshan.reggie.mapper;

import com.wanshan.reggie.pojo.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author wan'shan
* @description 针对表【orders(订单表)】的数据库操作Mapper
* @createDate 2025-10-10 17:39:48
* @Entity com.wanshan.reggie.pojo.Orders
*/
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

}




