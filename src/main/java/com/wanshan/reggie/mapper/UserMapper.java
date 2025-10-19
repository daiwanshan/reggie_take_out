package com.wanshan.reggie.mapper;

import com.wanshan.reggie.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author wan'shan
* @description 针对表【user(用户信息)】的数据库操作Mapper
* @createDate 2025-10-03 17:51:07
* @Entity com.wanshan.reggie.pojo.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




