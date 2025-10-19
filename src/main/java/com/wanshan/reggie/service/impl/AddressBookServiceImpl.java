package com.wanshan.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanshan.reggie.pojo.AddressBook;
import com.wanshan.reggie.service.AddressBookService;
import com.wanshan.reggie.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author wan'shan
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2025-10-05 15:11:06
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

}




