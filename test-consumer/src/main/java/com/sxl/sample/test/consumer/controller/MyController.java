package com.sxl.sample.test.consumer.controller;

import com.api.test.service.AsynService;
import com.api.test.service.Myservice;
import com.sxl.rpc.annoation.RpcReference;
import com.sxl.rpc.handler.IAsyncProxyObject;
import com.sxl.rpc.future.RPCFuture;
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
    private Myservice myservice;

    @RpcReference(version = "1.0.0",interfaceClass = AsynService.class)
    private IAsyncProxyObject<AsynService> asyncObjectProxy;

    @RequestMapping("/index")
    public String say(){

        String str=myservice.sayHello(" world");
        System.out.println("同步调用当前线程："+Thread.currentThread().getName());
        System.out.println("同步调用结果===================="+str);


        RPCFuture future=asyncObjectProxy.call("asycHello","shenxl");
        future.success((v)->{
            System.out.println("异步调用当前线程："+Thread.currentThread().getName());
            System.out.println("异步调用结果："+v);
        }).fail((e)-> System.out.println("异步调用结果"+e));

        return "success";

    }

}
