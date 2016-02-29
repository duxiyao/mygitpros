package com.kjstudy.plugin.gesture_pwd;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.utils.ImgUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.imbase.R;
import com.kjstudy.plugin.gesture_pwd.GestureDrawline.GestureCallBack;

/**
 * 手势密码容器类
 * 
 */
public class GestureContentView1 extends ViewGroup {

	private int mWidth;
	private int mHeight;
	private int mPadding;
	private int mGap;
	private int mChildEdge;

	private List<GesturePoint> list;
	private GestureDrawline gestureDrawline;
	private int mGestureNodeNormal = R.drawable.gesture_node_normal1;
	private int mGestureNodePressed = R.drawable.gesture_node_pressed1;
	private int mGestureNodeWrong = R.drawable.gesture_node_wrong1;

	public GestureContentView1(Context context, AttributeSet attrs) {
		super(context, attrs);
		list = new ArrayList<GesturePoint>();
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.GestureLockView);
		mPadding = a.getDimensionPixelSize(
				R.styleable.GestureLockView_gesture_p, 0);
		mGap = a.getDimensionPixelSize(R.styleable.GestureLockView_gesture_gap,
				0);
		a.recycle();
		addChild();
	}

	public void setParams(String pwd, boolean isVerify, GestureCallBack cb) {
		gestureDrawline = new GestureDrawline(getContext(), list, isVerify,
				pwd, cb);
		gestureDrawline.setDefaultLine(Color.rgb(31, 167, 240));
		addView(gestureDrawline);
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 遍历设置每个子view的大小
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			if (v.equals(gestureDrawline))
				continue;
			v.measure(widthMeasureSpec, heightMeasureSpec);
		}
		mWidth = MeasureSpec.getSize(widthMeasureSpec);
		mChildEdge = (mWidth - 2 * mPadding - 2 * mGap) / 3;
		mHeight = mPadding * 2 + mGap * 2 + 3 * mChildEdge;
		if (gestureDrawline != null) {
			android.widget.RelativeLayout.LayoutParams lp = new android.widget.RelativeLayout.LayoutParams(
					mWidth, mHeight);
			gestureDrawline.setLayoutParams(lp);
			gestureDrawline.measure(widthMeasureSpec,
					MeasureSpec.makeMeasureSpec(mHeight, 1));
			gestureDrawline.setWh(mWidth, mHeight);
		}
		setMeasuredDimension(widthMeasureSpec,
				MeasureSpec.makeMeasureSpec(mHeight, 1));
		changexy();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		for (int i = 0; i < getChildCount(); i++) {
			// 第几行
			int row = i / 3;
			// 第几列
			int col = i % 3;
			View v = getChildAt(i);
			int leftX = getChildLeft(col);
			int topY = getTop(row);
			int rightX = leftX + mChildEdge;
			int bottomY = topY + mChildEdge;
			if (v.equals(gestureDrawline)) {
				// leftX = getChildLeft(0);
				// topY = getTop(0);
				leftX = 0;
				topY = 0;
				rightX = mWidth;
				bottomY = mHeight;
			}
			v.layout(leftX, topY, rightX, bottomY);
		}
	}

	/**
	 * 保留路径delayTime时间长
	 * 
	 * @param delayTime
	 */
	public void clearDrawlineState(long delayTime) {
		gestureDrawline.clearDrawlineState(delayTime);
	}

	public int getGestureNodeNormal() {
		return mGestureNodeNormal;
	}

	public void setGestureNodeNormal(int gestureNodeNormal) {
		this.mGestureNodeNormal = gestureNodeNormal;
		changeStyle();
	}

	public int getGestureNodePressed() {
		return mGestureNodePressed;
	}

	public void setGestureNodePressed(int gestureNodePressed) {
		this.mGestureNodePressed = gestureNodePressed;
		changeStyle();
	}

	public int getGestureNodeWrong() {
		return mGestureNodeWrong;
	}

	public void setGestureNodeWrong(int gestureNodeWrong) {
		this.mGestureNodeWrong = gestureNodeWrong;
		changeStyle();
	}

	private void changeStyle() {
		for (GesturePoint gp : list) {
			gp.setGestureNodeNormal(mGestureNodeNormal);
			gp.setGestureNodePressed(mGestureNodePressed);
			gp.setGestureNodeWrong(mGestureNodeWrong);
		}
	}

	private void changexy() {
		int len = list.size();
		for (int i = 0; i < len ; i++) {
			GesturePoint gp = list.get(i);
			int row = i / 3;
			// 第几列
			int col = i % 3;
			// 定义点的每个属性
			int leftX = getChildLeft(col);
			int topY = getTop(row);
			int rightX = leftX + mChildEdge;
			int bottomY = topY + mChildEdge;
			gp.setLeftX(leftX);
			gp.setTopY(topY);
			gp.setRightX(rightX);
			gp.setBottomY(bottomY);
			gp.resetCenterXy();
		}
	}

	private int getChildLeft(int col) {
		return mPadding + col * mChildEdge + col * mGap;
	}

	private int getTop(int row) {
		return mPadding + row * mChildEdge + row * mGap;
	}

	private void addChild() {
		for (int i = 0; i < 9; i++) {
			ImageView image = new ImageView(getContext());
			// image.setScaleType(ScaleType.CENTER_CROP);
			image.setImageBitmap(ImgUtil.drawable2Bitmap(mGestureNodeNormal));
			// image.setBackgroundResource(mGestureNodeNormal);
			this.addView(image);
			// 第几行
			int row = i / 3;
			// 第几列
			int col = i % 3;
			// 定义点的每个属性
			int leftX = getChildLeft(col);
			int topY = getTop(row);
			int rightX = leftX + mChildEdge;
			int bottomY = topY + mChildEdge;
			GesturePoint p = new GesturePoint(leftX, rightX, topY, bottomY,
					image, i + 1);
			p.setGestureNodeNormal(mGestureNodeNormal);
			p.setGestureNodePressed(mGestureNodePressed);
			p.setGestureNodeWrong(mGestureNodeWrong);
			this.list.add(p);
		}
	}
}
