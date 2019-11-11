package org.sxl.rpc.ann;

import java.lang.annotation.*;

/**
 * @Author: shenxl
 * @Date: 2019/11/11 19:41
 * @Version 1.0
 * @description：${description}
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DeltaService {

    /**
     * 服务接口类
     */
    Class<?> value() default void.class;

    /**
     * 服务版本号
     */
    String version() default "";

}
