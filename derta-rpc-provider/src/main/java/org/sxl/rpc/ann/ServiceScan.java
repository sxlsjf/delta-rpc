package org.sxl.rpc.ann;

import org.springframework.context.annotation.Import;
import org.sxl.rpc.register.RpcServiceScannerRegistrar;

import java.lang.annotation.*;

/**
 * @Author: shenxl
 * @Date: 2019/11/11 19:38
 * @Version 1.0
 * @description：${description}
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcServiceScannerRegistrar.class)
public @interface ServiceScan {

    String[] basePackage() default {};
}
