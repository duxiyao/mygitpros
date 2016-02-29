package com.kjstudy.test.view;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

@SuppressLint("NewApi")
public class ImgView extends View {

	private List<String> mUrls;
	private List<Bitmap> mImgs;
	private int mImgCount;
	private int mWidgh, mHeight;
	private float mGap = 5;
	private final int mRowCount = 3;
	private float mDimension;

	public ImgView(Context context) {
		super(context);
		init();
	}

	public ImgView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ImgView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() { 
		this.addOnLayoutChangeListener(new OnLayoutChangeListener() {

			@Override
			public void onLayoutChange(View v, int left, int top, int right,
					int bottom, int oldLeft, int oldTop, int oldRight,
					int oldBottom) {
				mWidgh = right - left;
				mHeight = bottom - top;
			}
		});
	}

	public void setImgs(List<String> urls) {
		if (urls == null)
			return;
		mUrls = urls;
		mImgCount = mUrls.size();
		mImgs = new ArrayList<Bitmap>();
		for (String url : mUrls) {
			downimg(url);
		}
	}

	private void downimg(final String url) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// download imgs
				Bitmap bmp = null;
				URL neturl;
				try {
					neturl = new URL(url);
					HttpURLConnection conn = (HttpURLConnection) neturl
							.openConnection();
					conn.setConnectTimeout(5 * 1000);
					conn.setRequestMethod("GET");
					if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
						bmp = BitmapFactory.decodeStream(conn.getInputStream());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// after img downloaded
				if (bmp != null && mImgs != null) {
					mImgs.add(bmp);
					postInvalidate();
				}
			}
		}).start();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		int dimen = Math.min(width, height);
		setMeasuredDimension(dimen, dimen);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawImgs(canvas);
	}

	private synchronized void drawImgs(Canvas canvas) {
		if (mImgs == null)
			return;
		int count = mImgs.size();

		if (count >= 3)
			mDimension = (mWidgh - 4 * mGap) / 3f;
		else
			mDimension = (mWidgh - mGap * (count + 1)) / count;

		Paint paint = new Paint();
		paint.setAntiAlias(true);

		for (int i = 0; i < count; i++) {
			Bitmap bitmap = mImgs.get(i);
			Matrix matrix = new Matrix();
			matrix.postScale((float) mDimension / bitmap.getWidth(),
					(float) mDimension / bitmap.getHeight());
			canvas.save();
			float left = getLeftOffset(i, count);
			float top = getTopOffset(i, count);
			canvas.translate(left, top);
			Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			canvas.drawBitmap(newBitmap, 0, 0, paint);
			canvas.restore();
		}
	}

	private float getTopOffset(int pos, int curCount) {

		int row = pos / mRowCount;

		if (row == 0)
			return mGap;
		else {
			return (row + 1) * mGap + (row * mDimension);
		}
	}

	private float getLeftOffset(int pos, int curCount) {

		int col = pos % mRowCount;

		if (col == 0)
			return mGap;
		else {
			return (col + 1) * mGap + col * mDimension;
		}
	}

}
