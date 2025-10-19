package com.wanshan.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanshan.reggie.pojo.Orders;
import com.wanshan.reggie.service.OrdersService;
import com.wanshan.reggie.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

/**
* @author wan'shan
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2025-10-10 17:39:48
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

}




