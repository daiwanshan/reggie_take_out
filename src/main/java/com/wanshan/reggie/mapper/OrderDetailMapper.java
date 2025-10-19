package com.wanshan.reggie.mapper;

import com.wanshan.reggie.pojo.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author wan'shan
* @description 针对表【order_detail(订单明细表)】的数据库操作Mapper
* @createDate 2025-10-10 17:39:38
* @Entity com.wanshan.reggie.pojo.OrderDetail
*/
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}




