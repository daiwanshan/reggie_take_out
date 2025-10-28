package com.wanshan.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: Li JinHua
 * @date: 2025/9/21 上午10:54
 */

@Slf4j
@SpringBootApplication
@ServletComponentScan  // 扫描filter过滤器
@EnableTransactionManagement // 启动事务管理
@EnableCaching // 启动缓存
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class, args);
        log.info("项目启动成功...");
    }
}
