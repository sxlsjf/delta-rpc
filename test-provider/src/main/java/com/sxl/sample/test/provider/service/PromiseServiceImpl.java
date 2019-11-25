package com.sxl.sample.test.provider.service;

import com.api.test.service.PromiseService;
import com.sxl.common.promise.LightDeferred;
import com.sxl.common.promise.Promise;
import org.sxl.rpc.ann.DeltaService;

/**
 * @Author: shenxl
 * @Date: 2019/11/25 16:00
 * @Version 1.0
 * @description：${description}
 */
@DeltaService(value = PromiseService.class,version = "1.0.0")
public class PromiseServiceImpl implements PromiseService {


    public Promise<String> sayHello(String say) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("打印消费端传过来的参数："+say);

        return new LightDeferred<String>(say);
    }
}
