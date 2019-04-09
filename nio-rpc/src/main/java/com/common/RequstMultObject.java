package com.common;


import java.io.Serializable;

public class RequstMultObject implements Serializable {

    private static final long serialVersionUID = 3132836600205356306L;

    // 请求id
    private Long requestId;

    // 服务提供者接口
    private Class<?> calzz;

    // 服务的方法名称
    private String methodName;

    // 参数类型
    private Class<?>[] paramTypes;

    // 参数
    private Object[] args;

    public RequstMultObject(Class<?> calzz, String methodName, Class<?>[] paramTypes, Object[] args) {
        this.calzz = calzz;
        this.methodName = methodName;
        this.paramTypes = paramTypes;
        this.args = args;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requstId) {
        this.requestId = requstId;
    }

    public Class<?> getCalzz() {
        return calzz;
    }

    public void setCalzz(Class<?> calzz) {
        this.calzz = calzz;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}

