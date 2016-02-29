package com.zbar.lib;

import java.io.IOException;
import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.imbase.R;
import com.kjstudy.core.util.LogUtil;
import com.zbar.lib.camera.CameraManager;
import com.zbar.lib.decode.CaptureActivityHandler;
import com.zbar.lib.decode.InactivityTimer;

/**
 * 描述: 扫描界面
 */
public class CaptureActivity extends Activity implements Callback {
	public static final String TAG = "CaptureActivity";
	public final static String INTENT_TAG = "mConsultInfo";
	/** 扫描消息转发类 */
	private CaptureActivityHandler handler;
	/** 表面 */
	private boolean hasSurface;
	/** 不活动计时器 */
	private InactivityTimer inactivityTimer;
	/** 媒体播放器 */
	private MediaPlayer mediaPlayer;
	/** 发出 嘟嘟 声 */
	private boolean playBeep;
	/** 提示音量 */
	private static final float BEEP_VOLUME = 0.50f;
	/** 振动 */
	private boolean vibrate;
	/** 左上角的x */
	private int x = 0;
	/** 左上角的y */
	private int y = 0;
	/** 作物的宽度 */
	private int cropWidth = 0;
	/** 作物的高度 */
	private int cropHeight = 0;
	/** 容器 */
	private RelativeLayout mContainer = null;
	/*** 作物布局 */
	private RelativeLayout mCropLayout = null;
	/** 是需要捕获的截图 */
	private boolean isNeedCapture = false;
	private Object obj;

	private String mQrcodeData;// 存放二维码数据

	/***
	 * 是否需要捕获的截图
	 * 
	 * @return
	 */
	public boolean isNeedCapture() {
		return isNeedCapture;
	}

	/***
	 * 设置是否需要捕获的截图
	 * 
	 * @param isNeedCapture
	 */
	public void setNeedCapture(boolean isNeedCapture) {
		this.isNeedCapture = isNeedCapture;
	}

	/***
	 * 获取左上角的X轴坐标
	 * 
	 * @return
	 */
	public int getX() {
		return x;
	}

	/***
	 * 设置左上角的X轴坐标
	 * 
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/***
	 * 获取左上角的y轴坐标
	 * 
	 * @return
	 */
	public int getY() {
		return y;
	}

	/***
	 * 设置左上角的Y轴坐标
	 * 
	 * @param x
	 */
	public void setY(int y) {
		this.y = y;
	}

	/***
	 * 作物的宽度
	 * 
	 * @return
	 */
	public int getCropWidth() {
		return cropWidth;
	}

	/***
	 * 设置作物的宽度
	 * 
	 * @param cropWidth
	 */
	public void setCropWidth(int cropWidth) {
		this.cropWidth = cropWidth;
	}

	/***
	 * 作物的高度
	 * 
	 * @return
	 */
	public int getCropHeight() {
		return cropHeight;
	}

