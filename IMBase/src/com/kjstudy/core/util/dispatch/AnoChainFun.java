package com.kjstudy.core.util.dispatch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 改函数处理逻辑描述
 * @author duxiyao
 * @date 2016年1月9日 上午10:41:20
 * 
 */ 
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnoChainFun {
    public int order();
}
