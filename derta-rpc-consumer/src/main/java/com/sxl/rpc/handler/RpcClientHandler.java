package com.sxl.rpc.handler;

import com.sxl.common.core.bean.RpcResponse;
import com.sxl.rpc.client.NettyClient;
import com.sxl.rpc.future.RPCFuture;
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

        Map<String,RPCFuture> pendingRPC=NettyClient.getInstance().getPendingRPC();

        String requestId = rpcResponse.getRequestId();
        RPCFuture rpcFuture = pendingRPC.get(requestId);
        if (rpcFuture != null) {
            pendingRPC.remove(requestId);
            rpcFuture.done(rpcResponse);
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
