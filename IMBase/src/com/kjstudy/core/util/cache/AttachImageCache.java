package com.kjstudy.core.util.cache;

import android.graphics.Bitmap;

import com.kjstudy.core.io.FileAccessor;


/**
 * @author Administrator
 * 
 * 附件图片文件缓存。
 */
public class AttachImageCache extends MediaCache {

	public AttachImageCache() { 
		mDirPath = FileAccessor.ATTACH_IMAGE;
	} 
	
	@Override
	public void put(Object data) {
		if (data instanceof Bitmap) {
			if (!isExists()) {
				Bitmap bmp = (Bitmap) data;
				save(mFilePath, bmp);
			}
		}
	}
}
