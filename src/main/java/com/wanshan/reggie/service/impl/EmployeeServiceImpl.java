package com.wanshan.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanshan.reggie.pojo.Employee;
import com.wanshan.reggie.service.EmployeeService;
import com.wanshan.reggie.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author wan'shan
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2025-09-21 12:05:29
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService{

}




