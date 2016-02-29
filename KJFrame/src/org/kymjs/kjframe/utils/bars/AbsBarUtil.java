package org.kymjs.kjframe.utils.bars;

import org.kymjs.kjframe.ui.AnnotateUtil;
import org.kymjs.kjframe.ui.KJActivityStack;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 
 * @author duxiyao
 * 
 */
public abstract class AbsBarUtil implements OnClickListener{

	private int mLayoutId = -1;
	protected View mView;
	protected OnClickListener mOnClickListener;
	
	public AbsBarUtil() {
		mLayoutId = getLayoutId();
		if (mLayoutId != -1) {
			mView = LayoutInflater.from(
					KJActivityStack.create().topActivity()
							.getApplicationContext()).inflate(mLayoutId, null);
			AnnotateUtil.FATHERNAME=AbsBarUtil.class.getName();
			AnnotateUtil.initBindView(this, mView);
		}
	}

	protected abstract int getLayoutId();

	public View getBarView() {
		return mView;
	}
	
	public void onClick(View v){
		if(mOnClickListener!=null)
			mOnClickListener.onClick(v);
	}
}
