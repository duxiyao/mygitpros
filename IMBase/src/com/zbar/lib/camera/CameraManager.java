package com.zbar.lib.camera;

import java.io.IOException;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Handler;
import android.view.SurfaceHolder;

/**
 * 描述: 相机管理
 */
public final class CameraManager {
	private static CameraManager cameraManager;

	static final int SDK_INT;
	static {
		int sdkInt;
		try {
			sdkInt = android.os.Build.VERSION.SDK_INT;
		} catch (NumberFormatException nfe) {
			sdkInt = 10000;
		}
		SDK_INT = sdkInt;
	}

	/** 相机参数配置 */
	private final CameraConfigurationManager configManager;
	/** 相机 */
	private Camera camera;
	/** 初始化 */
	private boolean initialized;
	/** 预览 */
	private boolean previewing;
	/**拍摄预览回调 */
	private final boolean useOneShotPreviewCallback;
	/** 相机预览回调 */
	private final PreviewCallback previewCallback;
	/** 相机自动对焦 */
	private final AutoFocusCallback autoFocusCallback;
	/** 参数 */
	private Parameters parameter;

	/** 创建CameraManager对象 */
	public static void init(Context context) {
		if (cameraManager == null) {
			cameraManager = new CameraManager(context);
		}
	}

	/** 获取相机管理对象 */
	public static CameraManager get() {
		return cameraManager;
	}

	/**
	 * 构造方法
	 */
	private CameraManager(Context context) {
		this.configManager = new CameraConfigurationManager(context);

		useOneShotPreviewCallback = SDK_INT > 3;
		previewCallback = new PreviewCallback(configManager,
				useOneShotPreviewCallback);
		autoFocusCallback = new AutoFocusCallback();
	}

	/***
	 * 打开驱动器
	 * 
	 * @param holder
	 * @throws IOException
	 */
	public void openDriver(SurfaceHolder holder) throws IOException {
		if (camera == null) {
			// 创建一个新的相机对象访问第一个背上装置的照相机。如果设备没有后置的摄像头，这将返回null。
			camera = Camera.open();
			if (camera == null) {
				throw new IOException();
			}
			// 设置预览显示,被用于的表面集的实时预览
			camera.setPreviewDisplay(holder);

			if (!initialized) {
				initialized = true;
				configManager.initFromCameraParameters(camera);
			}
			configManager.setDesiredCameraParameters(camera);
			FlashlightManager.enableFlashlight();
		}
	}

	/***
	 * 相机的分辨率
	 * 
	 * @return
	 */
	public Point getCameraResolution() {
		return configManager.getCameraResolution();
	}

	/***
	 * 关闭驱动程序
	 */
	public void closeDriver() {
		if (camera != null) {
			FlashlightManager.disableFlashlight();
			/*
			 * 断开并释放相机的对象资源。 你必须调用这个只要你完成了摄像机的对象。
			 */
			camera.release();
			camera = null;
		}
	}

	/***
	 * 开始预览
	 */
	public void startPreview() {
		if (camera != null && !previewing) {
			//开始捕捉和绘制到屏幕预览框
			camera.startPreview();
			previewing = true;
		}
	}

	/***
	 * 关闭预览
	 */
	public void stopPreview() {
		if (camera != null && previewing) {
			if (!useOneShotPreviewCallback) {
				camera.setPreviewCallback(null);
			}
			//停止捕获和预览帧绘制表面，并重置相机
			camera.stopPreview();
			previewCallback.setHandler(null, 0);
			autoFocusCallback.setHandler(null, 0);
			previewing = false;
		}
	}

	/**
	 * 预览回调
	 * @param handler 
	 * @param message what
	 */
	public void requestPreviewFrame(Handler handler, int message) {
		if (camera != null && previewing) {
			previewCallback.setHandler(handler, message);
			if (useOneShotPreviewCallback) {
				//设置一个拍摄预览回调
				camera.setOneShotPreviewCallback(previewCallback);
			} else {
				camera.setPreviewCallback(previewCallback);
			}
		}
	}

	/***
	 * 自动对焦
	 * @param handler
	 * @param message
	 */
	public void requestAutoFocus(Handler handler, int message) {
		if (camera != null && previewing) {
			autoFocusCallback.setHandler(handler, message);
			camera.autoFocus(autoFocusCallback);
		}
	}

	/***
	 * 打开灯
	 */
	public void openLight() {
		if (camera != null) {
			parameter = camera.getParameters();
			//设置闪光模式。
			parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);//火炬
			camera.setParameters(parameter);
		}
	}

	/***
	 * 关闭灯
	 */
	public void offLight() {
		if (camera != null) {
			parameter = camera.getParameters();
			parameter.setFlashMode(Parameters.FLASH_MODE_OFF);//关闭
			camera.setParameters(parameter);
		}
	}
}
