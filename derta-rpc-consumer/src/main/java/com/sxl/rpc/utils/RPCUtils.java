package com.sxl.rpc.utils;

import com.sxl.common.core.bean.RpcRequest;
import com.sxl.common.core.util.StringUtil;
import com.sxl.common.register.ServiceDiscovery;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @Author: shenxl
 * @Date: 2019/11/22 16:47
 * @Version 1.0
 * @description：${description}
 */
@Slf4j
public class RPCUtils {

    public static RpcRequest setRequest(Class<?> interfaceClass, String serviceVersion, Method method, Object[] args) {

        RpcRequest request = setRequest(interfaceClass,serviceVersion,args);
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());

        return request;
    }


    public static RpcRequest setRequest(Class<?> interfaceClass,  String serviceVersion,String methodName, Object[] args) {

        RpcRequest request = setRequest(interfaceClass,serviceVersion,args);
        request.setMethodName(methodName);
        Class[] parameterTypes = new Class[args.length];

        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = getClassType(args[i]);
        }
        request.setParameterTypes(parameterTypes);

        return request;
    }

    public static String getServiceIP(ServiceDiscovery serviceDiscovery,String serviceName,String serviceVersion){
        // 获取 RPC 服务地址
        String serviceAddress="";

        if(null!=serviceDiscovery){
            String service=serviceName;
            if (StringUtil.isNotEmpty(serviceVersion)) {
                service += "-" + serviceVersion;
            }
            serviceAddress = serviceDiscovery.discover(service);
            log.info("discover service: {} => {}", serviceName, service);

        }
       return serviceAddress;
    }

    private static Class<?> getClassType(Object obj) {

        Class<?> classType = obj.getClass();

        return classType;
    }


    private static RpcRequest setRequest(Class<?> interfaceClass,  String serviceVersion, Object[] args){

        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setInterfaceName(interfaceClass.getName());
        request.setServiceVersion(serviceVersion);
        request.setParameters(args);

        return request;

    }
}
