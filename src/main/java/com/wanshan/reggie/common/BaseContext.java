package com.wanshan.reggie.common;

/**
 * @author: Li JinHua
 * @date: 2025/9/23 下午3:54
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
