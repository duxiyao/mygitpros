package com.kjstudy.act;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.imbase.R;
import com.kjstudy.bars.BarDefault2;
import com.kjstudy.bean.Entity;
import com.kjstudy.bean.data.TSUserInfo;
import com.kjstudy.core.net.Req;
import com.kjstudy.core.util.BroadCastUtil;
import com.kjstudy.core.util.Global;
import com.kjstudy.core.util.IntentNameUtil;
import com.kjstudy.core.util.JsonUtil;

public class InfoEditAct extends KJActivity {

	public static final String KEY = "InfoEditAct.key";
	public static final String HINTVALUE = "InfoEditAct.hintvalue";
	public static final String INTTYPE = "InfoEditAct.inttype";

	@BindView(id = R.id.tv_subject)
	private TextView mTvName;
	@BindView(id = R.id.et_content)
	private EditText mEtContent;
	private String mKey;
	private int intType = -1;

	@Override
	public void setRootView() {
		setContentView(R.layout.layout_personal_info_edit);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		BarDefault2 bar = new BarDefault2();
		bar.setOnClickLis(this);
		setCustomBar(bar.getBarView());
		mKey = getIntent().getStringExtra(KEY);
		if ("a.age".equals(mKey)) {
			mEtContent.setInputType(InputType.TYPE_CLASS_NUMBER);
		}
		intType = getIntent().getIntExtra(INTTYPE, -1);
		mEtContent.setHint(getIntent().getStringExtra(HINTVALUE));
		
		TSUserInfo m = Global.getCURUSER();
		if (m != null) {
			mTvName.setText(m.getName());
			if (m.getAge() > 0&&"a.age".equals(mKey))
				mEtContent.setText(String.valueOf(m.getAge()));
			else
				mEtContent.setText("");
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_confirm:
			if (StringUtils.isEmail(mKey)) {
				ViewInject.toast("key error");
				return;
			}
			String value = mEtContent.getText().toString();
			if (StringUtils.isEmail(value)) {
				ViewInject.toast("没写东西....");
				return;
			}
			if ("a.age".equals(mKey)) {
				try {
					int age=Integer.parseInt(value);
					if(age<=0||age>100){
						ViewInject.toast("你是人类吗....");
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
			if (intType == -1)
				return;
			Req.updateUserInfo(intType, mKey, value, new HttpCallBack() {
				@Override
				public void onSuccess(String t) {
					super.onSuccess(t);
					try {
						Entity en = JsonUtil.json2Obj(t, Entity.class);
						if (0 == en.getCode())
	                        BroadCastUtil
                            .sendBroadCast(IntentNameUtil.SERVICE_ACTION_ON_REQ_STU_TEA_DATA); 
						else
							ViewInject.toast("修改失败！");
					} catch (Exception e) {
						ViewInject.toast("修改失败！");
					}
				}
			});
			finish();
			break;

		default:
			break;
		}
	}
}
