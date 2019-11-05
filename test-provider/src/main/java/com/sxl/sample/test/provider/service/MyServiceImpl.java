package com.sxl.sample.test.provider.service;

import com.api.test.service.Myservice;

import org.sxl.rpc.ann.RpcService;

/**
 * @Author: shenxl
 * @Date: 2019/10/11 9:59
 * @Version 1.0
 * @description：${description}
 */

@RpcService(value = Myservice.class,version = "1.0.0")
public class MyServiceImpl implements Myservice {
    @Override
    public String sayHello(String say) {


        System.out.println("hello"+say+"========================================================================================");

        return "rpc调用成功";

    }
}
