package com.kjstudy.act;

import java.util.List;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.StringUtils;

import android.bluetooth.BluetoothClass.Device;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.imbase.R;
import com.kjstudy.bean.ImMessage;
import com.kjstudy.business.builder.ImMsgViewBuilder;
import com.kjstudy.communication.IMHelper;
import com.kjstudy.communication.UserData;
import com.kjstudy.core.thread.ThreadManager;
import com.kjstudy.core.util.DBUtil;
import com.kjstudy.core.util.IntentNameUtil;
import com.kjstudy.core.util.ParamsUtil;
import com.kjstudy.core.util.ViewUtil;
import com.kjstudy.ext.AdapterBase;
import com.kjstudy.plugin.ChattingItemView;

public class ChattingUI extends KJActivity {

	@BindView(id = R.id.lv_chatting)
	private ListView mLvChatting;
	@BindView(id = R.id.et_im_chatting_content)
	private EditText mEtChattingContent;
	@BindView(id = R.id.tv_send, click = true)
	private TextView mTvSend;

	private String mVoip;
	private ChattingAdapter mAdapter;
	private List<ImMessage> list;

//	private Handler mHandler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//
//			Bundle b = null;
//			int reason = -1;
//			if (msg.obj instanceof Bundle) {
//				b = (Bundle) msg.obj;
//			}
//			switch (msg.what) {
//			case CCPHelper.WHAT_ON_SEND_MEDIAMSG_RES:
//				if (b == null) {
//					return;
//				}
//				try {
//					reason = b.getInt(Device.REASON);
//					InstanceMsg instancemsg = (InstanceMsg) b
//							.getSerializable(Device.MEDIA_MESSAGE);
//					if (instancemsg == null) {
//						return;
//					}
//
//					String messageId = null;
//					if (instancemsg instanceof IMTextMsg) {
//						IMTextMsg imTextMsg = (IMTextMsg) instancemsg;
//						messageId = imTextMsg.getMsgId();
//						String sqlWhere = "msgId='" + messageId + "'";
//						ImMessage imsg = DBUtil.findOne(ImMessage.class,
//								sqlWhere);
//						if (reason == 0) {
//							if (imsg != null) {
//								imsg.setSendState(2);
//								DBUtil.update(imsg);
//							}
//						} else {
//							// do send text message failed ..
//							if (imsg != null) {
//								imsg.setSendState(3);
//								DBUtil.update(imsg);
//							}
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				break;
//			case CCPHelper.WHAT_ON_AMPLITUDE:
//				break;
//			case CCPHelper.WHAT_ON_RECODE_TIMEOUT:
//				break;
//			case CCPHelper.WHAT_ON_PLAY_VOICE_FINSHING:
//				break;
//			case CCPHelper.WHAT_ON_RECEIVE_SYSTEM_EVENTS:
//				break;
//			default:
//				break;
//			}
//		};
//	};

	@Override
	public void setRootView() {
		setContentView(R.layout.layout_chatting_ui);
	}

	@Override
	public void initWidget() {
		try {
			mVoip = getIntent().getStringExtra(ParamsUtil.VOIP);
		} catch (Exception e) {
			e.printStackTrace();
			finish();
			return;
		}
		if (StringUtils.strIsEmpty(mVoip)) {
			finish();
			return;
		}
		mAdapter = new ChattingAdapter(this);
		mLvChatting.setAdapter(mAdapter);
		setFilters(IntentNameUtil.IM_RECE_IN_DB, IntentNameUtil.IM_SEND_IN_DB,
				IntentNameUtil.IM_UPDATE_SEND_IN_DB);
		loadDatas();
//		CCPHelper.getInstance().setHandler(mHandler);
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.tv_send:
			sendMsg();
			break;

		default:
			break;
		}
	}

	@Override
	protected void dealBroadcase(Intent intent) {
		if (IntentNameUtil.IM_RECE_IN_DB.equals(intent.getAction())) {
			loadDatas();
		} else if (IntentNameUtil.IM_SEND_IN_DB.equals(intent.getAction())) {
			mEtChattingContent.setText("");
			loadDatas();
		} else if (IntentNameUtil.IM_UPDATE_SEND_IN_DB.equals(intent
				.getAction())) {
			loadDatas();
		}
	}

	private void sendMsg() {
		String msg = mEtChattingContent.getText().toString();
		IMHelper.getInstance().sendText(mVoip, msg, new UserData());
//		DeviceHelper.getInstance().sendText(mVoip, msg, new UserData());
	}

	private void loadDatas() {
		ThreadManager.getInstance().executeTask(new Runnable() {
			@Override
			public void run() {
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				list = DBUtil.getDB().findAllByWhere(ImMessage.class,
						"voipAccount='" + mVoip + "'");
				showDatas();
			}
		});
	}

	private void showDatas() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (list != null && list.size() > 0) {
					mAdapter.setDatas(list);
					mLvChatting.setSelection(list.size());
				}
			}
		});
	}

	class ChattingAdapter extends
			AdapterBase<ImMessage, ChattingAdapter.Holder> {
		protected ChattingAdapter(Context context) {
			super(context);
		}

		class Holder {
			ChattingItemView chattingView;
		}

		@Override
		protected int getItemLayout() {
			return R.layout.item_chatting_im_msg;
		}

		@Override
		protected Holder init(int position, View v) {
			Holder h = new Holder();
			h.chattingView = ViewUtil.findView(v, R.id.extv_chattingview);
			return h;
		}

		@Override
		protected void process(int position, Holder h) {
			ImMessage msg = mDatas.get(position);
			View child = ImMsgViewBuilder.getView(msg);
			h.chattingView.setContentView(child);
			initialization(msg, h.chattingView);
		}

		public void initialization(ImMessage msg, ChattingItemView chattingView) {
			if (msg.getMsgLocationType() == 1)
				chattingView.setHeadLeft();
			else if (msg.getMsgLocationType() == 2)
				chattingView.setHeadRight();
			else if (msg.getMsgLocationType() == 3)
				chattingView.setNoneHead();
			chattingView.setOnHeadClickable(mOnHeadClick);
		}

		private OnClickListener mOnHeadClick = new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		};
	}
}
