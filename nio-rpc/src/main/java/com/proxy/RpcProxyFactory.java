package com.proxy;

import java.lang.reflect.Proxy;
import com.niomultipart.RpcNioMultHandler;

public class RpcProxyFactory {

    
    public static <T> T getMultService(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] { interfaceClass },
                new RpcNioMultHandler());
    }
}

