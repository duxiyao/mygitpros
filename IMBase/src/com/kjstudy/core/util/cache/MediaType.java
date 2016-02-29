package com.kjstudy.core.util.cache;

import android.text.TextUtils;

public class MediaType {
	public static final String NONE = "MediaType.none";
	public static final String IMG = "MediaType.img";
	public static final String AUDIO = "MediaType.audio";
	public static final String VEDIO = "MediaType.vedio";

	private static final String[] imgs = { ".jpg", ".jpeg", ".png", ".gif",
			".bmp", ".wbmp", ".ico", ".jpe" };

	private static final String[] audios = { ".amr" };

	private static final String[] vedios = { ".mp4", ".3gp" };

	/**
	 * 穿进去一个字串，判断是否含有指定格式的字串。 判断文件格式。
	 * 
	 * @param url
	 * @return
	 */
	public static String getMediaType(String url) {

		if (TextUtils.isEmpty(url)) {

			return NONE;
		} else if (isImg(url)) {
			return IMG;
		} else if (isAudio(url)) {
			return AUDIO;
		} else if (isVedio(url)) {
			return VEDIO;
		} else {
			return NONE;
		}

	}

	public static boolean isImg(String content) {
		for (String str : imgs) {
			if (content.toLowerCase().contains(str))
				return true;
		}
		return false;
	}

	private static boolean isAudio(String content) {
		for (String str : audios) {
			if (content.toLowerCase().contains(str))
				return true;
		}
		return false;
	}

	private static boolean isVedio(String content) {
		for (String str : vedios) {
			if (content.toLowerCase().contains(str))
				return true;
		}
		return false;
	}
}
