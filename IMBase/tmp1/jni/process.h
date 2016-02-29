#ifndef _PROCESS_H
#define _PROCESS_H


#include <jni.h>
#include <sys/select.h>
#include <unistd.h>
#include <sys/socket.h>
#include <pthread.h>
#include <signal.h>
#include <sys/wait.h>
#include <android/log.h>
#include <sys/types.h>
#include <sys/un.h>
#include <errno.h>
#include <stdlib.h> 


#define LOG_TAG "Native"


#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)


/**
* ����:�Ը��ӽ��̵�һ������
* @author wangqiang
* @date 2014-03-14
*/
class ProcessBase
{
public:

	ProcessBase();

	/**
	* ���ӽ���Ҫ���Ĺ�������ͬ,����һ������ӿ��ɸ��ӽ���
	* �Լ�ȥʵ��.
	*/
	virtual void do_work() = 0;

	/**
	* ���̿��Ը�����Ҫ�����ӽ���,�������Ҫ�����ӽ���,���Ը�
	* �˽ӿ�һ����ʵ�ּ���.
	*/
	virtual bool create_child() = 0;

	/**
	* ��׽�ӽ����������ź�,���û���ӽ��̴˷������Ը�һ����ʵ��.
	*/
	virtual void catch_child_dead_signal() = 0;

	/**
	* ���ӽ�������֮������������.
	*/
	virtual void on_child_end() = 0;

	/**
	* �������ӽ���ͨ��ͨ��.
	*/
	bool create_channel();

	/**
	* ����������ͨ��ͨ��.
	* @param channel_fd ͨ�����ļ�����
	*/
	void set_channel(int channel_fd);

	/**
	* ��ͨ����д������.
	* @param data д��ͨ��������
	* @param len  д����ֽ���
	* @return ʵ��д��ͨ�����ֽ���
	*/
	int write_to_channel(void* data, int len);

	/**
	* ��ͨ���ж�����.
	* @param data �����ͨ���ж��������
	* @param len  ��ͨ���ж�����ֽ���
	* @return ʵ�ʶ������ֽ���
	*/
	int read_from_channel(void* data, int len);

	/**
	* ��ȡͨ����Ӧ���ļ�������
	*/
	int get_channel() const;

	virtual ~ProcessBase();

protected:

	int m_channel;
};

#endif