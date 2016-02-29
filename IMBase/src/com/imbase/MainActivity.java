//package com.imbase;
//
//import java.util.Map;
//
//import org.kymjs.kjframe.KJActivity;
//import org.kymjs.kjframe.KJDB;
//import org.kymjs.kjframe.http.HttpCallBack;
//import org.kymjs.kjframe.ui.BindView;
//import org.kymjs.kjframe.ui.ViewInject;
//import org.kymjs.kjframe.utils.KJLoger;
//import org.kymjs.kjframe.widget.KJViewPager;
//import org.kymjs.kjframe.widget.KJViewPager.OnViewChangeListener;
//
//import android.annotation.SuppressLint;
//import android.content.BroadcastReceiver;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.imbase.R;
//import com.kjstudy.act.Login;
//import com.kjstudy.bean.EUserInfo;
//import com.kjstudy.bean.data.UserInfo;
//import com.kjstudy.communication.CCPConfig;
//import com.kjstudy.communication.CCPHelper;
//import com.kjstudy.communication.CCPIntentUtils;
//import com.kjstudy.communication.DeviceHelper;
//import com.kjstudy.communication.UserData;
//import com.kjstudy.communication.CCPHelper.RegistCallBack;
//import com.kjstudy.core.io.FileAccessor;
//import com.kjstudy.core.net.Req;
//import com.kjstudy.core.util.DBUtil;
//import com.kjstudy.core.util.GUtil;
//import com.kjstudy.core.util.JsonUtil;
//
//@SuppressLint("NewApi")
//public class MainActivity extends KJActivity {
//
//	@BindView(id = R.id.vp_main)
//	private KJViewPager mVp;
//
//	@BindView(id = R.id.et_name)
//	private EditText etName;
//
//	@BindView(id = R.id.et_pwd)
//	private EditText etPwd;
//
//	@BindView(id = R.id.tv_login, click = true)
//	private TextView tvLogin;
//
//	@BindView(id = R.id.page_login)
//	private LinearLayout llLogin;
//
//	@BindView(id = R.id.tv1, click = true)
//	private TextView tv1;
//
//	@Override
//	public void setRootView() {
//		setContentView(R.layout.activity_main);
//	}
//
//	@Override
//	public void initWidget() {
//		super.initWidget();
//		if (!DeviceHelper.getInstance().isOnline()) {
//			skipActivity(this, Login.class);
//			return;
//		}
//		mVp.setOnViewChangeListener(new OnViewChangeListener() {
//
//			@Override
//			public void OnViewChange(int view) {
//				KJLoger.debug("vc-->" + view);
//			}
//		});
//		setFilters(CCPIntentUtils.INTENT_KICKEDOFF);
//	}
//
//	@Override
//	protected void dealBroadcase(Intent intent) {
//		if (CCPIntentUtils.INTENT_KICKEDOFF.equals(intent.getAction())) {
//			llLogin.setVisibility(View.VISIBLE);
//		}
//	}
//
//	@Override
//	public void widgetClick(View v) {
//		super.widgetClick(v);
//		switch (v.getId()) {
//		case R.id.tv_login:
//			ViewInject.toast(String.valueOf("on click"));
//			login();
//			// tvLogin.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
//			break;
//		case R.id.tv1:
//			boolean f = DeviceHelper.getInstance().isOnline();
//			String str = "";
//			break;
//		default:
//			break;
//		}
//	}
//
//	private void login() {
//		String name = etName.getText().toString();
//		String pwd = etPwd.getText().toString();
//		if (GUtil.isEmpty(name)) {
//			ViewInject.toast("用户名不能为空!");
//			return;
//		}
//		if (GUtil.isEmpty(pwd)) {
//			ViewInject.toast("密码不能为空!");
//			return;
//		}
//		KJLoger.debug("name=" + name + "  pwd=" + pwd);
//		name = "1589878326@qq.com";
//		pwd = "111111";
//		Req.login(name, pwd, new HttpCallBack() {
//			@Override
//			public void onSuccess(Map<String, String> headers, byte[] t) {
//				String ret = new String(t);
//				ViewInject.toast(String.valueOf(ret));
//				EUserInfo user = JsonUtil.json2Obj(ret, EUserInfo.class);
//				if (user.getCode() == 0 && user.getData() != null) {
//					UserInfo u = user.getData();
//					if (!DBUtil.getDB().isExists(UserInfo.class,
//							"email='" + u.getEmail() + "'")) {
//						DBUtil.save(u);
//					}
//					CCPConfig.VoIP_ID = u.getVoipAccount();
//					CCPConfig.VoIP_PWD = u.getVoipPwd();
//					CCPConfig.Sub_Account = u.getSubAccountSid();
//					CCPConfig.Sub_Token = u.getSubToken();
//
//					CCPHelper.getInstance().registerCCP(new RegistCallBack() {
//
//						@Override
//						public void onRegistResult(int reason, String msg) {
//							if (reason == CCPHelper.WHAT_ON_CONNECT) {
//								llLogin.setVisibility(View.GONE);
//								ViewInject.toast(String
//										.valueOf("register ccp success"));
//								DeviceHelper.getInstance().sendText(
//										"86920000000025", "aa", new UserData());
//							} else {
//								ViewInject.toast(String
//										.valueOf("register ccp failed"));
//							}
//							ViewInject.toast(msg);
//						}
//					});
//				} else {
//					ViewInject.toast(user.getMsg());
//				}
//			}
//		});
//	}
//}
