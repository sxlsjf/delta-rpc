package com.sxl.rpc.handler;

import com.sxl.common.core.bean.RpcResponse;
import com.sxl.common.promise.Deferred;
import com.sxl.rpc.client.NettyClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Author: shenxl
 * @Date: 2019/11/14 16:32
 * @Version 1.0
 * @description：${description}
 */
@Slf4j
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) {

        Map<String, Deferred> promiseMap=NettyClient.getInstance().getPromiseMap();

        String requestId = rpcResponse.getRequestId();

        //这个promise才是真正的异步promise
        Deferred deferred=promiseMap.get(requestId);

        //从返回的promise中拿到结果，返回的promise是同步的，仅仅是充当包装返回结果的实体
        Object reallyResult= rpcResponse.getResult().get();

        if (deferred!= null) {

            if(rpcResponse.isSuccess()){
                deferred.resolve(reallyResult);
            }else {
                deferred.reject(rpcResponse.getException());
            }

            promiseMap.remove(requestId);
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)  {
        log.error("api caught exception", cause);
        ctx.close();
    }
}
