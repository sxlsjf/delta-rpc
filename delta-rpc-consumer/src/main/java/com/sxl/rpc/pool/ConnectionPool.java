package com.sxl.rpc.pool;

import com.sxl.rpc.client.NettyClient;
import com.sxl.rpc.factory.ConnectFactory;
import io.netty.channel.Channel;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * 连接池
 */

public class ConnectionPool {

    private GenericObjectPool<Channel> pool;

    private String fullIp;

    public ConnectionPool(String ip, Integer port) {

        ConnectFactory connectFactory = new ConnectFactory(ip, port);
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        //最大空闲连接数
        config.setMaxIdle(10);
        //最大连接数
        config.setMaxTotal(20);

        pool = new GenericObjectPool(connectFactory, config);
        fullIp = ip + ":" + port;
    }

    public Channel getChannel() throws Exception {
        return pool.borrowObject();
    }

    public void releaseChannel(Channel channel) {
        pool.returnObject(channel);
    }

    public void destroyChannel() {
        //关闭Netty线程资源及其注册的连接
        ((ConnectFactory) pool.getFactory()).getGroup().shutdownGracefully();
        pool.close();
        //移除引用
        NettyClient.getInstance().getConnectionPoolMap().remove(fullIp);
    }

}
