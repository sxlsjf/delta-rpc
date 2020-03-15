package com.sxl.rpc.client;


import com.sxl.common.core.bean.RpcRequest;
import com.sxl.rpc.future.RPCFuture;
import com.sxl.rpc.pool.ConnectionPool;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
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
    private final Map<String, ConnectionPool> connectionPoolMap=new ConcurrentHashMap<>(16);

    //全局map 每个请求对应的锁 用于同步等待每个异步的RPC请求,暂且用不到
    private final Map<String,RpcRequest> requestLockMap=new ConcurrentHashMap<>();

    //每个请求对应一个Future
    private final Map<String, RPCFuture> pendingRPC = new ConcurrentHashMap<>();

    private static NettyClient instance;

    //负载均衡获取对应IP 从连接池中获取连接channel
    private Channel connect(String ip) throws Exception {
        String[] IPArr=ip.split(":");
        String host=IPArr[0];
        Integer port=Integer.valueOf(IPArr[1]);
        if (Objects.isNull(connectionPoolMap.get(ip))){
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
    public RPCFuture send(RpcRequest request,String ip) {

        RPCFuture rpcFuture = new RPCFuture(request);

        try {

            //从连接池获取链接
            Channel channel=connect(ip);
            //将一个RPCFuture对象放入全局上下文中
            pendingRPC.put(request.getRequestId(), rpcFuture);

            //将请求信息发送出去
            channel.writeAndFlush(request).sync();
            //释放连接到连接池中
            connectionPoolMap.get(ip).releaseChannel(channel);

            log.info("调用"+request.getRequestId()+"接收完毕");

        }  catch (Exception e) {
            e.printStackTrace();
        }

        return rpcFuture;
    }


}
