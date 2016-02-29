#include "process.h"
#include "Utils.h"  
#include <jni.h>

/**
* 全局变量，代表应用程序进程.
*/
ProcessBase *g_process = NULL;

/**
* 应用进程的UID.
*/
const char* g_userId = NULL;

/**
* 全局的JNIEnv，子进程有时会用到它.
*/
JNIEnv* g_env = NULL;

extern "C"
{
	JNIEXPORT jboolean JNICALL Java_com_guard_Watcher_createWatcher(JNIEnv*, jobject, jstring);

	JNIEXPORT jboolean JNICALL Java_com_guard_Watcher_connectToMonitor(JNIEnv*, jobject);

	JNIEXPORT jint JNICALL Java_com_guard_Watcher_sendMsgToMonitor(JNIEnv*, jobject, jstring);

	JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM*, void*);

	char* jstringTostr(JNIEnv* env, jstring jstr);
};

JNIEXPORT jboolean JNICALL Java_com_guard_Watcher_createWatcher(JNIEnv* env, jobject thiz, jstring user)
{
	g_process = new Parent(env, thiz);

	g_userId = (const char*)jstringTostr(env, user);

	g_process->catch_child_dead_signal();

	if (!g_process->create_child())
	{
		LOGE("<<create child error!>>");

		return JNI_FALSE;
	}
	LOGE("<<create child sucess!>>");
	return JNI_TRUE;
}


JNIEXPORT jboolean JNICALL Java_com_guard_Watcher_connectToMonitor(JNIEnv* env, jobject thiz)
{
	if (g_process != NULL)
	{
		if (g_process->create_channel())
		{
			return JNI_TRUE;
		}

		return JNI_FALSE;
	}
}


char* jstringTostr(JNIEnv* env, jstring jstr)
{ 
	char* rtn = NULL;
	jclass clsstring = env->FindClass("java/lang/String");
	jstring strencode = env->NewStringUTF("utf-8");
	jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
	jbyteArray barr = (jbyteArray)env->CallObjectMethod(jstr, mid, strencode);
	jsize alen = env->GetArrayLength(barr);
	jbyte* ba = env->GetByteArrayElements(barr, JNI_FALSE);
	if (alen > 0)
	{
		rtn = (char*)malloc(alen + 1);

		memcpy(rtn, ba, alen);
		rtn[alen] = 0;
	}
	env->ReleaseByteArrayElements(barr, ba, 0);
	return rtn;
}