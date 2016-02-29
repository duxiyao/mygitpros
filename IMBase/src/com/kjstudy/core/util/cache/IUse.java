package com.kjstudy.core.util.cache;

/**
 * @author Administrator
 * 
 * 获得到资源后的回调
 */
public interface IUse {
	void use(boolean canUse,String filePath,String url);
}
