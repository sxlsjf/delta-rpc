package com.sxl.rpc.handler;

import com.sxl.common.register.ServiceDiscovery;

import java.util.Optional;

/**
 * @Author: shenxl
 * @Date: 2019/11/23 20:34
 * @Version 1.0
 * @description：${description}
 */
abstract class AbstractProxyObject<T>{

    protected Class<T> interfaceClass;
    protected String serviceVersion;
    protected ServiceDiscovery serviceDiscovery;


    protected AbstractProxyObject(final Class<T> interfaceClass, final String serviceVersion, final ServiceDiscovery serviceDiscovery){

        this.interfaceClass=interfaceClass;
        this.serviceVersion=serviceVersion;
        this.serviceDiscovery=serviceDiscovery;
        Optional.ofNullable(interfaceClass).orElseThrow(NullPointerException::new);

    }
}
