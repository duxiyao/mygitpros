package com.kjstudy.core.util.cache;

import android.graphics.Bitmap;

import com.kjstudy.core.io.FileAccessor;

/**
 * @author Administrator
 *
 * 图片文件缓存类
 */
public class ImageCache extends FileCache { 

	public ImageCache(){
		mDirPath = FileAccessor.IMESSAGE_IMAGE+"/";
		mFilePath = "";
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
