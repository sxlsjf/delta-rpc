package org.sxl.rpc.listener;


import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.sxl.rpc.server.RpcServer;


/**
 * @Author: shenxl
 * @Date: 2019/9/25 14:39
 * @Version 1.0
 * @description
 */
public class RefreshInstanceListener  implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {

        applicationStartedEvent.getApplicationContext().getBean(RpcServer.class).action();


    }
}
