package com.kjstudy.im.views;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.imbase.R;
import com.kjstudy.a.im.AMsgView;
import com.kjstudy.bean.ImMessage;

public class IMTextView extends AMsgView {

	@Override
	public View getView(ImMessage msg) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.im_textview, null);
		TextView tv = findView(v, R.id.tv_text);
		tv.setText(msg.getMsgBody());
		switch (msg.getMsgLocationType()) {
		case 1:
			tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			break;
		case 2:
			tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
			break;
		case 3:
			tv.setGravity(Gravity.CENTER);
			break;
		default:
			break;
		}
		return v;
	}

}
