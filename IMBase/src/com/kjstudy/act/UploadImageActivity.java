package com.kjstudy.act;

import java.io.File;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.utils.ActUtil;
import org.kymjs.kjframe.utils.FileUtils;
import org.kymjs.kjframe.utils.ImgUtil;

import com.imbase.R;
import com.kjstudy.bean.data.TSUserInfo;
import com.kjstudy.core.io.FileAccessor;
import com.kjstudy.core.util.DBUtil;
import com.kjstudy.core.util.Global;
import com.kjstudy.core.util.cache.CacheFactory;
import com.kjstudy.core.util.cache.IFileCache;
import com.kjstudy.dialog.DialogAssistant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 头像上传界面
 * 
 * @author tianzc
 * 
 */
public class UploadImageActivity extends KJActivity {

	private final int FROMALBUM = 1, PHOTO = 2;
	private Dialog mChoiceDialog;

	@Override
	public void setRootView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.consult_upload_headimg_actdia);
		showUploadHeadChoice();
	}

	private void close() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (mChoiceDialog != null && mChoiceDialog.isShowing())
					mChoiceDialog.dismiss();
				finish();
			}
		});
	}

	private void showUploadHeadChoice() {
		View v = LayoutInflater.from(this).inflate(
				R.layout.layout_dialog_select_headimg, null);
		mChoiceDialog = DialogAssistant.getCustomDialog(v);
		mChoiceDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				close();
			}
		});
		v.findViewById(R.id.tv_album).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				/* 开启Pictures画面Type设定为image */
				intent.setType("image/*");
				/* 使用Intent.ACTION_GET_CONTENT这个Action */
				intent.setAction(Intent.ACTION_GET_CONTENT);
				/* 取得相片后返回本画面 */
				startActivityForResult(intent, FROMALBUM);
				// v.setEnabled(false);
			}
		});

		v.findViewById(R.id.tv_photo).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, PHOTO);
				// v.setEnabled(false);
			}
		});

		v.findViewById(R.id.cancle).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				close();
			}
		});
		mChoiceDialog.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// this.setVisible(false);
		switch (requestCode) {
		case FROMALBUM:
			chooseFromAlbum(resultCode, data);
			break;
		case PHOTO:
			photoGraph(resultCode, data);
			break;
		default:
			break;
		}
	} 

	private void afterSelected(Bitmap bmp) {
		close();
		File f = new File(FileAccessor.TMP_HEAD_IMG_FILEPATH);
		if (f.exists())
			f.delete();
		FileUtils.bitmapToFile(bmp, FileAccessor.TMP_HEAD_IMG_FILEPATH);
		Bundle b = new Bundle();
		b.putString(ImgPreviewAct.IMGDATA, FileAccessor.TMP_HEAD_IMG_FILEPATH);
		ActUtil.startAct(ImgPreviewAct.class, b);
	}

	/**
	 * 用手机拍照。
	 * 
	 * @param resultCode
	 * @param data
	 */
	private void photoGraph(int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Log.i("TestFile",
						"SD card is not avaiable/writeable right now.");
				return;
			}
			Bundle bundle = data.getExtras();
			Bitmap bmp = (Bitmap) bundle.get("data");
			afterSelected(bmp);
		}
	}

	/**
	 * 从相册选照片。
	 * 
	 * @param resultCode
	 * @param data
	 */
	private void chooseFromAlbum(int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Uri uri = data.getData();
			try {
				Bitmap bmp = ImgUtil.uri2Bmp(uri);
				afterSelected(bmp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
