package com.kjstudy.core.util.cache;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.kjstudy.core.util.CheckUtil;

/**
 * @author duxiyao
 * 
 *         缓存相关使用
 */
@SuppressLint("NewApi")
public class CacheManager {

	/**
	 * 通过手机号填充头像 用手机号从数据库中查出url
	 * 
	 * @param iv
	 * @param phoneNum
	 * @param defResId
	 */
	public static void inflateHeadByPhone(ImageView iv, String phoneNum,
			int defResId) {
		inflateHeadByPhone(iv, phoneNum, defResId, false);
	}

	/**
	 * 
	 * @param iv
	 * @param phoneNum
	 * @param defResId
	 * @param isFront
	 *            是否是ImageView的前景
	 */
	public static void inflateHeadByPhone(ImageView iv, String phoneNum,
			int defResId, boolean isFront) {
		if (TextUtils.isEmpty(phoneNum) || !CheckUtil.checkMDN(phoneNum)) {
			if (null != iv && defResId != -1) {
				setBackgroundResource(iv, defResId);
			}
			return;
		}
		// get url from database
		String url = "";// UserSipInfoStorage.getInstance().queryPhotoUrl(phoneNum);
		if (isFront)
			inflateHead(url, iv, defResId, isFront, ((BitmapDrawable) iv
					.getResources().getDrawable(defResId)).getBitmap());
		else
			inflateHead(url, iv, defResId);
	}

	/**
	 * 根据提供的url，填充头像
	 * 
	 * @param url
	 * @param iv
	 */
	public static void inflateHead(String url, final ImageView iv, int defResId) {
		inflateHead(url, iv, defResId, false, null);
	}

	public static void inflateHeadFront(String url, final ImageView iv,
			int defResId) {
		inflateHead(url, iv, defResId, true, null);
	}

	public static void inflateImage(String url, final ImageView iv, int defResId) {
		inflateResource(url, iv, defResId, true, 0, null);
	}

	public static void inflateImageToBackground(String url, final ImageView iv,
			int defResId) {
		inflateResource(url, iv, defResId, false, 0, null);
	}

	/**
	 * @param url
	 * @param iv
	 * @param defResId
	 * @param isFront
	 *            前景
	 */
	public synchronized static void inflateHead(final String url,
			final ImageView iv, int defResId, final boolean isFront,
			final Bitmap frontDefaultBmp) {
		inflateResource(url, iv, defResId, isFront, 2, frontDefaultBmp);
	}

	public synchronized static void inflateResource(final String url,
			final ImageView iv, final int defResId, final boolean isFront,
			int resType, final Bitmap frontDefaultBmp) {
		if (!CheckUtil.checkUrl(url)) {
			setImageView(iv, !isFront, frontDefaultBmp, defResId);
			return;
		}
		final MemoryCache mc = MemoryCache.getInstance();
		Bitmap bitmap;
		if (mc.contains(url) && (bitmap = mc.get(url)) != null) {
			setImageView(iv, !isFront, bitmap, defResId);
		} else {
			IFileCache f = CacheFactory.getICache(url, resType);
			if (f == null) {
				setImageView(iv, !isFront, frontDefaultBmp, defResId);
				return;
			}
			if (!f.isExists()) {
				setImageView(iv, !isFront, frontDefaultBmp, defResId);
			}
			iv.setTag(url);
			f.get(url, new IUse() {

				@Override
				public void use(boolean canUse, String filePath, String url) {
					if (!canUse)
						return;
					final Bitmap bmp = BitmapFactory.decodeFile(filePath);
					String tmpUrl = String.valueOf(iv.getTag());
					if (bmp != null && url.equalsIgnoreCase(tmpUrl)) {
						mc.put(url, bmp);
						setImageView(iv, !isFront, bmp, defResId, 100);
					}
				}
			});
		}
	}

	private static void setImageView(ImageView iv, boolean isBackground,
			Bitmap defaultBmp, int defaultResId) {
		setImageView(iv, isBackground, defaultBmp, defaultResId, 0);
	}

	private static void setImageView(final ImageView iv,
			final boolean isBackground, final Bitmap defaultBmp,
			final int defaultResId, int time) {
		iv.postDelayed(new Runnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				try {
					if (isBackground) {
						if (defaultBmp != null) {
							try {
								iv.setBackground(new BitmapDrawable(iv
										.getResources(), defaultBmp));
							} catch (Exception ex) {
								ex.printStackTrace();
								iv.setBackgroundDrawable(new BitmapDrawable(iv
										.getResources(), defaultBmp));
							}
						} else {
							iv.setBackgroundResource(defaultResId);
						}
					} else {
						try {
							iv.setScaleType(ImageView.ScaleType.FIT_XY);
						} catch (Exception e) {
						}
						if (defaultBmp != null) {
							iv.setImageBitmap(defaultBmp);
						} else {
							iv.setImageResource(defaultResId);
						}
					}
					iv.invalidate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, time);
	}

	private static void setBackgroundResource(final ImageView iv,
			final int resId) {
		setImageView(iv, true, null, resId, 0);
	}

	/**
	 * 判断病历夹列表缩略图是否存在
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isExistsThumbClampImg(String url) {
		IFileCache f = CacheFactory.getICache(url, 3);
		if (f == null) {
			return false;
		}
		return f.isExists();
	}

	/**
	 * 在病历夹列表缩略图存在的前提下，取出对应的缩略图
	 * 
	 * @param url
	 * @return
	 */
	public static Drawable getThumbClampImg(String url) {
		return getThumbClampImg(null, url);
	}

	public static Drawable getThumbClampImg(Resources res, String url) {
		if (MemoryCache.getInstance().contains(url)) {
			return new BitmapDrawable(res, MemoryCache.getInstance().get(url));
		}
		IFileCache f = CacheFactory.getICache(url, 3);
		if (f == null || !f.isExists())
			return null;
		String filePath = f.getFilePath();
		return new BitmapDrawable(res, BitmapFactory.decodeFile(filePath));
	}

	/**
	 * 在病历夹列表缩略图不存在的前提下，将缩略图保存到本地
	 * 
	 * @param url
	 * @param bmp
	 */
	public static void putThumbClampImg(String url, Bitmap bmp) {
		MemoryCache.getInstance().put(url, bmp);
		IFileCache f = CacheFactory.getICache(url, 3);
		if (f != null)
			f.put(bmp);
	}
}
