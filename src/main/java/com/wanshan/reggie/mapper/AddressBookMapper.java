package com.wanshan.reggie.mapper;

import com.wanshan.reggie.pojo.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author wan'shan
* @description 针对表【address_book(地址管理)】的数据库操作Mapper
* @createDate 2025-10-05 15:11:06
* @Entity com.wanshan.reggie.pojo.AddressBook
*/
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}




