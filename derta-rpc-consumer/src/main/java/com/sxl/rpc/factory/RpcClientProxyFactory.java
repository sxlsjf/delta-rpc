package com.sxl.rpc.factory;

import com.sxl.common.register.ServiceDiscovery;
import com.sxl.rpc.handler.RpcProxyHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;

/**
 * @Author: shenxl
 * @Date: 2019/9/29 14:35
 * @Version 1.0
 * @description
 */
@Slf4j
public class RpcClientProxyFactory {

    private ServiceDiscovery serviceDiscovery;

    public RpcClientProxyFactory() {
    }

    public RpcClientProxyFactory(final ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(final Class<T> interfaceClass, final String serviceVersion) {
        // 创建动态代理对象
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new RpcProxyHandler(interfaceClass,serviceVersion,serviceDiscovery));
    }


}


