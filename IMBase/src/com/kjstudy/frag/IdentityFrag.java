package com.kjstudy.frag;

import com.imbase.R;
import com.kjstudy.bars.BarDefault;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class IdentityFrag extends BFrag {

	private TeacherFrag mTeacherFrag;
	private StudentFrag mStudentFrag;
	private Fragment mCurFrag;
	private FragmentManager mManager;
	private FragmentTransaction mTransaction;
	private BarDefault mBar;
	private static int mIndex = -1;

	@Override
	protected int getLayoutId() {
		return R.layout.frag_layout_main;
	}

	@Override
	protected void initWidget() {
		super.initWidget();

		mTeacherFrag = new TeacherFrag();
		mStudentFrag = new StudentFrag();
		if (mIndex==-1||mIndex==1)
			showStuFrag();
		else
			showTerFrag();
		mBar = new BarDefault();
		mBar.setTxt(1, "学生");
		mBar.setTxt(2, "老师");
		mBar.setOnClickLis(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.tv1:
					showStuFrag();
					break;
				case R.id.tv2:
					showTerFrag();
					break;
				default:
					break;
				}
			}
		});
		setCustomBar(mBar.getBarView());
	}

	private void showFragment() {
		if (mManager == null)
			mManager = getChildFragmentManager();
		mTransaction = mManager.beginTransaction();
		if (mCurFrag != null) {
			mTransaction.replace(R.id.msg_attention_container, mCurFrag);
		}

		mTransaction.commitAllowingStateLoss();
	}

	public void showStuFrag() {
		mIndex=1;
		mCurFrag = mStudentFrag;
		showFragment();
	}

	public void showTerFrag() {
		mIndex=2;
		mCurFrag = mTeacherFrag;
		showFragment();
	}
}
