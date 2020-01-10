package com.sxl.rpc.handler;

import com.sxl.common.core.bean.RpcRequest;
import com.sxl.common.register.ServiceDiscovery;
import com.sxl.rpc.client.NettyClient;
import com.sxl.common.promise.Promise;
import com.sxl.rpc.utils.RPCUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author: shenxl
 * @Date: 2019/11/18 15:22
 * @Version 1.0
 * @description
 */
@Slf4j
public class RpcProxyHandler<T> extends AbstractProxyObject<T> implements InvocationHandler {


    public RpcProxyHandler(final Class<T> interfaceClass, final String serviceVersion,
                           final ServiceDiscovery serviceDiscovery){
      super(interfaceClass,serviceVersion,serviceDiscovery);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {

        // 创建 RPC 请求对象并设置请求属性
        RpcRequest request = RPCUtils.buildRequest(interfaceClass, serviceVersion, method, args);

        // 获取 RPC 服务地址
        String ipAddress=RPCUtils.getServiceIP(serviceDiscovery,interfaceClass.getName(),serviceVersion);

        // 创建 RPC 客户端对象并发送 RPC 请求
        long time = System.currentTimeMillis();
        Promise promise=NettyClient.getInstance().sendAsync(request, ipAddress);

        log.info("异步调用耗时: {}ms", System.currentTimeMillis() - time);

        return promise;
    }

}
