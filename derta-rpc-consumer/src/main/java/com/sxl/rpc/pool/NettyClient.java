package com.sxl.rpc.pool;


import com.sxl.common.core.bean.RpcRequest;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * 单例RPC请求类 调用端通过此单例进行对提供者端的请求
 * 有些属性需要是全局的例如requestLockMap 所以这里是单例的
 */
@Data
@Slf4j
public class NettyClient {


    //每个ip对应一个连接池
    public final Map<String,ConnectionPool> connectionPoolMap=new ConcurrentHashMap<>(16);

    //全局map 每个请求对应的锁 用于同步等待每个异步的RPC请求
    public final Map<String,RpcRequest> requestLockMap=new ConcurrentHashMap<>();

    private static NettyClient instance;

    //负载均衡获取对应IP 从连接池中获取连接channel
    private Channel connect(String ip) throws Exception {
        String[] IPArr=ip.split(":");
        String host=IPArr[0];
        Integer port=Integer.valueOf(IPArr[1]);
        if (connectionPoolMap.get(ip)==null){
            ConnectionPool connectionPool = new ConnectionPool(host, port);
            connectionPoolMap.putIfAbsent(ip, connectionPool);
        }
        return connectionPoolMap.get(ip).getChannel();
    }

    //单例模式 避免重复连接 构造方法中进行连接操作
    public static NettyClient getInstance(){
        if (instance==null){
            synchronized (NettyClient.class){
                if (instance==null){
                    instance=new NettyClient();
                }
            }
        }
        return instance;
    }

    //向实现端发送请求
    public void send(RpcRequest request,String ip) {

        try {

            Channel channel=connect(ip);
            channel.writeAndFlush(request).sync();

            //挂起等待实现端处理完毕返回
            synchronized (request) {
                //放弃对象锁 并阻塞等待notify
                request.wait();
            }

            connectionPoolMap.get(ip).releaseChannel(channel);
            log.info("调用"+request.getRequestId()+"接收完毕");

        }  catch (Exception e) {
            e.printStackTrace();
        }
    }


}
