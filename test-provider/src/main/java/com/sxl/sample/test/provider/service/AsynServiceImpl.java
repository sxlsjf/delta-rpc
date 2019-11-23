package com.sxl.sample.test.provider.service;

import com.api.test.service.AsyncService;
import org.sxl.rpc.ann.DeltaService;

/**
 * @Author: shenxl
 * @Date: 2019/11/22 17:55
 * @Version 1.0
 * @description：
 */


@DeltaService(value = AsyncService.class,version = "1.0.0")
public class AsynServiceImpl implements AsyncService {
    public String asycHello(String say) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("AsynServiceImpl.asycHello()"+say);

        return say;
    }
}
