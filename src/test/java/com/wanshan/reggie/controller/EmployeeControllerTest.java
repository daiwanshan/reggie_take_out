package com.wanshan.reggie.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: Li JinHua
 * @date: 2025/9/22 下午5:31
 */
@SpringBootTest
@Slf4j
class EmployeeControllerTest {
    @Autowired
    private EmployeeController employeeController;

    @Test
    void page() {
        employeeController.page(1,10,null);
    }
}