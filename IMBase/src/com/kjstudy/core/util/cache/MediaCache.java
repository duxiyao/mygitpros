package com.kjstudy.core.util.cache;

import java.io.File;

import org.kymjs.kjframe.utils.FileUtils;

import android.util.Log;

import com.kjstudy.core.io.FileAccessor;

/**
 * @author Administrator
 * 
 *         音视文件缓存类
 */
public class MediaCache extends FileCache {

	public MediaCache() {
		mDirPath = FileAccessor.ATTACH_IMAGE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hisun.sinldo.consult.cache.FileCache#generateFilePath(java.lang.String
	 * ) 传的参数s，必须为 documentId-detailId-url. ex:
	 * 10003-2455-https://xxx/xxxx/xx.suffix 在调用父类的get时候，参数需与上边描述一致
	 */
	@Override
	public void generateFilePath(String s) {
		File file = new File(mDirPath);
		file.mkdirs();
		mFilePath = mDirPath + parseFilePath(s);
	}

	/**
	 * 把localPath路径的文件，移动到s路径下。 其中，s与generateFilePath路径规则一致
	 * 
	 * @param srcFilePath
	 * @param s
	 */
	public static void move(String srcFilePath, String s) {
		String dirPath = FileAccessor.ATTACH_IMAGE + parseDirPath(s);
		String fileName = s.substring(s.lastIndexOf("/"));
		if (FileUtils.copyFile(srcFilePath, dirPath, fileName)) {
			File file = new File(srcFilePath);
			try {
				file.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Deprecated
	@Override
	public void put(Object data) {
	}

	private static String parseDirPath(String s) {
		try {
			String[] ss = s.split("-");
			String docId = ss[0];
			String detailId = ss[1];
			return docId + "/" + detailId + "/";
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("AVCache", "use error!!!!!");
		}
		return "";
	}

	private static String parseFilePath(String s) {
		try {
			String[] ss = s.split("-");
			String docId = ss[0];
			String detailId = ss[1];
			String fileName = s.substring(s.lastIndexOf("/"));
			return parseDirPath(s) + fileName;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("AVCache", "use error!!!!!");
		}
		return "";
	}

	public static String getPathString(String docId, String detailId, String url) {
		return docId + "-" + detailId + "-" + url;
	}

}
