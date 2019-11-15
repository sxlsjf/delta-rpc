package com.sxl.rpc.client;

import com.sxl.common.core.bean.RpcRequest;
import com.sxl.common.core.bean.RpcResponse;
import com.sxl.rpc.pool.RPCRequestNet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: shenxl
 * @Date: 2019/11/14 16:32
 * @Version 1.0
 * @description：${description}
 */
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<RpcResponse> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) {

        RpcRequest objectLock= (RpcRequest) RPCRequestNet.getInstance().getRequestLockMap().get(rpcResponse.getRequestId());
        if (objectLock!=null) {

            synchronized (objectLock) {
                //唤醒在该对象锁上wait的线程
                RpcRequest request = (RpcRequest) RPCRequestNet.getInstance().requestLockMap.get(rpcResponse.getRequestId());
                //标记该调用方法是否已返回 未标记但锁释放说明调用超时
                request.setResponse(rpcResponse);
                request.notifyAll();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)  {
        log.error("api caught exception", cause);
        ctx.close();
    }
}
