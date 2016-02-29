package com.kjstudy.core.util.cache;

import com.kjstudy.core.io.FileAccessor;

/**
 * @author Administrator
 * 
 * 病历夹列表图片文件缓存类
 */
public class ThumbnailClampImgCache extends ImageCache {
	public ThumbnailClampImgCache(){
		mDirPath = FileAccessor.IMAGE_CLAMP;
	}
}
