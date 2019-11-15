package org.sxl.rpc.post;

import com.sxl.common.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.sxl.rpc.ann.DeltaService;
import org.sxl.rpc.container.LocalHandlerMap;

/**
 * @Author: shenxl
 * @Date: 2019/10/11 10:41
 * @Version 1.0
 * @description：处理
 */
@Slf4j
public class ParseRpcServiceAnn implements BeanPostProcessor {

    private final LocalHandlerMap localHandlerMap;

    public ParseRpcServiceAnn(LocalHandlerMap localHandlerMap){
        this.localHandlerMap=localHandlerMap;

    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        DeltaService rpcService=bean.getClass().getAnnotation(DeltaService.class);
        if(rpcService!=null){
            String serviceName = rpcService.value().getName();
            String serviceVersion = rpcService.version();
        RpcService rpcService=bean.getClass().getAnnotation(RpcService.class);

        Optional.ofNullable(localHandlerMap).orElseThrow(NullPointerException::new);

        Optional.ofNullable(rpcService).ifPresent((t)->{

            String serviceName = t.value().getName();
            String serviceVersion = t.version();

            if (StringUtil.isNotEmpty(serviceVersion)) {

                serviceName += "-" + serviceVersion;
                localHandlerMap.getHandlers().put(serviceName, bean);
                log.info("服务实例 {} 加入本地缓存...",serviceName);
            }
        });

        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        return bean;
    }

}
