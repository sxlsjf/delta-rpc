package org.sxl.rpc;

import com.sxl.common.register.zookeeper.ZooKeeperServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.sxl.rpc.ann.DeltaService;
import org.sxl.rpc.container.LocalHandlerMap;
import org.sxl.rpc.post.ParseRpcServiceAnn;
import org.sxl.rpc.server.RpcServer;

/**
 * @Author: shenxl
 *  * @Date: 2019/9/30 14:32
 *  * @Version 1.0
 *  * @description：${description}
 */

@Configuration
@ConditionalOnClass(DeltaService.class)
@EnableConfigurationProperties(DertaProviderProperties.class)
@ConditionalOnProperty(prefix = "spring.derta", name = "provider", havingValue = "true")
public class DertaProviderAutoConfiguration {


    @Autowired
    private DertaProviderProperties dertaProperties;

    @Bean
    public ZooKeeperServiceRegistry zooKeeperServiceRegistry(){

        return new ZooKeeperServiceRegistry(dertaProperties.getZkAddressRegister());
    }

    @Bean
    public LocalHandlerMap localHandlerMap(){

        return new LocalHandlerMap();
    }

    @Bean
    public ParseRpcServiceAnn parseRpcServiceAnn(LocalHandlerMap localHandlerMap){

        return new ParseRpcServiceAnn(localHandlerMap);
    }




}
