package com.rpc;

import com.proxy.RpcProxyFactory;
import com.service.HelloService;

public class RpcNioConsumer {
    public static void main(String[] args) {
        multipartRpcNio();
    }


    public static void multipartRpcNio() {
        HelloService proxy = RpcProxyFactory.getMultService(HelloService.class);
        for (int i = 0; i < 10000; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    String result = proxy.sayHello("world!");
                }
            };
            Thread t = new Thread(runnable);
            t.start();
        }
    }
}