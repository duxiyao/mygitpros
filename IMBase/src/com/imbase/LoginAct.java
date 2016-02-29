package com.imbase;

import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.ActUtil;
import org.kymjs.kjframe.utils.KJLoger;
import org.kymjs.kjframe.utils.StringUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kjstudy.bars.BarDefault;
import com.kjstudy.bean.ETSUserInfo;
import com.kjstudy.bean.data.TSUserInfo;
import com.kjstudy.core.net.Req;
import com.kjstudy.core.util.CheckUtil;
import com.kjstudy.core.util.DBUtil;
import com.kjstudy.core.util.Global;
import com.kjstudy.core.util.IntentNameUtil;
import com.kjstudy.core.util.JsonUtil;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;

public class LoginAct extends KJActivity {

	@BindView(id = R.id.tv_register, click = true)
	private TextView mTvRegister;
	@BindView(id = R.id.tv_login, click = true)
	private TextView mTvLogin;
	@BindView(id = R.id.et_account)
	private EditText mEtAccount;
	@BindView(id = R.id.et_pwd)
	private EditText mEtPwd;
	@BindView(id = R.id.iv_qq, click = true)
	private ImageView mIvQQ;
	private String mAccount, mPwd;
	private ProgressDialog mProgressDialog;
	private Tencent mTencent;

	@Override
	public void setRootView() {
		setContentView(R.layout.layout_login);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		setFilters(IntentNameUtil.REGISTER_SUCCESS,
				IntentNameUtil.LOGIN_SUCCESS);
		BarDefault bar = new BarDefault();
		bar.setCenter(View.GONE);
		setCustomBar(bar.getBarView());
		bar.setOnClickLis(this);
	}

