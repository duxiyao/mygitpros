package com.zbar.lib.camera;

import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * 描述: 相机参数配置
 */
final class CameraConfigurationManager {

	private static final String TAG = CameraConfigurationManager.class
			.getSimpleName();

	private static final int TEN_DESIRED_ZOOM = 27;
	/**编译,正则表达式*/
	private static final Pattern COMMA_PATTERN = Pattern.compile(",");

	private final Context context;
	/**屏幕分辨率*/
	private Point screenResolution;
	/**相机的分辨率*/
	private Point cameraResolution;
	/**相机所照图片的预览格式*/
	private int previewFormat;
	/**相机所照图片的预览格式的值*/
	private String previewFormatString;

	CameraConfigurationManager(Context context) {
		this.context = context;
	}

	/***
	 * 相机的参数初始化
	 * @param camera 相机
	 */
	@SuppressWarnings("deprecation")
	void initFromCameraParameters(Camera camera) {
		//参数
		Camera.Parameters parameters = camera.getParameters();
		previewFormat = parameters.getPreviewFormat();
		previewFormatString = parameters.get("preview-format");
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		//提供有关显示的大小和密度的信息。
		Display display = manager.getDefaultDisplay();
		screenResolution = new Point(display.getWidth(), display.getHeight());
		
	    Point screenResolutionForCamera = new Point();   
	    screenResolutionForCamera.x = screenResolution.x;   
	    screenResolutionForCamera.y = screenResolution.y;   
	    
	    //将x,y的方向进行了,掉转
	    if (screenResolution.x < screenResolution.y) {  
	         screenResolutionForCamera.x = screenResolution.y;  
	         screenResolutionForCamera.y = screenResolution.x;
	    }
		cameraResolution = getCameraResolution(parameters, screenResolutionForCamera);
	}
	
	/***
	 * 设置相机所需的参数
	 * @param camera 相机的对象
	 */
	void setDesiredCameraParameters(Camera camera) {
		//参数
		Camera.Parameters parameters = camera.getParameters();
		//设置预览图片的尺寸
		parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
		setFlash(parameters);
		setZoom(parameters);
		//设置显示的方向
		camera.setDisplayOrientation(90);
		camera.setParameters(parameters);
	}

	/***
	 * 相机的分辨率
	 * @return
	 */
	Point getCameraResolution() {
		return cameraResolution;
	}

	/***
	 * 屏幕分辨率

	 * @return
	 */
	Point getScreenResolution() {
		return screenResolution;
	}

	/***
	 * 相机所照图片的预览格式

	 * @return
	 */
	int getPreviewFormat() {
		return previewFormat;
	}

	/***
	 * 相机所照图片的预览格式的值

	 * @return
	 */
	String getPreviewFormatString() {
		return previewFormatString;
	}

	/**
	 * 得到相机分辨率
	 * @param parameters 参数
	 * @param screenResolution 屏幕大小
	 * @return 相机分辨率
	 */
	private static Point getCameraResolution(Camera.Parameters parameters,
			Point screenResolution) {
		//预览大小值
		String previewSizeValueString = parameters.get("preview-size-values");
		if (previewSizeValueString == null) {
			previewSizeValueString = parameters.get("preview-size-value");
		}

		Point cameraResolution = null;

		if (previewSizeValueString != null) {
			cameraResolution = findBestPreviewSizeValue(previewSizeValueString,
					screenResolution);
		}

		if (cameraResolution == null) {
			cameraResolution = new Point((screenResolution.x >> 3) << 3,
					(screenResolution.y >> 3) << 3);
		}

		return cameraResolution;
	}

	/***
	 * 找到最好的预览的大小
	 * @param previewSizeValueString 预览大小值
	 * @param screenResolution 屏幕分辨率
	 * @return  相机分辨率
	 */
	private static Point findBestPreviewSizeValue(
			CharSequence previewSizeValueString, Point screenResolution) {
		int bestX = 0;
		int bestY = 0;
		int diff = Integer.MAX_VALUE;
		for (String previewSize : COMMA_PATTERN.split(previewSizeValueString)) {

			previewSize = previewSize.trim();
			int dimPosition = previewSize.indexOf('x');
			if (dimPosition < 0) {
				continue;
			}

			int newX;
			int newY;
			try {
				newX = Integer.parseInt(previewSize.substring(0, dimPosition));
				newY = Integer.parseInt(previewSize.substring(dimPosition + 1));
			} catch (NumberFormatException nfe) {
				continue;
			}

			int newDiff = Math.abs(newX - screenResolution.x)
					+ Math.abs(newY - screenResolution.y);
			if (newDiff == 0) {
				bestX = newX;
				bestY = newY;
				break;
			} else if (newDiff < diff) {
				bestX = newX;
				bestY = newY;
				diff = newDiff;
			}

		}

		if (bestX > 0 && bestY > 0) {
			return new Point(bestX, bestY);
		}
		return null;
	}

