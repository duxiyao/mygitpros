package com.kjstudy.frag;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.ActUtil;

import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.imbase.LoginAct;
import com.imbase.R;
import com.kjstudy.act.UploadImageActivity;
import com.kjstudy.bean.data.TSUserInfo;
import com.kjstudy.core.util.Global;
import com.kjstudy.core.util.IntentNameUtil;
import com.kjstudy.plugin.CircleImageView;

public class TeacherFrag extends BFrag {

	@BindView(id = R.id.iv_head, click = true)
	private CircleImageView mCIVHead;
	@BindView(id = R.id.tv_name, click = true)
	private TextView mTvName;
	private final int LOGINSUCCESS = 1;
	private final int REGISTERSUCCESS = 2;
	private final int ONLASTUSERLOGIN = 3;
	private final int ONINITDATA = 4;

	@Override
	protected int getLayoutId() {
		return R.layout.layout_teacher_me;
	}

	@Override
	protected void initWidget() {
		super.initWidget();
		setFilters(IntentNameUtil.LOGIN_SUCCESS,
				IntentNameUtil.REGISTER_SUCCESS,
				IntentNameUtil.ON_LAST_USER_LOGIN);
		Message msg = getOsEmptyMsg();
		msg.what = ONINITDATA;
		sendMsg(msg);
	}

	@Override
	protected void dealBroadcase(Intent intent) {
		super.dealBroadcase(intent);
		String action = intent.getAction();
		if (IntentNameUtil.LOGIN_SUCCESS.equals(action)) {
			Message msg = getOsEmptyMsg();
			msg.what = LOGINSUCCESS;
			sendMsg(msg);
		} else if (IntentNameUtil.REGISTER_SUCCESS.equals(action)) {
			Message msg = getOsEmptyMsg();
			msg.what = REGISTERSUCCESS;
			sendMsg(msg);
		} else if (IntentNameUtil.ON_LAST_USER_LOGIN.equals(action)) {
			Message msg = getOsEmptyMsg();
			msg.what = ONLASTUSERLOGIN;
			sendMsg(msg);
		}
	}

	@Override
	protected void handleMsg(Message msg) {
		super.handleMsg(msg);
		switch (msg.what) {
		case LOGINSUCCESS:
		case REGISTERSUCCESS:
		case ONLASTUSERLOGIN:
		case ONINITDATA:
			TSUserInfo info = Global.getCURUSER();
			if (info != null) {
				String name = info.getName();
				if (TextUtils.isEmpty(name))
					name = "完善资料";
				mTvName.setText(name);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_head:
			ActUtil.startAct(UploadImageActivity.class);
			break;
		case R.id.tv_name:
			if (!Global.isLogining()) {
				ActUtil.startAct(LoginAct.class);
				getActivity().overridePendingTransition(
						R.anim.sideslip_in_from_right,
						R.anim.sideslip_out_from_left);
			}
			break;
		default:
			break;
		}
	}
}
