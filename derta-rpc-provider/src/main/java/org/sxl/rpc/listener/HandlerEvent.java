package org.sxl.rpc.listener;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.sxl.rpc.server.RpcServer;

/**
 * @Author: shenxl
 * @Date: 2019/11/12 10:59
 * @Version 1.0
 * @description：${description}
 */

public class HandlerEvent {


    @EventListener
    public void eventListener(ApplicationStartedEvent event){

        event.getApplicationContext().getBean(RpcServer.class).action();
        System.out.println("监听了-----"+event.getSource());
        System.out.println("监听了-----"+event.getClass());
    }

}
