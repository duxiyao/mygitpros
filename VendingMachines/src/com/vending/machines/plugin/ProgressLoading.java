package com.vending.machines.plugin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ProgressLoading extends View {
	private int pos = 0;
	private boolean flag = true;
	private int width;

	public ProgressLoading(Context context) {
		super(context);
	}

	public ProgressLoading(Context context, AttributeSet attrs) {
		super(context, attrs);
		run();
	}

	public ProgressLoading(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private void run() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (flag) {
					if (width == 0)
						width = getMeasuredWidth();
					pos += 5;
					if (width != 0 && pos > width) {

						pos = -500;
					}
					postInvalidate();
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(widthMeasureSpec, 15);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		RectF rect = new RectF(pos, 0, pos + 500, 15);
		canvas.drawRoundRect(rect, 25, 25, paint);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		flag = false;
	}
}
