package com.wanshan.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanshan.reggie.pojo.OrderDetail;
import com.wanshan.reggie.service.OrderDetailService;
import com.wanshan.reggie.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author wan'shan
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2025-10-10 17:39:38
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