	@Override
	protected void dealBroadcase(Intent intent) {
		super.dealBroadcase(intent);
		if (intent == null)
			return;
		String action = intent.getAction();
		if (IntentNameUtil.REGISTER_SUCCESS.equals(action)
				|| IntentNameUtil.LOGIN_SUCCESS.equals(action)) {
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.iv_qq:
			qq();
			break;
		case R.id.tv_register:
			ActUtil.startAct(RegisterAct.class);
			overridePendingTransition(R.anim.sideslip_in_from_right,
					R.anim.sideslip_out_from_left);
			break;
		case R.id.tv_login:
			if (check()) {
				showDialog();
				Req.loginTS(mAccount, mPwd, new HttpCallBack() {
					@Override
					public void onSuccess(Map<String, String> headers, byte[] t) {
						String ret = new String(t);
						ViewInject.toast(String.valueOf(ret));
						ETSUserInfo user = JsonUtil.json2Obj(ret,
								ETSUserInfo.class);
						if (user != null && user.getCode() == 0
								&& user.getData() != null) {
							TSUserInfo u = user.getData();
							u.setPwd(mPwd);
							if (!DBUtil.getDB().isExists(TSUserInfo.class,
									"phone='" + u.getPhone() + "'")) {
								DBUtil.save(u);
							} else {
								DBUtil.update(u, "phone='" + u.getPhone() + "'");
							}
							Global.setCURUSER(u);
							ViewInject.toast("登录成功！！");
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

	private boolean check() {
		mAccount = mEtAccount.getText().toString();
		mPwd = mEtPwd.getText().toString();
		if (TextUtils.isEmpty(mAccount.trim())) {
			ViewInject.toast("账号不可以为空哦...");
			return false;
		}
		if (!CheckUtil.checkMDN(mAccount)) {
			ViewInject.toast("输入正确的手机号好不啦...");
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

	private void qq() {
		if (mTencent == null)
			mTencent = Tencent.createInstance("1104917096",
					this.getApplicationContext());

		if (!mTencent.isSessionValid()) {
			mTencent.login(this, "all", new IUiListener() {

				@Override
				public void onError(UiError arg0) {
					String str = "";
				}

				@Override
				public void onComplete(Object arg0) {
					final String qqOpenId = JsonUtil.getString(
							((JSONObject) arg0), "openid");
					ViewInject.toast(qqOpenId);
					UserInfo info = new UserInfo(LoginAct.this, mTencent
							.getQQToken());
					info.getUserInfo(new IUiListener() {

						@Override
						public void onError(UiError arg0) {
							String str = "";
						}

						@Override
						public void onComplete(Object arg0) {
							String figureurl_qq_1 = JsonUtil.getString(
									((JSONObject) arg0), "figureurl_qq_1");
							String figureurl_qq_2 = JsonUtil.getString(
									((JSONObject) arg0), "figureurl_qq_2");
							String nickname = JsonUtil.getString(
									((JSONObject) arg0), "nickname");
							String gender = JsonUtil.getString(
									((JSONObject) arg0), "gender");
							showDialog();
							Req.loginTSByQQ(qqOpenId, nickname, figureurl_qq_2,
									gender, new HttpCallBack() {
										@Override
										public void onSuccess(
												Map<String, String> headers,
												byte[] t) {
											String ret = new String(t);
											ViewInject.toast(String
													.valueOf(ret));
											ETSUserInfo user = JsonUtil
													.json2Obj(ret,
															ETSUserInfo.class);
											if (user != null
													&& user.getCode() == 0
													&& user.getData() != null) {
												TSUserInfo u = user.getData();
												if (!DBUtil
														.getDB()
														.isExists(
																TSUserInfo.class,
																"qqOpenId='"
																		+ u.getQqOpenId()
																		+ "'")) {
													DBUtil.save(u);
												} else {
													DBUtil.update(
															u,
															"qqOpenId='"
																	+ u.getQqOpenId()
																	+ "'");
												}
												Global.setCURUSER(u, "qqOpenId");
												ViewInject.toast("登录成功！！");
												finish();
											} else {
												ViewInject.toast(user.getMsg());
												dismissDialog();
											}
										}

										@Override
										public void onLoading(long count,
												long current) {
											showDialog();
										}

										@Override
										public void onFinish() {
										}
									});
							ViewInject.toast(figureurl_qq_1);
						}

						@Override
						public void onCancel() {
							String str = "";
						}
					});
				}

				@Override
				public void onCancel() {
					String str = "";
				}
			});
		} else {
			ViewInject.toast("您已登录！！");
		}

		// UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1104917096",
		// "cTbIQKW7HxmZlSne");
		// qqSsoHandler.addToSocialSDK();
		//
		// final UMSocialService mController = UMServiceFactory
		// .getUMSocialService("com.umeng.login");
		//
		// mController.doOauthVerify(this, SHARE_MEDIA.QQ, new UMAuthListener()
		// {
		//
		// @Override
		// public void onCancel(SHARE_MEDIA arg0) {
		// }
		//
		// @Override
		// public void onComplete(Bundle value, SHARE_MEDIA platform) {
		//
		// String str = "";
		// if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
		// // ViewInject.toast("授权成功.");
		// // getData(mController);
		// } else {
		// // ViewInject.toast("授权失败");
		// }
		// }
		//
		// @Override
		// public void onError(SocializeException arg0, SHARE_MEDIA arg1) {
		// }
		//
		// @Override
		// public void onStart(SHARE_MEDIA arg0) {
		// }
		// });
	}

	private void getData(final UMSocialService controller) {
		controller.getPlatformInfo(this, SHARE_MEDIA.QQ, new UMDataListener() {

			@Override
			public void onStart() {
				ViewInject.toast("获取平台数据开始...");
			}

			@Override
			public void onComplete(int status, Map<String, Object> info) {
				if (status == 200 && info != null) {
					StringBuilder sb = new StringBuilder();
					Set<String> keys = info.keySet();
					for (String key : keys) {
						sb.append(key + "=" + info.get(key).toString() + "\r\n");
					}
					KJLoger.debug("TestData-->" + sb.toString());
					// deleteData(controller);
				} else {
					KJLoger.debug("TestData-->" + "发生错误：" + status);
				}
			}
		});
	}

	private void deleteData(UMSocialService controller) {
		controller.deleteOauth(this, SHARE_MEDIA.QQ,
				new SocializeClientListener() {

					@Override
					public void onStart() {
					}

					@Override
					public void onComplete(int status, SocializeEntity arg1) {
						if (status == 200) {
							ViewInject.toast("删除成功.");
						} else {
							ViewInject.toast("删除失败");
						}
					}
				});
	}

}
