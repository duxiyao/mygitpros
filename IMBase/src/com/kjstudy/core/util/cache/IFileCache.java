package com.kjstudy.core.util.cache;

import com.kjstudy.core.util.transfer.ProgressListener;

/**
 * @author Administrator
 *
 * 缓存类接口
 */
public interface IFileCache  {
	/**
	 * 生成文件路径。默认的是指定的文件夹+url最后的文件名;病历夹的则是  param s=documentId-detailId-url
	 * @param s
	 */
	void generateFilePath(String s);
	String getFilePath();
	void put(Object data);
	void get(String url,IUse use);
	void get(String url,IUse use,ProgressListener listener);
	boolean isExists();
	void delete(); 
}	
