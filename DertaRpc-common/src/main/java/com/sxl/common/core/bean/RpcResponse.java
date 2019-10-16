package com.sxl.common.core.bean;

import lombok.Data;

/**
 * @Author: shenxl
 * @Date: 2019/9/27 14:26
 * @Version 1.0
 * @description：${description}
 */
@Data
public class RpcResponse {

    /**
     *
     * 请求唯一id
     * */
    private String requestId;
    /**
     * 封装错误信息
     * */
    private Exception exception;
    /**
     * 返回结果
     * */
    private Object result;

    private boolean success;

    public boolean hasException() {
        return exception != null;
    }

}
