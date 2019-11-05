package org.sxl.rpc.listener;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.sxl.rpc.server.RpcServer;


/**
 * @Author: shenxl
 * @Date: 2019/9/25 14:39
 * @Version 1.0
 * @description spring容器初始化完毕，开启rpc服务器
 */
@Slf4j
public class RpcServerActionListener implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {

        log.info("发布开启服务的事件");
        applicationStartedEvent.getApplicationContext().getBean(RpcServer.class).action();

    }
}
