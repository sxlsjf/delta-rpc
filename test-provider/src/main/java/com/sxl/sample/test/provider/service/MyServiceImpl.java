package com.sxl.sample.test.provider.service;

import com.api.test.service.MyService;

import org.sxl.rpc.ann.DeltaService;

/**
 * @Author: shenxl
 * @Date: 2019/10/11 9:59
 * @Version 1.0
 * @description：${description}
 */

@DeltaService(value = MyService.class,version = "1.0.0")
public class MyServiceImpl implements MyService {


    public String sayHello(String say) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("hello"+say+"========================================================================================");

        return "rpc调用成功";

    }
}
