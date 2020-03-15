package org.sxl.rpc.ann;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: shenxl
 * @Date: 2019/9/30 10:27
 * @Version 1.0
 * @description：RPC 服务注解（标注在服务实现类上），Component 是为了让 spring 扫描到
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
@Deprecated
public @interface RpcService {

    /**
     * 服务接口类
     */
    Class<?> value() default void.class;

    /**
     * 服务版本号
     */
    String version() default "";

    String group()   default "";

    int timeout() default 5000;
}
