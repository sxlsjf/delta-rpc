package org.sxl.rpc.server;

import com.sxl.common.core.bean.RpcRequest;
import com.sxl.common.core.bean.RpcResponse;
import com.sxl.common.core.util.StringUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import io.netty.channel.ChannelHandlerContext;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.sxl.rpc.container.LocalHandlerMap;

import java.util.Optional;


/**
 * @Author: shenxl
 * @Date: 2019/9/30 14:32
 * @Version 1.0
 * @description：${description}
 */
@Slf4j
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private final LocalHandlerMap localHandlerMap;

    public RpcServerHandler(LocalHandlerMap localHandlerMap){
        this.localHandlerMap= localHandlerMap;
    }

    private String serviceName;
    @Override
    public void channelRead0(final ChannelHandlerContext ctx, RpcRequest request)  {
        // 创建并初始化 RPC 响应对象
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        try {
            Object result = handle(request);
            response.setResult(result);
            response.setSuccess(true);
        } catch (Exception e) {
            log.error("handle result failure", e);
            response.setException(e);
        }
        // 写入 RPC 响应对象并自动关闭连接
        ctx.writeAndFlush(response);
    }

    private Object handle(RpcRequest request) throws Exception {
        // 获取服务对象
        serviceName = request.getInterfaceName();
        String serviceVersion = request.getServiceVersion();
        if (StringUtil.isNotEmpty(serviceVersion)) {
            serviceName += "-" + serviceVersion;
        }
        Object serviceBean = localHandlerMap.getHandlers().get(serviceName);

        Optional.ofNullable(serviceBean).orElseThrow(()->
                new RuntimeException(String.format("can not find service bean by key: %s",serviceName)));

        // 获取反射调用所需的参数
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        // 使用 CGLib 执行反射调用
        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean, parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("server caught exception", cause);
        ctx.close();
    }


























/*
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        RpcResponse response = new RpcResponse();
        try {
            RpcRequest rpcRequest = (RpcRequest) msg;
            log.info("RPC服务端收到消息:" + rpcRequest);

            // 获取本地服务
            // 获取服务对象
            String serviceName = rpcRequest.getInterfaceName();
            String serviceVersion = rpcRequest.getServiceVersion();
            if (StringUtil.isNotEmpty(serviceVersion)) {
                serviceName += "-" + serviceVersion;
            }
            Object serviceBean = localHandlerMap.getHandlers().get(serviceName);
            if (serviceBean == null) {
                throw new RuntimeException(String.format("can not find service bean by key: %s", serviceName));
            }

            // 获取反射调用所需的参数
            Class<?> serviceClass = serviceBean.getClass();
            String methodName = rpcRequest.getMethodName();
            Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
            Object[] parameters = rpcRequest.getParameters();
            // 执行反射调用
            //Method method = serviceClass.getMethod(methodName, parameterTypes);
            //method.setAccessible(true);
            //return method.invoke(serviceBean, parameters);
            // 使用 CGLib 执行反射调用
            FastClass serviceFastClass = FastClass.create(serviceClass);
            FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
            Object result=serviceFastMethod.invoke(serviceClass, parameters);

            response.setResult(result);
            response.setSuccess(true);
        } catch (Exception e) {
            log.error("服务端接收消息发送异常", e);
            response.setSuccess(false);
            response.setException(e);
        }

        // 写响应
        log.info("服务端响应内容:" + response);
        ctx.write(response);
        ctx.flush();
    }

*/


}
