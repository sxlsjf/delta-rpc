package com.sxl.rpc.client;

import com.sxl.common.core.bean.RpcRequest;
import com.sxl.common.core.bean.RpcResponse;
import com.sxl.common.core.util.StringUtil;
import com.sxl.common.register.ServiceDiscovery;
import com.sxl.rpc.pool.RPCRequestNet;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Optional;
import java.util.UUID;

/**
 * @Author: shenxl
 * @Date: 2019/9/29 14:35
 * @Version 1.0
 * @description
 */
@Slf4j
public class RpcClientProxyFactory {


    private ServiceDiscovery serviceDiscovery;

    private String serviceAddress;

    public RpcClientProxyFactory() {
    }

    public RpcClientProxyFactory(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(final Class<?> interfaceClass) {
        return create(interfaceClass, "");
    }

    @SuppressWarnings("unchecked")
    public <T> T create(final Class<?> interfaceClass, final String serviceVersion) {
        // 创建动态代理对象
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                (proxy, method, args) -> {
                    // 创建 RPC 请求对象并设置请求属性
                    RpcRequest request = setRequest(interfaceClass, serviceVersion, method, args);

                    // 获取 RPC 服务地址
                    Optional.ofNullable(serviceDiscovery).ifPresent((t) -> {
                        String serviceName = interfaceClass.getName();
                        if (StringUtil.isNotEmpty(serviceVersion)) {
                            serviceName += "-" + serviceVersion;
                        }
                        serviceAddress = serviceDiscovery.discover(serviceName);
                        log.info("discover service: {} => {}", serviceName, serviceAddress);
                    });

                    if (StringUtil.isEmpty(serviceAddress)) {
                        throw new RuntimeException("server address is empty");
                    }

                    RPCRequestNet.getInstance().getRequestLockMap().put(request.getRequestId(), request);

                    // 创建 RPC 客户端对象并发送 RPC 请求
                    long time = System.currentTimeMillis();
                    RPCRequestNet.getInstance().send(request, serviceAddress);
                    log.info("耗时: {}ms", System.currentTimeMillis() - time);

                  //  Thread.sleep(5000);
                    if (!request.getIsResponse()) {
                        throw new NullPointerException("response is null");
                    }


                    //移除零时存储
                    RPCRequestNet.getInstance().getRequestLockMap().remove(request.getRequestId());

                    // 返回 RPC 响应结果
                    if (request.getResponse().hasException()) {
                        throw request.getResponse().getException();
                    } else {
                        return request.getResponse().getResult();
                    }
                });
    }


    private RpcRequest setRequest(Class<?> interfaceClass, String serviceVersion, Method method, Object[] args) {

        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setInterfaceName(interfaceClass.getName());
        request.setServiceVersion(serviceVersion);
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);

        return request;
    }
}


