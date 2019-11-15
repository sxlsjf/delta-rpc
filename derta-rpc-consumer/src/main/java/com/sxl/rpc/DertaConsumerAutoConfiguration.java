package com.sxl.rpc;

import com.sxl.common.register.zookeeper.ZooKeeperServiceDiscovery;
import com.sxl.rpc.annoation.RpcReference;
import com.sxl.rpc.client.RpcClientProxyFactory;
import com.sxl.rpc.post.ParseReferencePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
@EnableConfigurationProperties(DertaConsumerProperies.class)
@ConditionalOnProperty(prefix = "spring.derta", name = "consumer", havingValue = "true")
public class DertaConsumerAutoConfiguration {

    @Autowired
    private DertaConsumerProperies dertaConsumerProperies;

    @Bean
    public ZooKeeperServiceDiscovery zooKeeperServiceDiscovery(){

        return new ZooKeeperServiceDiscovery(dertaConsumerProperies.getZkAddressDiscover());
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
