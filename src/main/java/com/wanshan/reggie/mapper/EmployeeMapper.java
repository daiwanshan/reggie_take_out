package com.wanshan.reggie.mapper;

import com.wanshan.reggie.pojo.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author wan'shan
* @description 针对表【employee(员工信息)】的数据库操作Mapper
* @createDate 2025-09-21 12:05:29
* @Entity com.wanshan.reggie.pojo.Employee
*/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}




