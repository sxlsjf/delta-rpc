package com.sxl.common.promise;


import com.sxl.common.async.FailAsync;
import com.sxl.common.async.SuccessAsync;
import com.sxl.common.exception.NoRealizedException;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @Author: shenxl
 * @Date: 2019/11/25 16:54
 * @Version 1.0
 * @description：仅仅用于包装返回值，
 */
public class LightDeferred<R> implements Promise<R> {

    private R result;

    public LightDeferred(R result){
        this.result=result;
    }

    @Override
    public Promise<R> then(Consumer<R> consumer) {
        throw new NoRealizedException();
    }

    @Override
    public Promise<R> onSuccess(SuccessAsync<R> processor) {
        throw new NoRealizedException();
    }

    @Override
    public Promise<R> onFail(FailAsync processor) {
        throw new NoRealizedException();
    }

    /**
     * 用于获取返回结果，其他方法没用，实现等于浪费
     * */
    @Override
    public R get() {
        return result;
    }

    @Override
    public R get(long timeout, TimeUnit unit) {
        throw new NoRealizedException();
    }
}
