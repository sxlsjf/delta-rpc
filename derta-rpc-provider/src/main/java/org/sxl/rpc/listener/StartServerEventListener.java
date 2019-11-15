package org.sxl.rpc.listener;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.sxl.rpc.server.RpcServer;


/**
 * @Author: shenxl
 * @Date: 2019/11/12 10:59
 * @Version 1.0
 * @description：${description}
 */

public class StartServerEventListener implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {

        System.out.println("监听了-----"+event.getSource());
        System.out.println("监听了-----"+event.getClass());
        event.getApplicationContext().getBean(RpcServer.class).action();
    }

}
