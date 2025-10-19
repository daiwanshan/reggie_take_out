package com.wanshan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanshan.reggie.common.R;
import com.wanshan.reggie.pojo.Employee;
import com.wanshan.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @author: Li JinHua
 * @date: 2025/9/21 下午12:09
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<Employee>()
                .eq(employee.getUsername() != null,Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(wrapper);

        if (emp == null){
            return R.error("登录失败");
        }

        if(!emp.getPassword().equals( password)){
            return R.error("登录失败");
        }

        if(emp.getStatus() == 0){
            return R.error("账号已禁用");
        }

        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee);

        // 设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        // 获得当前登录用户的id
//        Long empId = (Long) request.getSession().getAttribute("employee");
//
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page( int page,  int pageSize , String name){
        log.info("page = {}, pageSize = {}, name = {}",page,pageSize,name);
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<Employee>()
                .like(name != null, Employee::getName, name)
                .orderByDesc(Employee::getUpdateTime);
        // mybatispuls自动封装到pageInfo对象中
        employeeService.page(pageInfo, wrapper);

        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> updateStatus(HttpServletRequest request, @RequestBody Employee employee){
        log.info("员工信息：{}",employee);

//        employee.setUpdateTime(LocalDateTime.now());
//
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee emp = employeeService.getById(id);
        return R.success( emp);
    }
}
