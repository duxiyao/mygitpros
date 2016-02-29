package com.kjstudy.act;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.imbase.R;
import com.kjstudy.communication.CCPConfig;
import com.kjstudy.core.util.ParamsUtil;
import com.kjstudy.core.util.ViewUtil;
import com.kjstudy.ext.AdapterBase;

public class MyFriendsUI extends KJActivity {

	@BindView(id = R.id.lv)
	private ListView mLv;
	private FriendsAdapter mAdapter;

	@Override
	public void setRootView() {
		setContentView(R.layout.layout_myfriends_ui);
	}

	@Override
	public void initWidget() {
		mAdapter=new FriendsAdapter();
		mLv.setAdapter(mAdapter);
		String voipid="";
		if("86920000000025".equals(CCPConfig.VoIP_ID))
			voipid="86920000000023";
		else if("86920000000023".equals(CCPConfig.VoIP_ID))
			voipid="86920000000025";
		List<String> l=new ArrayList<String>();
		l.add(voipid);
		mAdapter.setDatas(l);
	}

	class FriendsAdapter extends AdapterBase<String, FriendsAdapter.Holder> {

		class Holder {
			TextView tv;
		}

		@Override
		protected int getItemLayout() {
			return R.layout.item_myfriends_ui;
		}

		@Override
		protected Holder init(int position, View v) {
			Holder h = new Holder();
			h.tv = ViewUtil.findView(v, R.id.tv);
			return h;
		}

		@Override
		protected void process(int position, Holder h) {
			final String voipid = mDatas.get(position);
			h.tv.setText(voipid);
			h.tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra(ParamsUtil.VOIP, voipid);
					intent.setClass(mContext, ChattingUI.class);
					showActivity(MyFriendsUI.this, intent);
				}
			});
		}
	}
}