	/***
	 * 找到最好的MOT缩放值
	 * @param stringValues  MOT缩放值
	 * @param tenDesiredZoom 所需的变焦
	 * @return
	 */
	private static int findBestMotZoomValue(CharSequence stringValues,
			int tenDesiredZoom) {
		int tenBestValue = 0;
		for (String stringValue : COMMA_PATTERN.split(stringValues)) {
			stringValue = stringValue.trim();
			double value;
			try {
				value = Double.parseDouble(stringValue);
			} catch (NumberFormatException nfe) {
				return tenDesiredZoom;
			}
			int tenValue = (int) (10.0 * value);
			if (Math.abs(tenDesiredZoom - value) < Math.abs(tenDesiredZoom
					- tenBestValue)) {
				tenBestValue = tenValue;
			}
		}
		return tenBestValue;
	}

	/**
	 * 设置闪光
	 * @param parameters 相机.参数
	 */
	private void setFlash(Camera.Parameters parameters) {
		//flash-value 闪光值
		if (Build.MODEL.contains("Behold II") && CameraManager.SDK_INT == 3) { // 3
			parameters.set("flash-value", 1);
		} else {
			parameters.set("flash-value", 2);
		}
		//flash-mode,闪光模式,关闭
		parameters.set("flash-mode", "off");
	}

	/**
	 * 设置变焦
	 * @param parameters 相机.参数
	 */
	private void setZoom(Camera.Parameters parameters) {
		//变焦支持 zoom-supported
		String zoomSupportedString = parameters.get("zoom-supported");
		if (zoomSupportedString != null
				&& !Boolean.parseBoolean(zoomSupportedString)) {
			return;
		}

		int tenDesiredZoom = TEN_DESIRED_ZOOM;

		
		//最大变焦 max-zoom
		String maxZoomString = parameters.get("max-zoom");
		if (maxZoomString != null) {
			try {
				int tenMaxZoom = (int) (10.0 * Double
						.parseDouble(maxZoomString));
				if (tenDesiredZoom > tenMaxZoom) {
					tenDesiredZoom = tenMaxZoom;
				}
			} catch (NumberFormatException nfe) {
				Log.w(TAG, "Bad max-zoom: " + maxZoomString);
			}
		}

		//拍照变焦最大 taking-picture-zoom-max
		String takingPictureZoomMaxString = parameters
				.get("taking-picture-zoom-max");
		if (takingPictureZoomMaxString != null) {
			try {
				int tenMaxZoom = Integer.parseInt(takingPictureZoomMaxString);
				if (tenDesiredZoom > tenMaxZoom) {
					tenDesiredZoom = tenMaxZoom;
				}
			} catch (NumberFormatException nfe) {
				Log.w(TAG, "Bad taking-picture-zoom-max: "
						+ takingPictureZoomMaxString);
			}
		}

		//MOT的缩放值 mot-zoom-values
		String motZoomValuesString = parameters.get("mot-zoom-values");
		if (motZoomValuesString != null) {
			tenDesiredZoom = findBestMotZoomValue(motZoomValuesString,
					tenDesiredZoom);
		}

		//MOT变焦步骤 mot-zoom-step
		String motZoomStepString = parameters.get("mot-zoom-step");
		if (motZoomStepString != null) {
			try {
				double motZoomStep = Double.parseDouble(motZoomStepString
						.trim());
				int tenZoomStep = (int) (10.0 * motZoomStep);
				if (tenZoomStep > 1) {
					tenDesiredZoom -= tenDesiredZoom % tenZoomStep;
				}
			} catch (NumberFormatException nfe) {
				// continue
			}
		}

		// Set zoom. This helps encourage the user to pull back.
		// Some devices like the Behold have a zoom parameter
		//变焦
		if (maxZoomString != null || motZoomValuesString != null) {
			parameters.set("zoom", String.valueOf(tenDesiredZoom / 10.0));
		}

		// Most devices, like the Hero, appear to expose this zoom parameter.
		// It takes on values like "27" which appears to mean 2.7x zoom
		//缩放参数
		if (takingPictureZoomMaxString != null) {
			parameters.set("taking-picture-zoom", tenDesiredZoom);
		}
	}

}
