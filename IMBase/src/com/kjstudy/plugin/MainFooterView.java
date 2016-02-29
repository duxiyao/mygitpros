package com.kjstudy.plugin;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imbase.R;
import com.kjstudy.core.util.ViewUtil;

/**
 * @author duxiyao
 * @date 2015年9月1日
 * @description 首页下边的 ，【 首页 - 消息】
 */
@SuppressLint("NewApi")
public class MainFooterView extends LinearLayout implements OnClickListener {
	/**
	 * @author duxiyao
	 * @date 2015年9月1日
	 * @description 页面点击切换回调
	 */
	public interface OnItemClickListener {
		/**
		 * @date 2015年9月1日
		 * @author duxiyao
		 * @description 触发footer点击事件
		 * @param 索引
		 */
		void onFootItemClick(int clickItemIndex);
	}

	/**
	 * @date 2015年9月1日
	 * @author duxiyao
	 * @description footer渐变动画效果对应的控件
	 */
	private TextView mTv1, mTv2, mTv3, mTv4;
	/**
	 * @date 2015年9月1日
	 * @author duxiyao
	 * @description 回调
	 */
	private OnItemClickListener mOnItemClickListener;

	/**
	 * @date 2015年9月1日
	 * @author duxiyao
	 * @description 构造函数
	 * @param context
	 */
	public MainFooterView(Context context) {
		super(context);
	}

	/**
	 * @date 2015年9月1日
	 * @author duxiyao
	 * @description 构造函数
	 * @param context
	 * @param attrs
	 */
	public MainFooterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	/**
	 * @date 2015年9月1日
	 * @author duxiyao
	 * @description 初始化界面控件及事件
	 */
	private void initView() {
		if (isInEditMode())
			return;
		View v = LayoutInflater.from(getContext()).inflate(
				R.layout.layout_main_footer, this);
		mTv1 = ViewUtil.findView(v, R.id.tv_maplist);
		mTv2 = ViewUtil.findView(v, R.id.tv_intereast);
		mTv3 = ViewUtil.findView(v, R.id.tv_friends);
		mTv4 = ViewUtil.findView(v, R.id.tv_me);
		mTv1.setOnClickListener(this);
		mTv2.setOnClickListener(this);
		mTv3.setOnClickListener(this);
		mTv4.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v instanceof TextView) {
			int itemIndex = -1;
			switch (v.getId()) {
			case R.id.tv_maplist:
				itemIndex = 0;
				break;
			case R.id.tv_intereast:
				itemIndex = 1;
				break;
			case R.id.tv_friends:
				itemIndex = 2;
				break;
			case R.id.tv_me:
				itemIndex = 3;
				break;
			default:
				break;
			}
			if (mOnItemClickListener != null && itemIndex != -1)
				mOnItemClickListener.onFootItemClick(itemIndex);
		}
	}

	/**
	 * @date 2015年9月1日
	 * @author duxiyao
	 * @description 设置footer选项点击事件回调
	 * @param onItemClickListener
	 */
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.mOnItemClickListener = onItemClickListener;
	}
}
