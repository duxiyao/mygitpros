package com.kjstudy.test.view;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.utils.KJLoger;

import com.imbase.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.TextView;

public class ZndzRet extends ViewGroup {

	public ZndzRet(Context context) {
		super(context);
	}

	public ZndzRet(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ZndzRet(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	private List<View> l;

	void init() {
		if (l == null) {
			l = new ArrayList<View>();
			TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(
					R.layout.im_textview, null);
			tv.setText("aaaa");
			tv.setBackgroundColor(Color.RED);
			l.add(tv);
			addView(tv);
			tv = (TextView) LayoutInflater.from(getContext()).inflate(
					R.layout.im_textview, null);
			tv.setText("aaaa");
			tv.setBackgroundColor(Color.BLACK);
			l.add(tv);
			addView(tv);
			tv = (TextView) LayoutInflater.from(getContext()).inflate(
					R.layout.im_textview, null);
			tv.setText("aaaa");
			tv.setBackgroundColor(Color.BLUE);
			l.add(tv);
			addView(tv);
			tv = (TextView) LayoutInflater.from(getContext()).inflate(
					R.layout.im_textview, null);
			tv.setText("aaaa");
			l.add(tv);
			addView(tv);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		init();
		int mode=MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		KJLoger.debug("widthSize-->" + widthSize + "    heightSize-->"
				+ heightSize);
		KJLoger.debug("cl" + getChildCount());
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// measureChildren(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(widthSize, heightSize);
		int len = l.size();
		int childWidth = widthSize / len;
		for (int i = 0; i < len; i++) {
			l.get(i).measure(MeasureSpec.makeMeasureSpec(childWidth, mode), heightMeasureSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int len = getChildCount();
		int w = 0;
		for (int i = 0; i < len; i++) {
			View childView = getChildAt(i);

			// 获取在onMeasure中计算的视图尺寸
			int measureHeight = childView.getMeasuredHeight();
			int measuredWidth = childView.getMeasuredWidth();

			childView.layout(w, 0, w + measuredWidth, measureHeight);
			w += measuredWidth;
		}
	}

}
