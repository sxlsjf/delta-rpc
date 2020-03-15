package com.sxl.common.promise;


import com.sxl.common.async.FailAsync;
import com.sxl.common.async.SuccessAsync;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.function.Consumer;

/**
 * @Author: shenxl
 * @Date: 2019/11/25 19:22
 * @Version 1.0
 * @description：异步调用真正返回的promise对象
 */
public class Deferred<R> implements Promise<R> {


    Queue<Consumer<R>> thenCallBackList = new LinkedBlockingQueue<>();

    private R result;

    private SuccessAsync<R> successAsync;
    private FailAsync failAsync;
    private Sync sync;

    private Exception ex;

    public Deferred() {
        this.sync = new Deferred.Sync();
    }

    @Deprecated
    public Deferred(R result) {
        this.sync = new Deferred.Sync();
        this.result = result;
        sync.tryRelease(1);
    }

    @Override
    public Promise<R> then(Consumer<R> consumer) {
        //判断是已经返回结果，返回结果了，直接执行回调函数
        if(isDone()){
            consumer.accept(result);
        }
        //不执行具体操作 只返回promise并保存其回调 具体操作在回调完成时执行
        thenCallBackList.offer(consumer);
        return this;
    }

    @Override
    public Promise<R> onSuccess(SuccessAsync<R> successAsync) {

        if(isDone()){
            successAsync.success(result);
        }
        this.successAsync = successAsync;
        return this;
    }

    @Override
    public Promise<R> onFail(FailAsync failAsync) {
        if(isDone()){
            failAsync.fail(ex);
        }
        //不执行具体操作 只返回promise并保存其回调 具体操作在回调完成时执行
        this.failAsync = failAsync;
        return this;
    }


    public boolean isDone() {
        return sync.isDone();
    }

    @Override
    public R get() {
        sync.acquire(-1);
        if (this.result != null) {
            return this.result;
        } else {
            return null;
        }
    }

    @Override
    public R get(long timeout, TimeUnit unit) throws InterruptedException {
        boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
        if (success) {
            if (this.result != null) {
                return this.result;
            } else {
                return null;
            }
        } else {
            throw new RuntimeException("Timeout exception. Request id: ");
        }
    }


    public void resolve(Object result) {
        this.result = (R) result;
        sync.release(1);

        //从队列中循环消费
        boolean loop = true;
        while (loop) {
            if (thenCallBackList.isEmpty()) {
               break;
            }
            Consumer<R> callBack = thenCallBackList.poll();
            callBack.accept((R) result);
        }

        Optional.ofNullable(successAsync).ifPresent(f -> f.success((R) result));

    }

    public void reject(Exception ex) {
        this.ex=ex;
        sync.release(1);
        Optional.ofNullable(failAsync).ifPresent(f -> f.fail(ex));
    }

    public Promise promise() {
        return this;
    }


    static class Sync extends AbstractQueuedSynchronizer {

        private static final long serialVersionUID = 1L;

        //future status
        private final int done = 1;
        private final int pending = 0;

        @Override
        protected boolean tryAcquire(int arg) {
            return getState() == done;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == pending) {
                if (compareAndSetState(pending, done)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }

        boolean isDone() {
            return getState() == done;
        }
    }
}
