package com.sxl.common.exception;

/**
 * @Author: shenxl
 * @Date: 2019/11/25 18:47
 * @Version 1.0
 * @description：${description}
 */
public class NoRealizedException extends RuntimeException {

    public NoRealizedException(){

        super("this method is not implemented");
    }

}
