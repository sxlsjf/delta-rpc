package com.sxl.rpc.future;

/**
 * @Author: shenxl
 * @Date: 2019/11/22 14:46
 * @Version 1.0
 * @description：${description}
 */
@FunctionalInterface
public interface SuccessAsyn<V> extends AsyncRpcCallback{

    void success(V result);
}
