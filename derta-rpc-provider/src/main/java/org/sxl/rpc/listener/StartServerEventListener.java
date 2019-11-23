package org.sxl.rpc.listener;

import com.sxl.common.register.zookeeper.ZooKeeperServiceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.sxl.rpc.DeltaProviderProperties;
import org.sxl.rpc.container.LocalHandlerMap;
import org.sxl.rpc.server.RpcServer;

import java.util.Optional;


/**
 * @Author: shenxl
 * @Date: 2019/11/12 10:59
 * @Version 1.0
 * @description：${description}
 */
@Slf4j
public class StartServerEventListener implements ApplicationListener<ApplicationStartedEvent> {

    private ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {

        log.info("监听器启动");
        applicationContext=event.getApplicationContext();

        LocalHandlerMap localHandlerMap=applicationContext.getBean(LocalHandlerMap.class);

        ZooKeeperServiceRegistry zkRegister=applicationContext.getBean(ZooKeeperServiceRegistry.class);

        //String ip = InetAddress.getLocalHost().getHostAddress();
        String ip = "127.0.0.1";

        Integer port=applicationContext.getBean(DeltaProviderProperties.class).getServerPort();

        String  serviceAddress=ip+":"+port;

        // 注册 RPC 服务地址
        Optional.of(zkRegister).ifPresent((t) ->
                localHandlerMap.getHandlers().keySet().stream().forEach((s) -> {
                    t.register(s, serviceAddress);
                    log.info("注册：register service: {} => {}", s, serviceAddress);
                }));

        //启动rpc
        new RpcServer(localHandlerMap,port).start();
    }

}
