package com.yinhuan.viewinject.view;

import android.app.Activity;
import android.view.View;

import com.yinhuan.viewinject.view.annotation.BindView;
import com.yinhuan.viewinject.view.annotation.ContentView;
import com.yinhuan.viewinject.view.annotation.EventBase;
import com.yinhuan.viewinject.view.annotation.OnClick;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by yinhuan on 2017/3/10.
 */

public class ViewInject {

    private static final String METHOD_SET_CONTENT_VIEW = "setContentView";
    private static final String METHOD_FIND_VIEW_BY_ID = "findViewById";


    /**
     * 注入接口
     *
     * @param activity
     */
    public static void inject(Activity activity) {
        injectContentView(activity);
        injectViews(activity);
        injectEvent(activity);
    }


    /**
     * 注入 ContentView
     *
     * @param activity
     */
    private static void injectContentView(Activity activity) {

        //获取 Activity类对应的 Class 类型对象
        Class<? extends Activity> clazz = activity.getClass();

        //获取到 ContentView 注解
        ContentView contentViewAnnotation = clazz.getAnnotation(ContentView.class);

        if (contentViewAnnotation != null) {
            //获取到 ContentView 注解的值，即 layoutResID
            int contentViewLayoutId = contentViewAnnotation.value();

            try {
                /**
                 * 获取到 setContentView 方法
                 * getMethod 方法传入的第二个参数 int.class 是 setContentView方法参数的 Class 类型
                 */
                Method method = clazz.getMethod(METHOD_SET_CONTENT_VIEW, int.class);
                method.setAccessible(true);
                //回调 setContentView 方法
                method.invoke(activity, contentViewLayoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * 注入 View
     *
     * @param activity
     */
    private static void injectViews(Activity activity) {

        //获取 Activity类对应的 Class 类型对象
        Class<? extends Activity> clazz = activity.getClass();
        //获取到所有变量
        Field[] fields = clazz.getDeclaredFields();

        if (fields != null) {
            //遍历每一个变量
            for (Field field : fields) {
                //获取到当前变量的 BindView 注解
                BindView bindViewAnnotation = field.getAnnotation(BindView.class);

                if (bindViewAnnotation != null) {
                    //获取到 BindView 注解的值，即 Id
                    int viewId = bindViewAnnotation.value();

                    if (viewId != -1) {
                        try {
                            //获取到 findViewById 方法
                            Method method = clazz.getMethod(METHOD_FIND_VIEW_BY_ID, int.class);
                            method.setAccessible(true);
                            //调用 findViewById 方法，得到View
                            Object resView = method.invoke(activity, viewId);
                            field.setAccessible(true);
                            //将 View 设置给 field，完成向下转型
                            field.set(activity, resView);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
    }

    private static void injectEvent(Activity activity) {

        Class<? extends Activity> clazz = activity.getClass();

        //获取所有的方法
        Method[] methods = clazz.getMethods();

        //遍历所有方法
        for (Method method : methods) {

            //获取这个方法的所有注解，-> 如果是点击事件方法，@OnClick注解是其中之一
            Annotation[] annotations = method.getAnnotations();

            //遍历这个方法的所有注解 - > @OnClick注解是其中之一
            for (Annotation annotation : annotations) {

                //拿到注解的class对象,-> OnClick.class
                Class<? extends Annotation> annotationType = annotation.annotationType();

                //获取 @OnClick注解上的 @EventBase 注解
                EventBase eventBaseAnnotation = annotationType.getAnnotation(EventBase.class);


                if (eventBaseAnnotation != null) {

                    //取出 @EventBase 注解设置 监听器的名称、类型，调用的方法名
                    String listenerSetter = eventBaseAnnotation.listenerSetter();
                    Class<?> listenerType = eventBaseAnnotation.listenerType();
                    String methodName = eventBaseAnnotation.methodName();

                    try {
                        //获取 @OnClick 注解的 value 方法
                        Method value = annotationType.getDeclaredMethod("value");
                        //获取 @OnClick({......}) 注解上的所有 viewId
                        int[] viewIds = (int[]) value.invoke(annotation, null);

                        //动态代理
                        DynamicHandler handler = new DynamicHandler(activity);

                        //添加 @OnClick 注解的方法，比如可以是 onClick 方法
                        handler.addMethod(methodName, method);

                        //动态代理创建一个 OnClickListener 接口对象
                        Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(),
                                new Class<?>[]{listenerType}, handler);

                        //遍历所有的 ViewId
                        for (int viewId : viewIds) {
                            //初始化 View 实例
                            View view = activity.findViewById(viewId);
                            //获取到 setOnClickListener 方法，将 OnClickListener.class 传进 getMethod 方法
                            Method setEventListenerMethod = view.getClass()
                                    .getMethod(listenerSetter, listenerType);

                            //回调 setOnClickListener 方法
                            setEventListenerMethod.invoke(view, listener);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }


    }

}
