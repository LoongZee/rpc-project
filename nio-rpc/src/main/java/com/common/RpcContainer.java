package com.common;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class RpcContainer {
    //返回值容器
    private static ConcurrentHashMap<Long, byte[]> responseContainer = new ConcurrentHashMap<>();
    //请求对象容器
    private static ConcurrentHashMap<Long, RpcResponseFuture> requestFuture = new ConcurrentHashMap<>();
    //请求id
    private volatile static AtomicLong requstId = new AtomicLong(0);

    public static Long getRequestId() {
        return requstId.getAndIncrement();
    }

    public static void addResponse(Long requestId, byte[] responseBytes) {
        responseContainer.put(requestId, responseBytes);
        RpcResponseFuture responseFuture = requestFuture.get(requestId);
        responseFuture.rpcIsDone();
    }

    public static byte[] getResponse(Long requestId) {
        return responseContainer.get(requestId);
    }

    public static void addRequstFuture(RpcResponseFuture rpcResponseFuture) {
        requestFuture.put(rpcResponseFuture.getRequstId(), rpcResponseFuture);
    }

    public static RpcResponseFuture getRpcRequstFutue(Long requestId) {
        return requestFuture.get(requestId);
    }

    public static void removeResponseAndFuture(Long requestId) {
        responseContainer.remove(requestId);
        requestFuture.remove(requestId);
    }
}


