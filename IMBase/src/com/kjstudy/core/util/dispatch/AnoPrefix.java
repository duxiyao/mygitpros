package com.kjstudy.core.util.dispatch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnoPrefix {
    public String pre();

    public boolean un() default false;

    public boolean finish() default false;

    public boolean isCondition() default false;

    public int order() default -1;
}
