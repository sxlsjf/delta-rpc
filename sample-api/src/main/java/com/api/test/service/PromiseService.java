package com.api.test.service;

import com.sxl.common.promise.Promise;

/**
 * @Author: shenxl
 * @Date: 2019/11/25 15:58
 * @Version 1.0
 * @description：${description}
 */
public interface PromiseService {

    Promise<String> sayHello(String say);
}
