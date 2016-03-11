package org.myframe.https;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReqConf {
	public String serverHost() default "";

	public String surffix();

	public boolean isPost() default true;
	/**
	 * 次 
	 * @return
	 */
	public int reReqCount() default 10;
	/**
	 * 秒
	 * @return
	 */
	public int reReqInterval() default 60;
}
