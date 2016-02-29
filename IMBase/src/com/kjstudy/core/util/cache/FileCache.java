package com.kjstudy.core.util.cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.kjstudy.core.thread.ThreadManager;
import com.kjstudy.core.util.transfer.ProgressListener;
import com.kjstudy.core.util.transfer.TransferUtil;

/**
 * @author Administrator
 * 
 *         文件缓存类抽象。子类需要先调用{@link #generateFilePath(String)}
 *         生成本地路劲(保存在mFilePath中)后，再使用其他功能。
 */
public abstract class FileCache implements IFileCache {
	protected String mDirPath;
	protected String mFilePath;

	/**
	 * 获得url / 结尾最后的文件名
	 * 
	 * @param url
	 * @return
	 */
	protected String getFileName(String url) {

		if (TextUtils.isEmpty(url)) {
			return "";
		}
		if (url.contains("/"))
			return url.substring(url.lastIndexOf("/"));
		else
			return url;
	}

	@Override
	public String getFilePath() {
		return mFilePath;
	}

	@Override
	public void generateFilePath(String s) {
		try {
			File file = new File(mDirPath);
			file.mkdirs();
			mFilePath = mDirPath + getFileName(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (TextUtils.isEmpty(mFilePath)) {
			throw new RuntimeException("mFilePath must be generated");
		}
	}

	@Override
	public boolean isExists() {
		File file = new File(mFilePath);
		return file.exists();
	}

	@Override
	public void get(String url, IUse iUse) {
		get(url, iUse, null);
	}

	@Override
	public void get(final String url, final IUse iUse,
			final ProgressListener listener) {

		ThreadManager.getInstance().executeTask(new Runnable() {

			@Override
			public void run() {
				try {
					if (isExists()) {
						callBack(true, mFilePath, url, iUse);
					} else {
						download(url, mFilePath, iUse, listener);
					}
				} catch (Exception e) {
					callBack(false, mFilePath, url, iUse);
					e.printStackTrace();
				}

			}
		});
	}

	@Override
	public void delete() {
		if (isExists()) {
			File file = new File(mFilePath);
			file.delete();
		}
	}

	/**
	 * 下载文件并保存
	 * 
	 * @param url
	 * @param filePath
	 * @param iUse
	 */
	public void download(String url, String filePath, IUse iUse) {
		download(url, filePath, iUse, null);
	}

	public void download(final String url, final String filePath,
			final IUse iUse, final ProgressListener listener) {
		TransferUtil.download(url, filePath, new ProgressListener() {
			@Override
			public void transferred(long percent) {
				if (listener != null) {
					listener.transferred(percent);
				}
			}

			@Override
			public void onResponse(boolean isOk, String ret, Exception e) {
				if (listener != null) {
					listener.onResponse(isOk, ret, e);
				}
				callBack(isOk, filePath, url, iUse);

			}
		});
	}

	private void callBack(final boolean ok, final String filePath,
			final String url, final IUse iUse) {
		if (iUse != null) {
			iUse.use(ok, filePath, url);
		}
	}

	/**
	 * 保存图片
	 * 
	 * @param filePath
	 * @param bmp
	 */
	protected void save(String filePath, Bitmap bmp) {
		File file = new File(filePath);
		FileOutputStream b = null;
		try {
			b = new FileOutputStream(file.getAbsolutePath());
			bmp.compress(Bitmap.CompressFormat.JPEG, 80, b);// 把数据写入文件
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
