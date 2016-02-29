package com.kjstudy.act;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.imbase.R;
import com.kjstudy.core.thread.ThreadManager;
import com.zbar.lib.CaptureActivity;
import com.zbar.lib.QRCodeUtil;

public class Qrcode extends KJActivity {

	private final int REQUEST_CODE_QRCODE = 0x8765;// 启动二维码扫描界面的请求码
	
	@BindView(id = R.id.tv_result)
	private TextView mTvResult;
	
	@BindView(id=R.id.iv_qrimg)
	private ImageView mIvQr;

	@Override
	public void setRootView() {
		setContentView(R.layout.layout_qrcode_ui);
	}

	@Override
	public void initWidget() {
		Intent intent = new Intent();
		intent.setClass(this, CaptureActivity.class);
		startActivityForResult(intent, REQUEST_CODE_QRCODE);
//		ThreadManager.getInstance().executeTask(mRunnable);
	}
	
	private Runnable mRunnable=new Runnable() {
		
		@Override
		public void run() {
			QRCodeUtil u=new QRCodeUtil();
			try {
				Bitmap bmp = u.getQrcode("abcd", null, false);
				showBmp(bmp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	private void showBmp(final Bitmap bmp){
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				mIvQr.setImageBitmap(bmp);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			mTvResult.setText(scanResult);
		}
	}
}
