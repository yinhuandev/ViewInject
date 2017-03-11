package com.yinhuan.viewinject.view;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by yinhuan on 2017/3/11.
 */

public class DynamicHandler implements InvocationHandler {

    private WeakReference<Object> handlerReference;

    private final HashMap<String, Method> methodMap = new HashMap<String, Method>(1);

    public DynamicHandler(Object handler){

        this.handlerReference = new WeakReference<Object>(handler);
    }

    public void addMethod(String name, Method method){
        methodMap.put(name, method);
    }

    public Object getHandler(){
        return handlerReference.get();
    }

    public void setHandler(Object handler){
        this.handlerReference = new WeakReference<Object>(handler);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //handler -> activity
        Object handler = handlerReference.get();
        if (handler != null){
            String methodName = method.getName();
            method = methodMap.get(methodName);
            if (method != null){
                //回调 @OnClick 注解的方法，比如 onClick()
                return method.invoke(handler,args);
            }
        }
        return null;
    }
}
