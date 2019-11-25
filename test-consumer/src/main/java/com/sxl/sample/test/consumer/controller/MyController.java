package com.sxl.sample.test.consumer.controller;

import com.api.test.service.PromiseService;
import com.sxl.rpc.annoation.RpcReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: shenxl
 * @Date: 2019/10/11 13:51
 * @Version 1.0
 * @description：
 */
@RestController
public class MyController {


    @RpcReference(version = "1.0.0")
    private PromiseService promiseService;


    @RequestMapping("/index")
    public String say(){


        System.out.println("同步调用当前线程："+Thread.currentThread().getName());

        promiseService.sayHello("i love you").then(System.out::println)
                .then(t-> System.out.println("result:"+t))
                .then(t-> System.out.println("reslut2:"+t))
                .onSuccess(t-> System.out.println("成功执行"));

        return "success";

    }

}
