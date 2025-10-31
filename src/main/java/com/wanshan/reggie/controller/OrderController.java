package com.wanshan.reggie.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.wanshan.reggie.common.BaseContext;
import com.wanshan.reggie.common.CustomException;
import com.wanshan.reggie.common.R;
import com.wanshan.reggie.dto.OrdersDto;
import com.wanshan.reggie.pojo.AddressBook;
import com.wanshan.reggie.pojo.OrderDetail;
import com.wanshan.reggie.pojo.Orders;
import com.wanshan.reggie.pojo.ShoppingCart;
import com.wanshan.reggie.pojo.User;
import com.wanshan.reggie.service.OrderDetailService;
import com.wanshan.reggie.service.OrdersService;
import com.wanshan.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author: Li JinHua
 * @date: 2025/10/10 下午5:41
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @RequestMapping("/submit")
    @Transactional
    public R<String> submit(@RequestBody Orders orders){
        Long userId = BaseContext.getCurrentId();

        List<ShoppingCart> shoppingCartList = Db.lambdaQuery(ShoppingCart.class)
                .eq(ShoppingCart::getUserId, userId)
                .list();
        if(shoppingCartList == null || shoppingCartList.size() == 0){
            throw new CustomException("购物车为空，不能下单");
        }

        User user = Db.lambdaQuery(User.class)
                .eq(User::getId, userId)
                .one();

        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = Db.lambdaQuery(AddressBook.class)
                .eq(AddressBook::getId, addressBookId)
                .one();
        if(addressBook == null){
            throw new CustomException("地址信息为空，不能下单");
        }

        long orderId = IdWorker.getId();

        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetailList = shoppingCartList.stream().map(item -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            BeanUtil.copyProperties(item, orderDetail, "id");
            amount.getAndAdd(item.getAmount().multiply(BigDecimal.valueOf(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        orders.setStatus(1);
        orders.setOrderTime(LocalDateTime.now());

        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserId(userId);
        orders.setNumber(String.valueOf( orderId));
        orders.setUserName(user.getName());
        orders.setPhone(user.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                        + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                        + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                        + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));

        ordersService.save(orders);

        orderDetailService.saveBatch(orderDetailList);

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, userId);
        shoppingCartService.remove(wrapper);

        return R.success("下单成功");
    }

    @GetMapping ("/userPage")
    public R<Page<OrdersDto>> userPage(int page, int pageSize){
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<Orders>()
                .eq(Orders::getUserId, BaseContext.getCurrentId())
                .orderByDesc(Orders::getOrderTime);
        ordersService.page(pageInfo, wrapper);

        Page<OrdersDto> pageDto = new Page<>();
        BeanUtil.copyProperties(pageInfo, pageDto, "records");

        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> ordersDtos = BeanUtil.copyToList(records, OrdersDto.class);

        List<OrdersDto> dtoList = ordersDtos.stream()
                .map(item -> {
                    List<OrderDetail> orderDetailList = Db.lambdaQuery(OrderDetail.class)
                            .eq(OrderDetail::getOrderId, item.getNumber())
                            .list();
                    item.setOrderDetails(orderDetailList);
                    return item;
                }).collect(Collectors.toList());

        pageDto.setRecords(dtoList);

        return R.success(pageDto);
    }

    @GetMapping ("/page")
    public R<Page<OrdersDto>> page(int page, int pageSize, String number, String beginTime, String endTime){
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<Orders>()
                .eq(number != null, Orders::getNumber, number)
                .between(beginTime != null && endTime != null, Orders::getOrderTime, beginTime, endTime)
                .orderByDesc(Orders::getOrderTime);
        ordersService.page(pageInfo, wrapper);

        Page<OrdersDto> pageDto = new Page<>();
        BeanUtil.copyProperties(pageInfo, pageDto, "records");

        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> ordersDtos = BeanUtil.copyToList(records, OrdersDto.class);

        List<OrdersDto> dtoList = ordersDtos.stream()
                .map(item -> {
                    List<OrderDetail> orderDetailList = Db.lambdaQuery(OrderDetail.class)
                            .eq(OrderDetail::getOrderId, item.getNumber())
                            .list();
                    item.setOrderDetails(orderDetailList);
                    return item;
                }).collect(Collectors.toList());

        pageDto.setRecords(dtoList);

        return R.success(pageDto);
    }
}