	/***
	 * 设置作物高度
	 * 
	 * @param cropHeight
	 */
	public void setCropHeight(int cropHeight) {
		this.cropHeight = cropHeight;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_qr_scan);
		// 初始化 CameraManager
		CameraManager.init(getApplication());
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		mContainer = (RelativeLayout) findViewById(R.id.capture_containter);
		mCropLayout = (RelativeLayout) findViewById(R.id.capture_crop_layout);
		findViewById(R.id.result).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				resultFatherAct();
			}
		});
		;

		ImageView mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
		// 位移动画
		TranslateAnimation mAnimation = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE,
				0f, TranslateAnimation.RELATIVE_TO_PARENT, 0f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0.9f);
		// 持续时间
		mAnimation.setDuration(1500);
		// 动画的重复次数
		mAnimation.setRepeatCount(-1);
		// 定义这个动画应该做它到达终点时。此设置仅适用于当重复计数是大于0或无穷大。缺省为重启。重新启动或逆转//设置反方向执行
		mAnimation.setRepeatMode(Animation.REVERSE);
		// 设置此动画的加速度曲线。缺省为线性插值。
		mAnimation.setInterpolator(new LinearInterpolator());
		mQrLineView.setAnimation(mAnimation);
	}

	/***
	 * 返回Activity
	 */
	private void resultFatherAct() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				LogUtil.i(TAG, "resultFatherAct");
				Intent mIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable(TAG, (Serializable) obj);
				mIntent.putExtras(bundle);
				setResult(RESULT_OK, mIntent);
				CaptureActivity.this.finish();
			}
		});
	}

	// 旗帜
	boolean flag = true;

	/***
	 * 开启或者关闭闪光灯
	 */
	protected void light() {
		if (flag == true) {
			flag = false;
			// 开闪光灯
			CameraManager.get().openLight();
		} else {
			flag = true;
			// 关闪光灯
			CameraManager.get().offLight();
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		// 预览组件
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			// 添加回调
			surfaceHolder.addCallback(this);
			// 设置曲面的类型。
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		playBeep = true;
		// /音频管理器
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	/**
	 * 返回键
	 */
	public void onBackPressed() {
		resultFatherAct();
		super.onBackPressed();
	};

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		mCropLayout.clearAnimation();
		obj = null;
		super.onDestroy();
	}

	/**
	 * 处理解码,二维码扫描解析完成后,调用这个方法返回二维码扫描到的信息
	 * 
	 * @param result
	 */
	public void handleDecode(String result) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		LogUtil.e(TAG, "qrcode: " + result);
		// TODO 一种是扫面现有的I呼会员，另外一种是通过护士站来添加在院/出院病人
		// TODO 1：voipid; 2: (住院号|住院次数|姓名|性别|生日|入院日期|医院|诊断|手机号|状态(在院、出院))
		CameraManager.get().stopPreview();
		// 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
		// handler.sendEmptyMessage(R.id.restart_preview);
	}

	/***
	 * 初始化相机
	 * 
	 * @param surfaceHolder
	 */
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);

			Point point = CameraManager.get().getCameraResolution();
			int width = point.y;
			int height = point.x;

			int x = mCropLayout.getLeft() * width / mContainer.getWidth();
			int y = mCropLayout.getTop() * height / mContainer.getHeight();

			int cropWidth = mCropLayout.getWidth() * width
					/ mContainer.getWidth();
			int cropHeight = mCropLayout.getHeight() * height
					/ mContainer.getHeight();

			setX(x);
			setY(y);
			setCropWidth(cropWidth);
			setCropHeight(cropHeight);
			// 设置是否需要截图
			setNeedCapture(true);

		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(CaptureActivity.this);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	/***
	 * 扫描消息转发类 对象
	 * 
	 * @return
	 */
	public Handler getHandler() {
		return handler;
	}

	/***
	 * 初始化的嘟嘟声 实现媒体播放器播放声音
	 */
	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// 提出了一种音频流的体积应该由硬件音量控制改变。
			setVolumeControlStream(AudioManager.STREAM_MUSIC);// STREAM_MUSIC:音乐播放音频流
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			// 设置完成监听器
			mediaPlayer.setOnCompletionListener(beepListener);
			// 导入资源文件
			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				// 设置数据源
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				// 音量设置
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				// 准备播放器进行播放，同步
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	/** 震动时间 */
	private static final long VIBRATE_DURATION = 200L;

	/***
	 * 嘟嘟的声音开始或回复播放 和振动的开启
	 */
	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			// 开始或恢复播放。
			mediaPlayer.start();
		}
		if (vibrate) {
			// 振动器
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			// 指定震动时间
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/***
	 * 回调接口定义是要被当播放媒体源的播放已经完成。
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		// /当一个媒体源端在回放期间到达。
		public void onCompletion(MediaPlayer mediaPlayer) {
			// 以指定的时间位置。以毫秒为单位从开始寻求偏移
			// 寻求
			mediaPlayer.seekTo(0);
		}
	};
}