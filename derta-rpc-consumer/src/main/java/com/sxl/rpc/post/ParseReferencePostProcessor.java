package com.sxl.rpc.post;

import com.sxl.rpc.annoation.RpcReference;
import com.sxl.rpc.client.RpcClientProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;


import java.lang.reflect.Field;

/**
 * @Author: shenxl
 * @Date: 2019/9/29 15:16
 * @Version 1.0
 * @description：${description}
 */
public class ParseReferencePostProcessor implements BeanPostProcessor {

    private RpcClientProxyFactory factory;

    public ParseReferencePostProcessor(RpcClientProxyFactory factory){
        this.factory = factory;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> objClz;
        if (AopUtils.isAopProxy(bean)) {
            objClz = AopUtils.getTargetClass(bean);
        } else {
            objClz = bean.getClass();
        }
        try {
            for (Field field : objClz.getDeclaredFields()) {
                RpcReference reference = field.getAnnotation(RpcReference.class);
                if (reference != null) {
                    Object objProxy=factory.create(field.getType(),reference.version());
                    field.setAccessible(true);
                    field.set(bean,objProxy);
                }
            }
        } catch (Exception e) {
            throw new BeanCreationException(beanName, e);
        }

        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        return bean;
    }


}
