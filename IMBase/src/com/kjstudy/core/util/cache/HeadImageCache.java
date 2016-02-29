package com.kjstudy.core.util.cache;

import com.kjstudy.core.io.FileAccessor;

/**
 * @author Administrator
 *
 * 头像图片文件缓存类
 */
public class HeadImageCache extends ImageCache {

	public HeadImageCache() {
		mDirPath = FileAccessor.IMAGE_HEADS+"/";
	}
}
