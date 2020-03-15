package com.sxl.rpc.pool;

import com.sxl.common.core.bean.RpcRequest;
import com.sxl.common.core.bean.RpcResponse;
import com.sxl.common.core.coder.RpcDecoder;
import com.sxl.common.core.coder.RpcEncoder;
import com.sxl.rpc.client.ClientHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * 生产连接的工厂
 */
public class ConnectFactory extends BasePooledObjectFactory<Channel> {

    private String ip;
    private Integer port;

    //启动辅助类 用于配置各种参数
    private Bootstrap bootstrap = new Bootstrap();

    //netty线程组 同一个服务的连接池内各个连接共用
    private EventLoopGroup group = new NioEventLoopGroup();

    public ConnectFactory(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public EventLoopGroup getGroup() {
        return group;
    }

    @Override
    public Channel create() throws Exception {

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new RpcEncoder(RpcRequest.class)); // 编码 RPC 请求
                        pipeline.addLast(new RpcDecoder(RpcResponse.class)); // 解码 RPC 响应
                        pipeline.addLast(new ClientHandler()); // 处理 RPC 响应
                    }
                });

        ChannelFuture f = bootstrap.connect(ip, port).sync();

        System.out.println("pool create channel " + ip + ":" + port);

        return f.channel();
    }

    @Override
    public PooledObject<Channel> wrap(Channel channel) {
        return new DefaultPooledObject<>(channel);
    }

    @Override
    public void destroyObject(PooledObject<Channel> p) {
        System.out.println("destroy channel " + ip + ":" + port);
        //销毁channel时释放资源 http://www.infoq.com/cn/articles/netty-elegant-exit-mechanism-and-principles
        p.getObject().close();
        //关闭链路 而不是关闭所有EventLoop线程和注册在该线程持有的多路复用器上所有的Channel
        p.getObject().eventLoop().shutdownGracefully();
    }

}
