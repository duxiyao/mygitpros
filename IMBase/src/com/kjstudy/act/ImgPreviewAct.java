package com.kjstudy.act;

import java.io.File;
import java.io.FileInputStream;

import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.FileUtils;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.imbase.R;
import com.kjstudy.bars.BarDefault2;
import com.kjstudy.bean.data.TSUserInfo;
import com.kjstudy.core.io.FileAccessor;
import com.kjstudy.core.net.Req;
import com.kjstudy.core.util.BroadCastUtil;
import com.kjstudy.core.util.DBUtil;
import com.kjstudy.core.util.Global;
import com.kjstudy.core.util.IntentNameUtil;
import com.kjstudy.core.util.JsonUtil;
import com.kjstudy.core.util.cache.CacheFactory;
import com.kjstudy.core.util.cache.IFileCache;
import com.kjstudy.core.util.transfer.ParamsUpload;
import com.kjstudy.core.util.transfer.ProgressListener;
import com.kjstudy.core.util.transfer.TransferUtil;
import com.kjstudy.dialog.DialogAssistant;
import com.open.crop.CropImageView;

public class ImgPreviewAct extends KJActivity {

	public static final String IMGDATA = "ImgPreview.img.path";
	@BindView(id = R.id.civ)
	private CropImageView mCiv;
	private String mTmpImgFilePath;

	@Override
	public void setRootView() {
		setContentView(R.layout.layout_img_preview);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		BarDefault2 bar = new BarDefault2();
		bar.setOnClickLis(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Bitmap bmp = mCiv.getCropImage();
					showConfirm(bmp);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		setCustomBar(bar.getBarView());
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			mTmpImgFilePath = getIntent().getStringExtra(IMGDATA);
			FileInputStream fin = new FileInputStream(new File(mTmpImgFilePath));
			mCiv.setDrawable(BitmapDrawable.createFromStream(fin, ""), 200, 200);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void showConfirm(final Bitmap bmp) {
		if (bmp == null)
			return;
		View v = LayoutInflater.from(this).inflate(
				R.layout.layout_confirm_cropimg, null);
		final Dialog d = DialogAssistant.getCustomDialog(v);
		d.setCancelable(false);
		ImageView iv = (ImageView) v.findViewById(R.id.iv_img);
		iv.setImageBitmap(bmp);
		PhotoViewAttacher pva = new PhotoViewAttacher(iv);

		v.findViewById(R.id.iv_cancle).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (d.isShowing())
							d.dismiss();
					}
				});
		v.findViewById(R.id.iv_confirm).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						final TSUserInfo m = Global.getCURUSER();
						if (m == null)
							return;
						final String filePath = FileAccessor.TMP_DIR
								+ "myhead.jpg";
						File f = new File(filePath);
						if (f.exists())
							f.delete();
						try {
							f.getParentFile().mkdirs();

						} catch (Exception e) {
							e.printStackTrace();
						}
						FileUtils.bitmapToFile(bmp, filePath);
						ParamsUpload p = new ParamsUpload();
						p.url = Req.getUploadHeadImgAddr();
						p.params.add(new BasicNameValuePair("id", String
								.valueOf(m.getId())));
						FileBody fb = new FileBody(f);
						p.sparams.put("file", fb);
						TransferUtil.upload(p, new ProgressListener() {

							@Override
							public void transferred(long percent) {
							}

							@Override
							public void onResponse(boolean isOk, String ret,
									Exception e) {
								try {
									if (isOk) {
									    String url=JsonUtil.getString(ret, "data");
										if (!TextUtils.isEmpty(url)) {
											deleteMyPreHead();
											IFileCache ifc = CacheFactory
													.getICache(url, 2);
											ifc.generateFilePath(url);
											FileUtils.move(filePath,
													ifc.getFilePath());
											m.setPhotoUrl(url);
											DBUtil.update(m, "id=" + m.getId());
											BroadCastUtil
													.sendBroadCast(IntentNameUtil.ON_UPLOAD_HEAD_IMG_SUCCESS);
										}
									} else {

									}
								} catch (Exception e2) {
									e2.printStackTrace();
								}
								ImgPreviewAct.this.finish();
							}
						});
					}
				});
		d.show();
	}

	private void deleteMyPreHead() {
		String url = "";
		TSUserInfo m = Global.getCURUSER();
		if (m != null) {
			url = m.getPhotoUrl();
		}
		if (TextUtils.isEmpty(url))
			return;
		IFileCache ic = CacheFactory.getICache(url, 2);
		if (ic == null || !ic.isExists())
			return;
		ic.delete();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			new File(mTmpImgFilePath).delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
