package com.kjstudy.core.util.cache;


/**
 * @author Administrator
 * 
 *         生存文件缓存的简单工厂类。
 */
public class CacheFactory {

	public static IFileCache getGeneralFileCache() {
		return new GeneralFileCache();
	}

	/**
	 * 音视频文件存储接口
	 * 
	 * @param url
	 * @return
	 */
	public static IFileCache getICache(String url) {
		if (MediaType.AUDIO.equals(MediaType.getMediaType(url))) {
			return new AudioCache();
		} else if (MediaType.VEDIO.equals(MediaType.getMediaType(url))) {
			return new VideoCache();
		}
		return null;
	}

	/**
	 * 图片文件存储接口
	 * 
	 * @param url
	 * @param imgType
	 *            0 普通的图片 ; 1 病历夹图片; 2 头像图片; 3 病历夹列表图片
	 * @return
	 */
	public static IFileCache getICache(String url, int imgType) {
		IFileCache f = null;
//		if (MediaType.IMG.equals(MediaType.getMediaType(url))) {
			switch (imgType) {
			case 0:
				f = new ImageCache();
				f.generateFilePath(url);
				break;
			case 1:
				f = new AttachImageCache();
				break;
			case 2:
				f = new HeadImageCache();
				f.generateFilePath(url);
				break;
			case 3:
				f = new ThumbnailClampImgCache();
				f.generateFilePath(url);
				break;
			default:
				break;
			}
//		}
		return f;
	}
}
