package com.kjstudy.core.util.transfer;

import java.io.File;

import android.text.TextUtils;

import com.kjstudy.core.thread.ThreadManager;

/**
 * @author duxiyao
 * 
 *         文件上传下载工具类。
 */
public class TransferUtil {

	/**
	 * 上传附件。
	 * 
	 * @param params
	 * @param listener
	 */
	public static void upload(ParamsUpload params, ProgressListener listener) {
		AbstractTransfered upload = new Upload(params);
		// AbstractTransfered upload = new UploadGet(params);
		exe(upload, listener);
	}

	/**
	 * 下载附件
	 * 
	 * @param downUrl
	 * @param dirPath
	 * @param listener
	 */
	public static void download(String downUrl, String filePath,
			ProgressListener listener) {

		if (TextUtils.isEmpty(filePath)) {
			if (null != listener)
				listener.onResponse(false, "", new Exception("lllegal"));
			return;
		}

		try {
			File f = new File(filePath);
			if (!f.getParentFile().exists())
				f.getParentFile().mkdirs();
		} catch (Exception e) {
			e.printStackTrace();
		}

		AbstractTransfered download = new Download(downUrl, null, filePath);
		exe(download, listener);
	}

	private static void exe(final AbstractTransfered transfered,
			final ProgressListener listener) {
		ThreadManager.getInstance().executeTask(new Runnable() {
			@Override
			public void run() {
				transfered.exe(listener);
			}
		});
	}

	// Bitmap转换成byte[]
	// public byte[] Bitmap2Bytes(Bitmap bm) {
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
	// return baos.toByteArray();
	// }
}
