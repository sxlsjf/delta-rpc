package com.sxl.rpc.client;

import com.sxl.common.core.bean.RpcRequest;
import com.sxl.common.core.bean.RpcResponse;
import com.sxl.common.core.coder.RpcDecoder;
import com.sxl.common.core.coder.RpcEncoder;
import com.sxl.common.core.util.StringUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @Author: shenxl
 * @Date: 2019/9/29 13:51
 * @Version 1.0
 * @description
 */
@Slf4j
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse>{

    private String serviceAddress;

    private RpcRequest request;

    private RpcResponse response;


    /**
     * @param request
     * @param serviceAddress
     */
    public RpcClient(RpcRequest request, String serviceAddress) {
        this.request = request;
        this.serviceAddress = serviceAddress;
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, RpcResponse response) {
        this.response = response;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)  {
        log.error("api caught exception", cause);
        ctx.close();
    }
    /**
     * 连接远程服务
     */
    public RpcResponse send() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建并初始化 Netty 客户端 Bootstrap 对象
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
            .option(ChannelOption.SO_KEEPALIVE,true)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel)  {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(new RpcEncoder(RpcRequest.class)); // 编码 RPC 请求
                    pipeline.addLast(new RpcDecoder(RpcResponse.class)); // 解码 RPC 响应
                    pipeline.addLast(RpcClient.this); // 处理 RPC 响应
                }
            });

            String[] array = StringUtil.split(serviceAddress, ":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);

            // 连接 RPC 服务器
            ChannelFuture future = bootstrap.connect(host, port).sync();

            // 写入 RPC 请求数据并关闭连接
            Channel channel = future.channel();
            channel.writeAndFlush(request).sync();
            channel.closeFuture().sync();

            log.info("服务端响应：" + response);
            // 返回 RPC 响应对象
            return response;

        } catch (Exception e) {

            response.setException(e);
            response.setSuccess(false);
            return response;

        } finally {
            group.shutdownGracefully();
        }

    }
}