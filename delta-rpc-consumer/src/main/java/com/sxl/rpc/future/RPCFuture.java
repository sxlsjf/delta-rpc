package com.sxl.rpc.future;

import com.sxl.common.core.bean.RpcRequest;
import com.sxl.common.core.bean.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * RPCFuture for async RPC call
 */
@Slf4j
public class RPCFuture<V> implements Future<V> {

    //同步框架
    private Sync sync;
    private RpcRequest request;
    private RpcResponse response;
    private long startTime;
    private long responseTimeThreshold = 5000;

    private List<AsyncRpcCallback> pendingCallbacks = new ArrayList<>();
    private Lock lock = new ReentrantLock();

    private static final boolean USE_COMMON_POOL =
            (ForkJoinPool.getCommonPoolParallelism() > 1);

    /**
     * Fallback if ForkJoinPool.commonPool() cannot support parallelism
     */
    static final class ThreadPerTaskExecutors implements Executor {
        public void execute(Runnable r) {
            new Thread(r).start();
        }
    }

    /**
     * Default executor -- ForkJoinPool.commonPool() unless it cannot
     * support parallelism.
     */
    private static final Executor ASYNC_POOL = USE_COMMON_POOL ?
            ForkJoinPool.commonPool() : new RPCFuture.ThreadPerTaskExecutors();

    public RPCFuture(RpcRequest request) {
        this.sync = new Sync();
        this.request = request;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    @Override
    public V get() {
        sync.acquire(-1);
        if (Objects.nonNull(this.response) ) {
            return (V) this.response.getResult();
        } else {
            return null;
        }
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException {
        boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
        if (success) {
            if (Objects.nonNull(this.response)) {
                return (V) this.response.getResult();
            } else {
                return null;
            }
        } else {
            throw new RuntimeException("Timeout exception. Request id: " + this.request.getRequestId()
                    + ". Request method: " + this.request.getMethodName());
        }
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    public void done(RpcResponse response) {
        this.response = response;
        sync.release(1);
        invokeCallbacks();
        // Threshold
        long responseTime = System.currentTimeMillis() - startTime;
        if (responseTime > this.responseTimeThreshold) {
            log.warn("Service response time is too slow. Request id = " + response.getRequestId() + ". Response Time = " + responseTime + "ms");
        }
    }

    private void invokeCallbacks() {
        lock.lock();
        try {
            pendingCallbacks.forEach(this::runCallback);

        } finally {
            lock.unlock();
        }
    }

    private RPCFuture addCallBack(AsyncRpcCallback callback) {

        synchronized (this) {
            if (isDone()) {
                runCallback(callback);
            } else {
                this.pendingCallbacks.add(callback);
            }
        }
        return this;
    }

    public RPCFuture success(SuccessAsync callback) {
        addCallBack(callback);
        return this;
    }

    public RPCFuture fail(FailAsync callback) {
        addCallBack(callback);
        return this;
    }

    private void runCallback(final AsyncRpcCallback callback) {
        final RpcResponse res = this.response;
        ASYNC_POOL.execute(() -> {
            if (callback instanceof SuccessAsync) {

                ((SuccessAsync) callback).success(res.getResult());

            } else {
                if(response.hasException()){
                    ((FailAsync) callback).fail(new RuntimeException("Response error", new Throwable(res.getException())));
                }
            }
        });
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
            getState();
            return getState() == done;
        }
    }
}
