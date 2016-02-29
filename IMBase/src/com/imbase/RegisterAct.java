package com.imbase;

import java.util.Map;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.ActUtil;
import org.kymjs.kjframe.utils.PreferenceHelper;
import org.kymjs.kjframe.utils.StringUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kjstudy.bars.BarDefault;
import com.kjstudy.bean.ETSUserInfo;
import com.kjstudy.bean.data.TSUserInfo;
import com.kjstudy.bean.data.UserInfo;
import com.kjstudy.communication.CCPConfig;
import com.kjstudy.communication.SDKHelper;
import com.kjstudy.core.io.FileUtil;
import com.kjstudy.core.net.Req;
import com.kjstudy.core.util.CheckUtil;
import com.kjstudy.core.util.DBUtil;
import com.kjstudy.core.util.Global;
import com.kjstudy.core.util.IntentNameUtil;
import com.kjstudy.core.util.JsonUtil;

public class RegisterAct extends KJActivity {

	@BindView(id = R.id.et_account)
	private EditText mEtAccount;
	@BindView(id = R.id.et_pwd)
	private EditText mEtPwd;
	@BindView(id = R.id.tv_register, click = true)
	private TextView mTvRegister;
	private String mAccount, mPwd;
	private ProgressDialog mProgressDialog;

	@Override
	public void setRootView() {
		setContentView(R.layout.layout_register);
	}

	public void initWidget() {
		setFilters(IntentNameUtil.REGISTER_SUCCESS);
		BarDefault bar = new BarDefault();
		bar.setCenter(View.GONE);
		setCustomBar(bar.getBarView());
	}

	@Override
	protected void dealBroadcase(Intent intent) {
		super.dealBroadcase(intent);
		if (intent == null)
			return;
		if (IntentNameUtil.REGISTER_SUCCESS.equals(intent.getAction())) {
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_register:
			if (checkAccdPwd()) {
				showDialog();
				Req.getVerifyCode(mAccount, mPwd, new HttpCallBack() {
					@Override
					public void onSuccess(Map<String, String> headers, byte[] t) {
						String ret = new String(t);
						ViewInject.toast(String.valueOf(ret));
						ETSUserInfo user = JsonUtil.json2Obj(ret,
								ETSUserInfo.class);
						if (user != null && user.getCode() == 0) {
							ViewInject.toast("验证码已发送，请等待…");
							Bundle p = new Bundle();
							p.putString(VerifyCodeAct.PHONE, mAccount);
							p.putString(VerifyCodeAct.PWD, mPwd);
							ActUtil.startAct(VerifyCodeAct.class, p);
							overridePendingTransition(
									R.anim.sideslip_in_from_right,
									R.anim.sideslip_out_from_left);
						} else {
							if (user != null)
								ViewInject.toast(user.getMsg());
							dismissDialog();
						}
					}

					@Override
					public void onLoading(long count, long current) {
						showDialog();
					}

					@Override
					public void onFinish() {
						dismissDialog();
					}
				});
			}
			break;

		default:
			break;
		}
	}

	private boolean checkAccdPwd() {
		mAccount = mEtAccount.getText().toString();
		mPwd = mEtPwd.getText().toString();
		if (TextUtils.isEmpty(mAccount.trim())) {
			ViewInject.toast("账号不可以为空哦...");
			return false;
		}
		if (!CheckUtil.checkMDN(mAccount)) {
			ViewInject.toast("请输入正确的手机号好不啦...");
			return false;
		}
		if (TextUtils.isEmpty(mPwd.trim())) {
			ViewInject.toast("密码不可以为空哦...");
			return false;
		}
		return true;
	}

	private void showDialog() {
		if (mProgressDialog == null)
			mProgressDialog = ViewInject.getprogress(this,
					StringUtils.getResString(R.string.request_from_net), false);
		if (mProgressDialog != null && !mProgressDialog.isShowing())
			mProgressDialog.show();
	}

	private void dismissDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing())
			mProgressDialog.dismiss();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dismissDialog();
	}
}
