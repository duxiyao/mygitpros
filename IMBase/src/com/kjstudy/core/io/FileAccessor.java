package com.kjstudy.core.io;

import java.io.File;

import android.os.Environment;

public class FileAccessor {

	public static final String APP_NAME = "ts";
	public static final String APPS_ROOT_DIR = getExternalStorePath() + "/"
			+ APP_NAME;
	public static final String CRASH_PATH = APPS_ROOT_DIR + "/Log";
	public static final String DB_FILENAME_BEFORE_LOGIN = "login_state.db";
	public static final String DBNAME = "test.db";
	public static final String DBPATH = "sdcard";
	public static final String QRCODE_PATH = "/qrcode";

	public static final String IMESSAGE_VOICE = APPS_ROOT_DIR + "/voice";
	public static final String IMESSAGE_IMAGE = APPS_ROOT_DIR + "/image";
	public static final String IMESSAGE_FILE = APPS_ROOT_DIR + "/file";
	public static final String CCP_AVATAR = APPS_ROOT_DIR + "/avatar";
	public static final String CONTACT_BAKUP = APPS_ROOT_DIR + "/backup";
	public static final String ATTACH_IMAGE = APPS_ROOT_DIR + "/attach/";
	public static final String IMAGE_HEADS = APPS_ROOT_DIR + "/image/heads";
	public static final String IMAGE_CLAMP = APPS_ROOT_DIR + "/image/clamp";
	public static final String GENERAL_FILE = APPS_ROOT_DIR + "/general/file";
	public static final String TMP_DIR = APPS_ROOT_DIR + "/tmp/";
	public static final String TMP_HEAD_IMG_FILEPATH = APPS_ROOT_DIR
			+ "/tmp/tmphead.png";

	/**
	 * 是否有外存卡
	 * 
	 * @return
	 */
	public static boolean isExistExternalStore() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * /sdcard
	 * 
	 * @return
	 */
	public static String getExternalStorePath() {
		if (isExistExternalStore()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		return null;
	}

	public static File creatFile(String filename) {
		String rootPath = FileAccessor.CRASH_PATH;
		File dir = new File(rootPath);
		try {
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File f = new File(rootPath + File.separator + filename);
			if (!f.exists()) {
				f.createNewFile();
			}
			return f;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 检查文件是否存在
	 * 
	 * @return
	 */
	public static boolean checkDir(String cacheDir) {
		File f = new File(cacheDir);
		if (!f.exists()) {
			return f.mkdirs();
		}
		return true;
	}
}
