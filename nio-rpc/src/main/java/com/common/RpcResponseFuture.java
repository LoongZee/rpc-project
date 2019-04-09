package com.common;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RpcResponseFuture {

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private Long requstId;

    public RpcResponseFuture(Long requstId) {
        this.requstId = requstId;
    }

    public byte[] get() {
        byte[] bytes = RpcContainer.getResponse(requstId);
        if (bytes == null || bytes.length < 0) {
            lock.lock();
            try {
                System.out.println("请求id:" + requstId + ",请求结果尚未返回，线程挂起");
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
        System.out.println("请求id:" + requstId + ",请求结果返回，线程挂起结束");
        return RpcContainer.getResponse(requstId);
    }

    public void rpcIsDone() {
        lock.lock();
        try {
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    public Long getRequstId() {
        return requstId;
    }

    public void setRequstId(Long requstId) {
        this.requstId = requstId;
    }
}
