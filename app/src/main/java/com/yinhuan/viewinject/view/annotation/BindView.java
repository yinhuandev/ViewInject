package com.yinhuan.viewinject.view.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yinhuan on 2017/3/10.
 *
 * @Target(ElementType.TYPE) - >该注解用于变量
 * @Retention(RetentionPolicy.RUNTIME) ->该注解为 运行时 注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindView {
    /**
     * 通过 @BindView(R.id.button) 注解一个 View，然后可以通过 bindViewAnnotation.value() 获取
     * R.id.button 这个 Id 值
     * So ，其实可以理解 @BindView(R.id.button) 之后，给 value() 方法一个 return 返回值
     * @return
     */
    int value();
}
