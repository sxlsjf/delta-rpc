package com.sxl.rpc.handler;

import com.sxl.common.core.bean.RpcRequest;
import com.sxl.common.register.ServiceDiscovery;
import com.sxl.rpc.client.NettyClient;
import com.sxl.rpc.future.RPCFuture;
import com.sxl.rpc.utils.RPCUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: shenxl
 * @Date: 2019/11/22 17:32
 * @Version 1.0
 * @description
 */
@Slf4j
public class AsyncProxyObject<T> extends AbstractProxyObject<T> implements IAsyncProxyObject<T> {

    private  final List<String> methods=new ArrayList<>();

    public AsyncProxyObject(final Class<T> interfaceClass, final String serviceVersion, final ServiceDiscovery serviceDiscovery){
        super(interfaceClass,serviceVersion,serviceDiscovery);

        Method[] declaredMethods=interfaceClass.getDeclaredMethods();

        for(Method method:declaredMethods){
            methods.add(method.getName());
        }
    }

    @Override
    public RPCFuture call(String methodName, Object... args) {

        if(methodName==null){
            throw new NullPointerException("method can't be null");
        }

        if(methods.contains(methodName)){

            RpcRequest request= RPCUtils.setRequest(interfaceClass,serviceVersion,methodName,args);

            String ipAddress=RPCUtils.getServiceIP(serviceDiscovery,interfaceClass.getName(),serviceVersion);

            long time = System.currentTimeMillis();
            RPCFuture rpcFuture = NettyClient.getInstance().send(request,ipAddress);
            log.info("异步调用耗时: {}ms", System.currentTimeMillis() - time);
            return rpcFuture;

        }else{
            throw new RuntimeException("method can not find");
        }

    }
}
