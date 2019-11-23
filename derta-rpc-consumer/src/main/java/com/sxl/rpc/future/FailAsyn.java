package com.sxl.rpc.future;

/**
 * @Author: shenxl
 * @Date: 2019/11/22 14:47
 * @Version 1.0
 * @description：${description}
 */
@FunctionalInterface
public interface FailAsyn extends AsyncRpcCallback {

    void fail(Exception e);

}
