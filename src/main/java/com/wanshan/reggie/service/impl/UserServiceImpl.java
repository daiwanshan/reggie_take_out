package com.wanshan.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanshan.reggie.pojo.User;
import com.wanshan.reggie.service.UserService;
import com.wanshan.reggie.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author wan'shan
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2025-10-03 17:51:07
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




