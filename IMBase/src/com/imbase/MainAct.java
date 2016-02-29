package com.imbase;

import java.util.ArrayList;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.KJLoger;
import org.kymjs.kjframe.widget.KJViewPager;
import org.kymjs.kjframe.widget.KJViewPager.OnViewChangeListener;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kjstudy.communication.CCPIntentUtils;
import com.kjstudy.communication.SDKHelper;
import com.kjstudy.plugin.MainFooterView;
import com.zbar.lib.SCQrcodeRetAct;

public class MainAct extends KJActivity {

	@BindView(id = R.id.vp_main)
	private KJViewPager mVp;

	@BindView(id = R.id.et_name)
	private EditText etName;

	@BindView(id = R.id.et_pwd)
	private EditText etPwd;

	@BindView(id = R.id.tv_login, click = true)
	private TextView tvLogin;

	@BindView(id = R.id.page_login)
	private LinearLayout llLogin;

	@BindView(id = R.id.tv1, click = true)
	private TextView tv1;

	private ActionBarAssistant mActionBarAssistant;
	private MainFragmentAdapter mFragAdapter;
	private MainFooterView mMainFooterView;
	private SCQrcodeRetAct mSCQrcodeRetAct;// 扫描类
//	private MsgFragment mMsgFragment;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_main);
	}

	@Override
	public void initWidget() {
		super.initWidget();
//		if (!SDKHelper.getInstance().isOnline()) {
//			skipActivity(this, Login.class);
//			return;
//		}
		mVp.setOnViewChangeListener(new OnViewChangeListener() {

			@Override
			public void OnViewChange(int view) {
				KJLoger.debug("vc-->" + view);
			}
		});
//		final HomePageFragment fragHome = new HomePageFragment();
//		mMsgFragment = new MsgFragment();
//
//		ArrayList<Fragment> frags = new ArrayList<Fragment>();
//		frags.add(fragHome);
//		frags.add(mMsgFragment);
//		mFragAdapter = new MainFragmentAdapter(getSupportFragmentManager(),
//				frags);
//		mVp.setAdapter(mFragAdapter);
//
//		mMainFooterView = (MainFooterView) findViewById(R.id.custom_footer);
//		mMainFooterView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onFootItemClick(int clickItemIndex) {
//				mVp.setCurrentItem(clickItemIndex);
//			}
//		});
//		mVp.addOnPageChangeListener(new OnPageChangeListener() {
//
//			@Override
//			public void onPageSelected(int pos) {
//				if (pos == 0) {
//					mActionBarAssistant.selectHomePageBar();
//					mMainFooterView.selectHomePage();
//				} else if (pos == 1) {
//					mActionBarAssistant.selectMsgPageBar();
//					mMainFooterView.selectMsgPage();
//				}
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//			}
//		});
//		mMainFooterView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
//
//			@Override
//			public void onLayoutChange(View v, int left, int top, int right,
//					int bottom, int oldLeft, int oldTop, int oldRight,
//					int oldBottom) {
//				if (oldBottom == 0) {
//					int barHeight = mActionBarAssistant.getActionbar()
//							.getMeasuredHeight();
//					LayoutParams lp = mVp.getLayoutParams();
//					int tmp = getWindow().getDecorView().getHeight();
//					lp.height = /* DensityUtil.getScreenHeight() */getWindow()
//							.getDecorView().getHeight()
//							- barHeight
//							- bottom
//							- DensityUtil.getStatusBarHeight(getWindow());
//					mVp.setLayoutParams(lp);
//				}
//			}
//		});
//
//		registerReceiver(new String[] { CASIntent.INTETN_KICKOFF,
//				CASIntent.INTENT_DISCONNECT_CCP, 
//				SLDIntent.ACTION_ACCOUNT_LOGOUT });
//		setFilters(CCPIntentUtils.INTENT_KICKEDOFF);
	}

	@Override
	protected void dealBroadcase(Intent intent) {
		if (CCPIntentUtils.INTENT_KICKEDOFF.equals(intent.getAction())) {
			llLogin.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.tv_login:
			ViewInject.toast(String.valueOf("on click"));
			// tvLogin.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
			break;
		case R.id.tv1:
			break;
		default:
			break;
		}
	}

	/**
	 * @author duxiyao
	 * @description 主界面几个fragment的adapter
	 */
	class MainFragmentAdapter extends FragmentPagerAdapter {

		/**
		 * @author duxiyao
		 * @description 主界面要用的fragments
		 */
		private ArrayList<Fragment> mFrags;

		/**
		 * @author duxiyao
		 * @description 构造函数
		 * @param fm
		 * @param frags
		 */
		public MainFragmentAdapter(FragmentManager fm, ArrayList<Fragment> frags) {
			super(fm);
			this.mFrags = frags;
		}

		@Override
		public Fragment getItem(int pos) {
			return mFrags.get(pos);
		}

		@Override
		public int getCount() {
			return mFrags.size();
		}

	}

	class ActionBarAssistant implements OnClickListener {
		/**
		 * @date 2015年9月1日
		 * @author duxiyao
		 * @description 首页bar和消息bar
		 */
		private View mHomePageBar, mMsgPageBar, mCur;
		private ImageView mIvSetting, mIvMsg, mIvAttention, mIvQrScan;

		/**
		 * @date 2015年9月1日
		 * @author duxiyao
		 * @description 构造函数，不同的bar
		 * @param homepageBar
		 * @param msgpageBar
		 */
		public ActionBarAssistant(View homepageBar, View msgpageBar) {
			mHomePageBar = homepageBar;
			mMsgPageBar = msgpageBar;
			mCur = mHomePageBar;
//			mIvSetting = (ImageView) homepageBar
//					.findViewById(R.id.iv_personal_setting);
//			mIvMsg = (ImageView) msgpageBar.findViewById(R.id.iv_homebar_msg);
//			mIvAttention = (ImageView) msgpageBar
//					.findViewById(R.id.iv_homebar_attention);
//			mIvQrScan = (ImageView) msgpageBar.findViewById(R.id.iv_qrscan);
			mIvMsg.setOnClickListener(this);
			mIvAttention.setOnClickListener(this);
			mIvQrScan.setOnClickListener(this);
			mIvSetting.setOnClickListener(this);
		}

		/**
		 * @date 2015年9月1日
		 * @author duxiyao
		 * @description 获得当前的actionbar
		 * @return
		 */
		public View getActionbar() {
			return mCur;
		}

		/**
		 * @date 2015年9月1日
		 * @author duxiyao
		 * @description 选择首页actionbar
		 */
		public void selectHomePageBar() {
			mCur = mHomePageBar;
//			showActionbar(true);
		}

		/**
		 * @date 2015年9月1日
		 * @author duxiyao
		 * @description 选择消息页面actionbar
		 */
		public void selectMsgPageBar() {
			mCur = mMsgPageBar;
//			showActionbar(true);
		}

		@Override
		public void onClick(View v) {
//			switch (v.getId()) {
//			case R.id.iv_personal_setting:
//				SCIntent.startActivity(HospitalMainUI.this, PersonalUI.class);
//				break;
//			case R.id.iv_qrscan:
//				mSCQrcodeRetAct = new SCQrcodeRetAct(HospitalMainUI.this);
//				mSCQrcodeRetAct.startActForRet();
//				break;
//			case R.id.iv_homebar_msg:
//				resetBg();
//				mIvMsg.setBackgroundResource(R.drawable.homebar_msg_s);
//				if (mMsgFragment != null)
//					mMsgFragment.showMsgFragment();
//				break;
//			case R.id.iv_homebar_attention:
//				resetBg();
//				mIvAttention
//						.setBackgroundResource(R.drawable.homebar_attention_s);
//				mIvQrScan.setVisibility(View.VISIBLE);
//				if (mMsgFragment != null)
//					mMsgFragment.showAttentionFragment();
//				break;
//			default:
//				break;
//			}
		}

		private void resetBg() {
//			mIvMsg.setBackgroundResource(R.drawable.homebar_msg_uns);
//			mIvAttention
//					.setBackgroundResource(R.drawable.homebar_attention_uns);
//			mIvQrScan.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		mSCQrcodeRetAct.onActivityResult(arg0, arg1, arg2);
	}
}
