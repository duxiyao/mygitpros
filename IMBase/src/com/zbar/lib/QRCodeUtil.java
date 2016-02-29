package com.zbar.lib;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.imbase.MyApplication;
import com.kjstudy.core.io.FileAccessor;
import com.imbase.R;

public class QRCodeUtil {
	public final static String TAG = "QRCodeUtil";
	// 圆角半径
	private static int RADIUS = 10;
	// 留白填充宽度
	private static int MARGIN = 4;

	private static int FRONTCOLOR = 0xff000000;
	// 图片宽度的一半
	private static int IMAGE_HALFWIDTH = 40;
	private static int BITMSPWIDTH = 236;
	private int[][] src = null;
	private Bitmap mDefault;

	public QRCodeUtil() {
		mDefault = BitmapFactory.decodeResource(MyApplication.getInstance()
				.getResources(), R.drawable.consult_masses);
		BITMSPWIDTH = (int) MyApplication.getInstance().getResources()
				.getDimension(R.dimen.dp_236);
		IMAGE_HALFWIDTH = (int) MyApplication.getInstance().getResources()
				.getDimension(R.dimen.dp_24);
	}

	// 图片的压缩、圆角处理,并生成数组
	public int[][] getPic(Bitmap mBitmap) {
		int imgWidth = (IMAGE_HALFWIDTH * 2);
		// 缩放图片
		Matrix m = new Matrix();
		float sx = (float) imgWidth / mBitmap.getWidth();
		float sy = (float) imgWidth / mBitmap.getHeight();
		m.setScale(sx, sy);
		// 重新构造一个40*40的图片
		Bitmap bitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
				mBitmap.getHeight(), m, false);
		int[][] src = new int[imgWidth][imgWidth];
		// 圆角处理半径
		int r = RADIUS;
		int max = imgWidth;
		int bordercolor = 0x00000000;
		int whitecolor = 0xffffffff;
		for (int x = 0; x < imgWidth; x++) {
			for (int y = 0; y < imgWidth; y++) {
				if (x < r
						&& y < r
						&& ((r - x) * (r - x) + (r - y) * (r - y) > (r - 1)
								* (r - 1))) {
					// 左上圆角
					if ((r - x) * (r - x) + (r - y) * (r - y) > r * r) {
						src[x][y] = whitecolor;
					} else {
						src[x][y] = bordercolor;
					}
				} else if (x > (max - r)
						&& y < r
						&& (x + r - max) * (x + r - max) + (r - y) * (r - y) > (r - 1)
								* (r - 1)) {
					// 右上圆角
					if ((x + r - max) * (x + r - max) + (r - y) * (r - y) > r
							* r) {
						src[x][y] = whitecolor;
					} else {
						src[x][y] = bordercolor;
					}
				} else if (x < r
						&& y > (max - r)
						&& (r - x) * (r - x) + (y + r - max) * (y + r - max) > (r - 1)
								* (r - 1)) {
					// 左下圆角
					if ((r - x) * (r - x) + (y + r - max) * (y + r - max) > r
							* r) {
						src[x][y] = whitecolor;
					} else {
						src[x][y] = bordercolor;
					}
				} else if (x > (max - r)
						&& y > (max - r)
						&& (x + r - max) * (x + r - max) + (y + r - max)
								* (y + r - max) > (r - 1) * (r - 1)) {
					// 右下圆角
					if ((x + r - max) * (x + r - max) + (y + r - max)
							* (y + r - max) > r * r) {
						src[x][y] = whitecolor;
					} else {
						src[x][y] = bordercolor;
					}
				} else {
					if (((x >= r && x <= max - r) && (y == 0 || y == 1
							|| y == max - 1 || y == max))
							|| ((y >= r && y <= max - r) && (x == 0 || x == 1
									|| x == max - 1 || x == max))) {
						// 四周除圆角的边框
						src[x][y] = bordercolor;
					} else {
						// 图片值
						src[x][y] = bitmap.getPixel(x, y);
					}
				}
			}
		}
		this.src = src;
		return src;
	}

	/**
	 * 生成二维码
	 * 
	 * @param 字符串
	 * @return Bitmap
	 * @throws WriterException
	 */
	public Bitmap cretaeBitmap(String str, int[][] src) throws WriterException {
		if (src != null && src.length > 0)
			this.src = src;
		return cretaeBitmap(str);
	}

	/**
	 * 生成二维码
	 * 
	 * @param 字符串
	 * @return Bitmap
	 * @throws WriterException
	 */
	public Bitmap cretaeBitmap(String str) throws WriterException {
		HashMap<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 1);

		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, BITMSPWIDTH, BITMSPWIDTH, hints);
		int width = matrix.getWidth();
		// 处理后图片的数据
		int pixels[] = new int[width * width];
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		// 填充色
		int margincolor = 0xffffffff;// 白色
		int w_half = width / 2;
		int frame = MARGIN;
		int img_half = IMAGE_HALFWIDTH;
		int r = RADIUS;
		int near = width / 2 - img_half - frame + r;// 101
		int far = width / 2 + img_half + frame - r;// 149
		for (int y = 0; y < width; y++) {
			for (int x = 0; x < width; x++) {
				/*
				 * if (!true) { // 二维码 pixels[y * width + x] = matrix.get(x, y)
				 * ? FRONTCOLOR : margincolor; } else
				 */{
					// 中间图片
					if (x > w_half - img_half && x < w_half + img_half
							&& y > w_half - img_half && y < w_half + img_half) {
						//
						pixels[y * width + x] = src[x - w_half + img_half][y
								- w_half + img_half];

					} else if ((x > w_half - img_half - frame // 左边框
							&& x < w_half - img_half + frame
							&& y > w_half - img_half - frame && y < w_half
							+ img_half + frame)
							|| (x > w_half + img_half - frame // 右边框
									&& x < w_half + img_half + frame
									&& y > w_half - img_half - frame && y < w_half
									+ img_half + frame)
							|| (x > w_half - img_half - frame // 上边框
									&& x < w_half + img_half + frame
									&& y > w_half - img_half - frame && y < w_half
									- img_half + frame)
							|| (x > w_half - img_half - frame // 下边框
									&& x < w_half + img_half + frame
									&& y > w_half + img_half - frame && y < w_half
									+ img_half + frame)) {

						// 圆角处理
						if (x < near
								&& y < near
								&& (near - x) * (near - x) + (near - y)
										* (near - y) > r * r) {
							// 左上圆角
							pixels[y * width + x] = matrix.get(x, y) ? FRONTCOLOR
									: margincolor;
						} else if (x > far
								&& y < near
								&& (x - far) * (x - far) + (near - y)
										* (near - y) > r * r) {
							// 右上圆角
							pixels[y * width + x] = matrix.get(x, y) ? FRONTCOLOR
									: margincolor;
						} else if (x < near
								&& y > far
								&& (near - x) * (near - x) + (y - far)
										* (y - far) > r * r) {
							// 左下圆角
							pixels[y * width + x] = matrix.get(x, y) ? FRONTCOLOR
									: margincolor;
						} else if (x > far
								&& y > far
								&& (x - far) * (x - far) + (y - far)
										* (y - far) > r * r) {
							// 右下圆角
							pixels[y * width + x] = matrix.get(x, y) ? FRONTCOLOR
									: margincolor;
						} else {
							// 边框填充颜色
							pixels[y * width + x] = margincolor;
						}
					} else {
						// 二维码
						pixels[y * width + x] = matrix.get(x, y) ? FRONTCOLOR
								: margincolor;
					}
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, width,
				Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap
		bitmap.setPixels(pixels, 0, width, 0, 0, width, width);
		return bitmap;
	}

	/**
	 * 生成二维码图片(在子线程中调用 )
	 * 
	 * @param data
	 *            数据
	 * @param center
	 *            中间显示图片
	 * @param save
	 *            是否保存本地
	 * @return
	 * @throws WriterException
	 */
	public Bitmap getQrcode(String data, Bitmap center, boolean save) {
		if (center == null) {
			src = getPic(mDefault);
		} else {
			src = getPic(center);
		}
		Bitmap result = null;
		try {
			result = cretaeBitmap(data, src);
			save(result);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return result;
	}

	private void save(Bitmap bitmap) {
		ByteArrayOutputStream by = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, by);
		byte[] stream = by.toByteArray();
		FileOutputStream fos = null;
		FileAccessor.checkDir(FileAccessor.QRCODE_PATH);
		try {
			fos = new FileOutputStream(new File(FileAccessor.QRCODE_PATH
					+ "/tmp.jpg"));
			fos.write(stream);
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
