package com.kjstudy.core.util.cache;

import com.kjstudy.core.io.FileAccessor;

public class GeneralFileCache extends FileCache {

	public GeneralFileCache() {
		mDirPath = FileAccessor.GENERAL_FILE;
	}

	@Override
	public void put(Object data) {
	}

}
