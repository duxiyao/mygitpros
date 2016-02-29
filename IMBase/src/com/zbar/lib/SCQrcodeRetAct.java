package com.zbar.lib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;



/**
 * @author duxiyao 启动二维码扫描页面
 */
public class SCQrcodeRetAct {

	private final int REQUEST_CODE_QRCODE = 0x8765;// 启动二维码扫描界面的请求码
	private Activity mAct;

	public SCQrcodeRetAct(Activity act) {
		mAct = act;
	}

	public void startActForRet() {

		Intent intent = new Intent();
		intent.setClass(mAct, CaptureActivity.class);
		mAct.startActivityForResult(intent, REQUEST_CODE_QRCODE);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (REQUEST_CODE_QRCODE == requestCode) {
			if (resultCode == Activity.RESULT_OK) {
				if (null != intent) {
					Object obj = intent
							.getSerializableExtra(CaptureActivity.TAG);
					if (null != obj) {
						Bundle tmp = new Bundle();
						
//							tmp.putSerializable(
//									SLDIntent.ACTION_CONSULT_INFO,
//									(ConsultInfo) obj);
//							stratActivity(QRResultUI.class, tmp);

						}
				}
			}
		}
	}
	
	


	private void stratActivity(Class<?> cls, Bundle params) {
//		SCIntent.stratActivity(mAct, cls, params);
	}
}
