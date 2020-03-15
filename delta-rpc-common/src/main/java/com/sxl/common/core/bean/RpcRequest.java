package com.sxl.common.core.bean;
import lombok.Data;


/**
 * @Author: shenxl
 * @Date: 2019/9/27 14:25
 * @Version 1.0
 * @description
 */
@Data
public class RpcRequest {

    /**
     * 请求唯一id
     */
    private String requestId;
    /**
     * 接口名称
     */
    private String interfaceName;
    /**
     * 接口版本
     */
    private String serviceVersion;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * 参数
     */
    private Object[] parameters;




}
