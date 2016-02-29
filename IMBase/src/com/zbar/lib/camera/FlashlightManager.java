package com.zbar.lib.camera;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.os.IBinder;
import android.util.Log;

/**
 * 描述: 闪光灯管理
 */
final class FlashlightManager {

	private static final String TAG = FlashlightManager.class.getSimpleName();
	/** 我的硬件服务 **/
	private static final Object iHardwareService;
	/** 设置闪光灯启动的方法 */
	private static final Method setFlashEnabledMethod;
	static {
		iHardwareService = getHardwareService();
		setFlashEnabledMethod = getSetFlashEnabledMethod(iHardwareService);
		if (iHardwareService == null) {
			Log.v(TAG, "This device does supports control of a flashlight");
		} else {
			Log.v(TAG, "This device does not support control of a flashlight");
		}
	}

	private FlashlightManager() {
	}

	/***
	 * 获得硬件服务
	 * 
	 * @return Object
	 */
	private static Object getHardwareService() {
		// 服务管理类
		Class<?> serviceManagerClass = maybeForName("android.os.ServiceManager");
		if (serviceManagerClass == null) {
			return null;
		}

		// 这类表示方法。这种方法可以访问信息，该方法可动态调用。
		Method getServiceMethod = maybeGetMethod(serviceManagerClass,
				"getService", String.class);
		if (getServiceMethod == null) {
			return null;
		}

		Object hardwareService = invoke(getServiceMethod, null, "hardware");// hardware
																			// 硬件
		if (hardwareService == null) {
			return null;
		}

		// I Hardware Service 我的硬件服务 Stub 存根
		Class<?> iHardwareServiceStubClass = maybeForName("android.os.IHardwareService$Stub");//
		if (iHardwareServiceStubClass == null) {
			return null;
		}

		Method asInterfaceMethod = maybeGetMethod(iHardwareServiceStubClass,
				"asInterface", IBinder.class);
		if (asInterfaceMethod == null) {
			return null;
		}

		return invoke(asInterfaceMethod, null, hardwareService);
	}

	/***
	 * 准备打开闪光灯的方法
	 * 
	 * @param iHardwareService
	 *            我的硬件服务
	 * @return
	 */
	private static Method getSetFlashEnabledMethod(Object iHardwareService) {
		if (iHardwareService == null) {
			return null;
		}
		Class<?> proxyClass = iHardwareService.getClass();
		return maybeGetMethod(proxyClass, "setFlashlightEnabled", boolean.class);// setFlashlightEnabled
																					// 设置闪光灯启用
	}

	/**
	 * 返回一个类对象表示与指定名称的类。名称应该是一个类的名称为类中的定义；然而，代表原始类型的类不能被发现使用这种方法。
	 */
	private static Class<?> maybeForName(String name) {
		try {
			return Class.forName(name);
		} catch (ClassNotFoundException cnfe) {
			// OK
			return null;
		} catch (RuntimeException re) {
			Log.w(TAG, "Unexpected error while finding class " + name, re);
			return null;
		}
	}

	/***
	 * 返回一个对象的方法为代表的公共方法用指定的名称和参数类型。该方法首先搜索C类表示由这个类，然后C和最后实现的接口的C和C类最后一名的超类方法匹配。
	 * 
	 * @param clazz
	 *            生成方法的类名
	 * @param name
	 *            请求的方法的名称
	 * @param argClasses
	 *            所请求的方法的参数类型。（类[ ]）空等效空数组。
	 * @return
	 */
	private static Method maybeGetMethod(Class<?> clazz, String name,
			Class<?>... argClasses) {
		try {
			return clazz.getMethod(name, argClasses);
		} catch (NoSuchMethodException nsme) {
			// OK
			return null;
		} catch (RuntimeException re) {
			Log.w(TAG, "Unexpected error while finding method " + name, re);
			return null;
		}
	}

	/***
	 * 返回动态调用这个方法的结果。
	 * 
	 * @param method
	 *            类方法
	 * @param instance
	 *            在调用该方法的对象（或空的静态方法）
	 * @param args
	 *            该方法的参数
	 * @return
	 */
	private static Object invoke(Method method, Object instance, Object... args) {
		try {
			return method.invoke(instance, args);
		} catch (IllegalAccessException e) {
			Log.w(TAG, "Unexpected error while invoking " + method, e);
			return null;
		} catch (InvocationTargetException e) {
			Log.w(TAG, "Unexpected error while invoking " + method,
					e.getCause());
			return null;
		} catch (RuntimeException re) {
			Log.w(TAG, "Unexpected error while invoking " + method, re);
			return null;
		}
	}

	/***
	 * 启用闪光灯
	 */
	static void enableFlashlight() {
		setFlashlight(true);
	}

	/***
	 * 关闭闪光灯
	 */
	static void disableFlashlight() {
		setFlashlight(false);
	}

	/***
	 * 设置闪光灯
	 * 
	 * @param active
	 *            boolean true false
	 */
	private static void setFlashlight(boolean active) {
		if (iHardwareService != null) {
			invoke(setFlashEnabledMethod, iHardwareService, active);
		}
	}

}
