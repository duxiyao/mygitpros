package com.kjstudy.frag;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class BaseFragment {
//	private CommunicationUI mCommunicationUI;
//	private ContactsListUI mContactsListUI;
	private Fragment mCurFragment;
	private FragmentManager mManager;
	private FragmentTransaction mTransaction;

//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
		
//		View v = inflater.inflate(R.layout.frag_layout_main_msg, container,
//				false);
//		return v;
//	}

//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//		mCommunicationUI = new CommunicationUI();
//		mContactsListUI = new ContactsListUI();
//		mCurFragment = mCommunicationUI;;
//		showFragment();
//	}

//	public void showMsgFragment() {
//		mCurFragment = mCommunicationUI;
//		showFragment();
//	}

//	public void showAttentionFragment() {
//		mCurFragment = mContactsListUI;
//		showFragment();
//	}

//	private void showFragment() {
//		if (mManager == null)
//			mManager = getChildFragmentManager();
//		mTransaction = mManager.beginTransaction();
//		if (mCurFragment != null) {
//			mTransaction.replace(R.id.msg_attention_container, mCurFragment);
//		}
//
//		mTransaction.commitAllowingStateLoss();
//	}

}
