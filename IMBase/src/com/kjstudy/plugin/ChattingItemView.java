package com.kjstudy.plugin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.imbase.R;
import com.kjstudy.core.util.ViewUtil;

public class ChattingItemView extends RelativeLayout {

	private ImageView mIvHeadLeft, mIvHeadRight;
	private LinearLayout mLlContent;
	private OnClickListener mOnClickable;

	public ChattingItemView(Context context) {
		super(context);
		init();
	}

	public ChattingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ChattingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void init() {
		View v = inflate(getContext(), R.layout.single_im_view, this);
		mIvHeadLeft = ViewUtil.findView(v, R.id.iv_head_left);
		mIvHeadRight = ViewUtil.findView(v, R.id.iv_head_right);
		mLlContent = ViewUtil.findView(v, R.id.ll_content);
		mIvHeadLeft.setVisibility(View.GONE);
		mIvHeadRight.setVisibility(View.GONE);
	}

	public void setContentView(View v) {
		mLlContent.removeAllViews();
		mLlContent.addView(v);
	}

	public void setHeadLeft() {
		mIvHeadLeft.setVisibility(View.VISIBLE);
		mIvHeadRight.setVisibility(View.GONE);
		mIvHeadLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mOnClickable != null) {
					mOnClickable.onClick(v);
				}
			}
		});
	}

	public void setHeadRight() {
		mIvHeadLeft.setVisibility(View.GONE);
		mIvHeadRight.setVisibility(View.VISIBLE);
		mIvHeadRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mOnClickable != null) {
					mOnClickable.onClick(v);
				}
			}
		});
	}

	public void setNoneHead() {
		mIvHeadLeft.setVisibility(View.GONE);
		mIvHeadRight.setVisibility(View.GONE);
	}

	public void setOnHeadClickable(OnClickListener lis) {
		mOnClickable = lis;
	}
}
