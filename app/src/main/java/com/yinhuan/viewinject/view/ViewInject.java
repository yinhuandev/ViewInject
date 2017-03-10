package com.yinhuan.viewinject.view;

import android.app.Activity;

import com.yinhuan.viewinject.view.annotation.BindView;
import com.yinhuan.viewinject.view.annotation.ContentView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by yinhuan on 2017/3/10.
 */

public class ViewInject {

    private static final String METHOD_SET_CONTENT_VIEW = "setContentView";
    private static final String METHOD_FIND_VIEW_BY_ID = "findViewById";


    /**
     * 注入接口
     * @param activity
     */
    public static void inject(Activity activity){
        injectContentView(activity);
        injectViews(activity);
    }


    /**
     * 注入 ContentView
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

}
