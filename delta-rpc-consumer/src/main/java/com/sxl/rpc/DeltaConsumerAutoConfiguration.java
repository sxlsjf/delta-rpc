package com.sxl.rpc;

import com.sxl.common.register.zookeeper.ZooKeeperServiceDiscovery;
import com.sxl.rpc.annoation.RpcReference;
import com.sxl.rpc.factory.RpcClientProxyFactory;
import com.sxl.rpc.post.ParseReferencePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @Author: shenxl
 * @Date: 2019/10/10 17:07
 * @Version 1.0
 * @description：${description}
 */
@Configuration
@ConditionalOnClass(RpcReference.class)
@EnableConfigurationProperties(DeltaConsumerProperties.class)
@ConditionalOnProperty(prefix = "spring.delta", name = "consumer", havingValue = "true")
public class DeltaConsumerAutoConfiguration {

    @Autowired
    private DeltaConsumerProperties deltaConsumerProperties;

    @Bean
    public ZooKeeperServiceDiscovery zooKeeperServiceDiscovery(){

        return new ZooKeeperServiceDiscovery(deltaConsumerProperties.getZkAddressDiscover());
    }

    @Bean
    public RpcClientProxyFactory rpcClientProxyFactory(ZooKeeperServiceDiscovery zooKeeperServiceDiscovery){

        return new RpcClientProxyFactory(zooKeeperServiceDiscovery);
    }

    @Bean
    public ParseReferencePostProcessor parseReferencePostProcessor(RpcClientProxyFactory rpcClientProxyFactory){

        return new ParseReferencePostProcessor(rpcClientProxyFactory);
    }
}
