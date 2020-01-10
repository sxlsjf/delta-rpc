package com.sxl.common.promise;


import com.sxl.common.async.FailAsync;
import com.sxl.common.async.SuccessAsync;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @Author: shenxl
 * @Date: 2019/11/25 19:13
 * @Version 1.0
 * @description：${description}
 */
public interface Promise<R> {


    Promise<R> then(Consumer<R> consumer);

    Promise<R> onSuccess(SuccessAsync<R> processor);

    Promise<R> onFail(FailAsync processor);

    R get();

    R get(long timeout, TimeUnit unit) throws InterruptedException;


}
