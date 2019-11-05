package com.sxl.rpc.post;

import com.sxl.rpc.annoation.RpcReference;
import com.sxl.rpc.client.RpcClientProxyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;


import java.lang.reflect.Field;
import java.util.Optional;

/**
 * @Author: shenxl
 * @Date: 2019/9/29 15:16
 * @Version 1.0
 * @description bean后置处理器，处理处理bean中带RpcReference的字段
 */
@Slf4j
public class ParseReferencePostProcessor implements BeanPostProcessor {

    private RpcClientProxyFactory factory;

    public ParseReferencePostProcessor(RpcClientProxyFactory factory) {
        this.factory = factory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        Class<?> objClz;
        if (AopUtils.isAopProxy(bean)) {
            objClz = AopUtils.getTargetClass(bean);
        } else {
            objClz = bean.getClass();
        }

        for (Field field : objClz.getDeclaredFields()) {

            RpcReference reference = field.getAnnotation(RpcReference.class);

            Optional.ofNullable(reference).ifPresent((t) -> {
                Object objProxy = factory.create(field.getType(), reference.version());
                field.setAccessible(true);
                try {
                    field.set(bean, objProxy);
                } catch (IllegalAccessException e) {
                    log.error("赋值出错", e);
                }
            });
        }


        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        return bean;
    }


}
