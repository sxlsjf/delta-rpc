package com.sxl.common.async;

/**
 * @Author: shenxl
 * @Date: 2019/11/25 15:05
 * @Version 1.0
 * @description：${description}
 */
@FunctionalInterface
public interface ConvertCallBack<T, R> extends AsyncRpcCallback {

    T map(R r);

}
