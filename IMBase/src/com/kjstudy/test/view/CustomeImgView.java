package com.kjstudy.test.view;

import java.util.HashMap;
import java.util.List;

import org.kymjs.kjframe.utils.DensityUtils;
import org.kymjs.kjframe.utils.KJLoger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

public class CustomeImgView extends ImageView {

	public interface OnDataInitListener {
		void onDataInit(CustomeImgView v);
	}

	private Bitmap[] mBitmaps;
	private int mCurClickIndex = -1;
	private List<MapInfo> mDatas;
	private MapInfo mCurMapInfo;
	private HashMap<Integer, MapInfo> mH;
	private OnDataInitListener mOnDataInitListener;

	public CustomeImgView(Context context) {
		super(context);
	}

	public CustomeImgView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomeImgView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setDatas(final List<MapInfo> datas) {
		if (datas == null || datas.size() == 0)
			return;
		if (mH == null)
			mH = new HashMap<Integer, MapInfo>();
		mH.clear();
		mDatas = datas;
		int len = mDatas.size();
		for (int i = 0; i < len; i++)
			mH.put(i, mDatas.get(i));
		mBitmaps = new Bitmap[len];
		init();
		if (mOnDataInitListener != null)
			mOnDataInitListener.onDataInit(this);
	}

	private void init() {
		if (mDatas == null)
			return;
		int len = mDatas.size();
		for (int i = 0; i < len; i++) {
			MapInfo mi = mH.get(i);
			if (mi == null){
//				throw new RuntimeException("mapinfo could not be null");
				return;
			}
			mBitmaps[i] = BitmapFactory.decodeResource(getResources(),
					mi.getResIdDef());
		}
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mBitmaps != null && mBitmaps[0] != null) {
			int left = DensityUtils.getScreenW()/ 2
					- mBitmaps[0].getWidth() / 2;
			left = 0;
			for (int i = 0; i < mBitmaps.length; i++) {
				if (mBitmaps[i] != null)
					canvas.drawBitmap(mBitmaps[i], left, 0, null);
			}
		}
	}

	@Override
	public synchronized boolean onTouchEvent(final MotionEvent event) {
		KJLoger.debug("action-->" + event.getAction());

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			recoveryHighlight();
			which(event.getX(), event.getY());
			onClick(1);
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			postDelayed(new Runnable() {

				@Override
				public synchronized void run() {
					synchronized (this) {
						onClick(2);	
					}					
				}
			}, 1);
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			recoveryHighlight();
			which(event.getX(), event.getY());
			onClick(3);
		}
		return true;
	}

	private synchronized void recoveryHighlight() {
		if (mCurClickIndex == -1 || mCurMapInfo == null)
			return;
		try {
			if (mCurMapInfo.getResIdDef() != -1) {
				mBitmaps[mCurClickIndex].recycle();
				mBitmaps[mCurClickIndex] = BitmapFactory.decodeResource(
						getResources(), mCurMapInfo.getResIdDef());
			}
		} catch (Exception e) {
			KJLoger.debug("eeee-->"+mBitmaps.length);
			KJLoger.debug("eeeecur-->"+mCurClickIndex);
		}

	}

	public synchronized void which(float x, float y) {
		if (mBitmaps != null) {
			for (int i = mBitmaps.length - 1; i >= 1; i--) {
				// 判断坐标点不超过图片得宽高
				if ((int) x > mBitmaps[0].getWidth()
						|| (int) y > mBitmaps[0].getHeight()||x<0||y<0) {
					// clickBitmapListener.ClickBitmap(-1);
					break;
				}
				Bitmap mBitmap = mBitmaps[i];
				// 判断坐标点是否是在图片得透明区域
				if (mBitmap != null && mBitmap.getPixel((int) x, (int) y) != 0) {
					mCurClickIndex = i;
					mCurMapInfo = mH.get(i);
					break;
				}
			}
		}
	}

	private synchronized void onClick(int du) {
		if (mCurClickIndex == -1 || mCurMapInfo == null)
			return;
		MapInfo mi = mH.get(mCurClickIndex);
		if (mi == null){
//			throw new RuntimeException("mapinfo could not be null");
			return;
		}
		OnClickListener lis = mi.getClickListener();
		if (du == 1 || du == 3) {
			if (mi.getResIdPress() != -1) {
				mBitmaps[mCurClickIndex].recycle();
				mBitmaps[mCurClickIndex] = BitmapFactory.decodeResource(
						getResources(), mi.getResIdPress());
			}
		
		} else if (du == 2) {
			if (mi.getResIdDef() != -1) {
				mBitmaps[mCurClickIndex].recycle();
				mBitmaps[mCurClickIndex] = BitmapFactory.decodeResource(
						getResources(), mi.getResIdDef());
			}
			if (lis != null && du == 2){
				lis.onClick(this);
				Toast.makeText(getContext(), mCurMapInfo.getName(),
						Toast.LENGTH_SHORT).show();
			}
			mCurClickIndex = -1;
			mCurMapInfo = null;
		}
		invalidate();
	}

	public void setOnDataInitListener(OnDataInitListener lis) {
		mOnDataInitListener = lis;
	}
}
