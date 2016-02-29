package com.kjstudy.plugin.gesture_pwd;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.utils.DensityUtils;
import org.kymjs.kjframe.utils.ImgUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.imbase.R;
import com.kjstudy.plugin.gesture_pwd.GestureDrawline.GestureCallBack;

/**
 * 手势密码容器类
 * 
 */
public class GestureContentView extends ViewGroup {

	private int baseNum = 3;

	private int mScreenWidth;
	/**
	 * 声明一个集合用来封装坐标集合
	 */
	private List<GesturePoint> list;
	private Context context;
	private boolean isVerify;
	private GestureDrawline gestureDrawline;
	private int mGestureNodeNormal = R.drawable.gesture_node_normal;
	private int mGestureNodePressed = R.drawable.gesture_node_pressed;
	private int mGestureNodeWrong = R.drawable.gesture_node_wrong;

	private int mGap;
	private int mPadding;
	private int mChildWidth;

	/**
	 * 包含9个ImageView的容器，初始化
	 * 
	 * @param context
	 * @param isVerify
	 *            是否为校验手势密码
	 * @param passWord
	 *            用户传入密码
	 * @param gestureCallBack
	 *            手势绘制完毕的回调
	 */
	public GestureContentView(Context context, boolean isVerify,
			String passWord, GestureCallBack gestureCallBack) {
		super(context);
		mScreenWidth = DensityUtils.getScreenW();
//		mPadding = mScreenWidth / 6;
		mGap = mScreenWidth / 12;
		mPadding=mGap;
		mChildWidth = (mScreenWidth - 2 * mPadding - 2 * mGap) / 3;
		this.list = new ArrayList<GesturePoint>();
		this.context = context;
		this.isVerify = isVerify;
		// 添加9个图标
		addChild();
		// 初始化一个可以画线的view
		gestureDrawline = new GestureDrawline(context, list, isVerify,
				passWord, gestureCallBack);
		gestureDrawline.setDefaultLine(Color.rgb(31, 167, 240));
	}

	private void addChild() {
		for (int i = 0; i < 9; i++) {
			ImageView image = new ImageView(context);
			// image.setScaleType(ScaleType.CENTER_CROP);
			image.setImageBitmap(ImgUtil.drawable2Bitmap(mGestureNodeNormal));
			// image.setBackgroundResource(mGestureNodeNormal);
			this.addView(image);
			invalidate();
			// 第几行
			int row = i / 3;
			// 第几列
			int col = i % 3;
			// 定义点的每个属性
			int leftX = getChildLeft(col);
			int topY = getTop(row);
			int rightX = leftX + mChildWidth;
			int bottomY = topY + mChildWidth;
			GesturePoint p = new GesturePoint(leftX, rightX, topY, bottomY,
					image, i + 1);
			p.setGestureNodeNormal(mGestureNodeNormal);
			p.setGestureNodePressed(mGestureNodePressed);
			p.setGestureNodeWrong(mGestureNodeWrong);
			this.list.add(p);
		}
	}

	public void setParentView(final ViewGroup parent) {

		LayoutParams layoutParams = new LayoutParams(mScreenWidth, mChildWidth
				* 3 + 2 * mGap);
		// LayoutParams layoutParams = new LayoutParams(mScreenWidth,
		// mScreenWidth);
		setLayoutParams(layoutParams);
		gestureDrawline.setLayoutParams(layoutParams);
		if (parent.getChildCount() > 0)
			return;
		parent.addView(gestureDrawline);
		parent.addView(GestureContentView.this);

		parent.addOnLayoutChangeListener(new OnLayoutChangeListener() {

			@Override
			public void onLayoutChange(View v, int left, int top, int right,
					int bottom, int oldLeft, int oldTop, int oldRight,
					int oldBottom) {
				System.out.println("aaaaa");
				mScreenWidth = parent.getMeasuredWidth();
				GestureContentView.this.invalidate();
			}
		});
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
			int rightX = leftX + mChildWidth;
			int bottomY = topY + mChildWidth;
			v.layout(leftX, topY, rightX, bottomY);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 遍历设置每个子view的大小
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			v.measure(widthMeasureSpec, heightMeasureSpec);
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

	private int getChildLeft(int col) {
		return mPadding + col * mChildWidth + col * mGap;
	}

	private int getTop(int row) {
		return row * mChildWidth + row * mGap;
	}
}
