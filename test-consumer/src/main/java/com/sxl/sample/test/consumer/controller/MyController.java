package com.sxl.sample.test.consumer.controller;

import com.api.test.service.Myservice;
import com.sxl.rpc.annoation.RpcReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: shenxl
 * @Date: 2019/10/11 13:51
 * @Version 1.0
 * @description：${description}
 */
@RestController
public class MyController {

    @RpcReference(version = "1.0.0")
    private Myservice myservice;

    @RequestMapping("/index")
    public String say(){

        String str=myservice.sayHello("shenxl");
        System.out.println("===================="+str);
        return str;

    }

}
