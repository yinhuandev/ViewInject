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
    int value();
}
