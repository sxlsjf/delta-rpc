package com.sxl.rpc.handler;


import com.sxl.rpc.future.RPCFuture;

/**
 *
 */
public interface IAsyncProxyObject<T> {
     RPCFuture call(String methodName, Object... args);
}