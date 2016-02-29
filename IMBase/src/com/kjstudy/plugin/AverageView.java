package com.kjstudy.plugin;

import java.util.List;

import org.kymjs.kjframe.ui.ViewInject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author duxiyao
 * @date 2015年11月2日
 * @description 均匀排列的控件
 */
public class AverageView extends ViewGroup {

	public interface OnAddAllViews {
		void onAddAllViews(List<? extends View> vs);
	}

	private int mHeight, mChildWidthMeasureSpec, mChildHeightMeasureSpec;
	private List<? extends View> mVs;
	private int mGap = 10;
	private boolean isSquare = false;
	private int mRowCount = 3;
	private OnAddAllViews mOnAddAllViews;

	public AverageView(Context context) {
		super(context);
	}

	public AverageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AverageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void setOnAddAllViews(OnAddAllViews lis) {
		mOnAddAllViews = lis;
	}

	/**
	 * @date 2015年11月2日
	 * @author duxiyao
	 * @description 要显示的view
	 * @param vs
	 * @param rowCount
	 *            每行的个数
	 * @param isSquare
	 *            子view的宽高是否相同
	 */
	public void setViews(List<? extends View> vs, int rowCount, boolean isSquare) {
		removeAllViews();
		this.isSquare = isSquare;
		this.mRowCount = rowCount;
		this.mVs = vs;
		if (mVs != null) {
			mVs.get(mVs.size() - 1).addOnLayoutChangeListener(
					new OnLayoutChangeListener() {

						@Override
						public void onLayoutChange(View v, int left, int top,
								int right, int bottom, int oldLeft, int oldTop,
								int oldRight, int oldBottom) {
							if (mOnAddAllViews != null)
								mOnAddAllViews.onAddAllViews(mVs);
						}
					});
			for (View v : mVs)
				addView(v);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		measureChildWidthHeight(widthMeasureSpec, heightMeasureSpec);
		if (mVs != null) {
			int len = mVs.size();
			for (int i = 0; i < len; i++) {
				View v = getChildAt(i);
				v.measure(mChildWidthMeasureSpec, mChildHeightMeasureSpec);
			}
		}

		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mHeight);
	}

	private void measureChildWidthHeight(int widthMeasureSpec,
			int heightMeasureSpec) {
		if (mVs != null) {
			int len = mVs.size();
			if (len > 0) {
				View v = getChildAt(0);
				v.measure(widthMeasureSpec, heightMeasureSpec);
				int w = v.getMeasuredWidth();
				int h = v.getMeasuredHeight();
				// mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
				// (w - (mRowCount + 1) * mGap) / mRowCount, 1);
				mChildWidthMeasureSpec = MeasureSpec
						.makeMeasureSpec(
								(MeasureSpec.getSize(widthMeasureSpec) - (mRowCount + 1)
										* mGap)
										/ mRowCount, 1);

				if (isSquare)
					mChildHeightMeasureSpec = mChildWidthMeasureSpec;
				else
					mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(h, 1);
				v.measure(mChildWidthMeasureSpec, mChildHeightMeasureSpec);
				int row = len / mRowCount;
				if (len % mRowCount != 0)
					row += 1;
				if (mHeight == 0)
					mHeight = (MeasureSpec.getSize(mChildHeightMeasureSpec))
							* row + (row + 1) * mGap;
			}
		}

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View v = getChildAt(i);

			int measureHeight = 0;
			if (isSquare)
				measureHeight = mChildHeightMeasureSpec;
			else
				measureHeight = v.getMeasuredHeight();
			int measuredWidth = mChildWidthMeasureSpec;// v.getMeasuredWidth();
			int curRow = i / mRowCount + 1;
			int curCol = i % mRowCount + 1;

			int x = curCol * mGap + measuredWidth * (curCol - 1);
			int y = mGap * curRow + (curRow - 1) * measureHeight;
			v.layout(x, y, x + measuredWidth, y + measureHeight);
		}
	}
}
