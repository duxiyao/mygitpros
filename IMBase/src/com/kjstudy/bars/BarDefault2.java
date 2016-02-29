package com.kjstudy.bars;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.KJActivityStack;
import org.kymjs.kjframe.utils.bars.AbsBarUtil;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.imbase.R;

public class BarDefault2 extends AbsBarUtil {

	@BindView(id = R.id.iv_back, click = true)
	private ImageView mIvBack;
	@BindView(id = R.id.tv_confirm, click = true)
	private TextView mTv;

	@Override
	protected int getLayoutId() {
		return R.layout.actionbar_default2;
	}

	public void setOnClickLis(OnClickListener lis) {
		mOnClickListener = lis;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.iv_back:
			KJActivityStack.create().topActivity().finish();
			break;

		default:
			break;
		}
	}
}
