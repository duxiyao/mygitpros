package com.kjstudy.core.util.transfer;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import android.util.Log;

/**
 * @author duxiyao
 * 
 *         下载文件类。
 */
public class Download extends AbstractTransfered {

	private static HashMap<String, ProgressListener> mHold = new HashMap<String, ProgressListener>();

	private String downUrl, dirPath, filePath;

	public Download(String downUrl, String dirPath, String filePath) {
		this.downUrl = downUrl;
		this.dirPath = dirPath;
		this.filePath = filePath;
	}

	private String getFileName() {
		if (null == downUrl)
			return null;
		String[] tmp = downUrl.split("/");
		if (tmp.length <= 0)
			return null;
		return tmp[tmp.length - 1];
	}

	public void download(ProgressListener listener) {
		try {
			File f = null;
			if (filePath != null) {
				f = new File(filePath);
				if (!f.exists()) {
					File tmpf = new File(filePath.substring(0,
							filePath.lastIndexOf("/")));
					tmpf.mkdirs();
				}
			} else {
				String fileName = getFileName();
				f = new File(dirPath + fileName);
			}
			// if (f.exists()) {
			// listener.onRespone(true, "file exists", null);
			// return;
			// }
			f.setLastModified(System.currentTimeMillis());
			int downloadSize = 0;
			URL u = new URL(downUrl);
			URLConnection conn = u.openConnection();
			conn.connect();

			// Map<String,List<String>> tmpmap = conn.getHeaderFields();
			// String fileName = conn.getHeaderField(6);
			// fileName =
			// URLDecoder.decode(fileName.substring(fileName.indexOf("filename=")+9),"UTF-8");
			// System.out.println("文件名为："+fileName);

			InputStream is = conn.getInputStream();
			int fileSize = conn.getContentLength();
			if (fileSize < 1 || is == null) {
				// sendMessage(DOWNLOAD_ERROR);
				System.out.println("DOWNLOAD_ERROR");
				throw new Exception("lllegal");
			} else {
				System.out.println("DOWNLOAD_PREPARE");
				FileOutputStream fos = new FileOutputStream(f);
				byte[] bytes = new byte[1024];
				int len = -1;
				while ((len = is.read(bytes)) != -1) {
					fos.write(bytes, 0, len);
					downloadSize += len;
					// sendMessage(DOWNLOAD_WORK);
					System.out.println("--" + (downloadSize / (float) fileSize)
							* 100);
					Log.e("download file -----", String
							.valueOf((downloadSize / (float) fileSize) * 100));
					if (null != listener)
						listener.transferred((long) ((downloadSize / (float) fileSize) * 100));
				}
				System.out.println("DOWNLOAD_OK");
				is.close();
				fos.close();
				listener.onResponse(true, "", null);
				mHold.remove(downUrl);
			}
		} catch (Exception e) {
			System.out.println("DOWNLOAD_ERROR");
			e.printStackTrace();
			listener.onResponse(false, "", e);
			mHold.remove(downUrl);
		}
	}

	@Override
	public void exe(ProgressListener listener) {
		if (mHold.containsKey(downUrl)) {
			listener = mHold.get(downUrl);
		} else {
			mHold.put(downUrl, listener);
			download(listener);
		}
	}
}
