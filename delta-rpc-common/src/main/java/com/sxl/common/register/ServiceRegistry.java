package com.sxl.common.register;

/**
 * @Author: shenxl
 * @Date: 2019/9/29 15:13
 * @Version 1.0
 * @description：服务注册接口
 */
public interface ServiceRegistry {

    /**
     * 注册服务名称与服务地址
     *
     * @param serviceName 服务名称
     */
    void register(String serviceName, String serviceAddress);
}