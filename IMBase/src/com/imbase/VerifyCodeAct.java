package com.imbase;

import java.util.Map;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kjstudy.bars.BarDefault;
import com.kjstudy.bean.ETSUserInfo;
import com.kjstudy.bean.data.TSUserInfo;
import com.kjstudy.core.net.Req;
import com.kjstudy.core.util.BroadCastUtil;
import com.kjstudy.core.util.DBUtil;
import com.kjstudy.core.util.Global;
import com.kjstudy.core.util.IntentNameUtil;
import com.kjstudy.core.util.JsonUtil;

public class VerifyCodeAct extends KJActivity {

	public static final String PHONE = "VerifyCodeAct.phone";
	public static final String PWD = "VerifyCodeAct.pwd";
	private String mAccount, mPwd;
	private ProgressDialog mProgressDialog;

	@BindView(id = R.id.et_verify_code)
	private EditText mEtVerifyCode;
	@BindView(id = R.id.tv_verify, click = true)
	private TextView mTvVerify;

	@Override
	public void setRootView() {
		setContentView(R.layout.layout_verify_code);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		mAccount = getIntent().getStringExtra(PHONE);
		mPwd = getIntent().getStringExtra(PWD);
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
		case R.id.tv_verify:
			String vc = mEtVerifyCode.getText().toString().trim();
			if (TextUtils.isEmpty(vc)) {
				ViewInject.toast("验证码呢...");
				return;
			}
			showDialog();
			Req.verifyCode(mAccount, mPwd, vc, new HttpCallBack() {
				@Override
				public void onSuccess(Map<String, String> headers, byte[] t) {
					String ret = new String(t);
					ViewInject.toast(String.valueOf(ret));
					ETSUserInfo user = JsonUtil
							.json2Obj(ret, ETSUserInfo.class);
					if (user != null && user.getCode() == 0
							&& user.getData() != null) {
						TSUserInfo u = user.getData();
						if (!DBUtil.getDB().isExists(TSUserInfo.class,
								"phone='" + u.getPhone() + "'")) {
							DBUtil.save(u);
						} else {
							DBUtil.update(u, "phone='" + u.getPhone() + "'");
						}
						Global.setCURUSER(u);
						ViewInject.toast("注册成功！！");
						BroadCastUtil
								.sendBroadCast(IntentNameUtil.REGISTER_SUCCESS);
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
			break;

		default:
			break;
		}
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