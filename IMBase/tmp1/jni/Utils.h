#ifndef _UTILS_H
#define _UTILS_H

#include <jni.h>

/**
* ���ܣ������̵�ʵ��
* @author wangqiang
* @date 2014-03-14
*/
class Parent : public ProcessBase
{
public:

	Parent(JNIEnv* env, jobject jobj);

	virtual bool create_child();

	virtual void do_work();

	virtual void catch_child_dead_signal();

	virtual void on_child_end();

	virtual ~Parent();

	bool create_channel();

	/**
	* ��ȡ�����̵�JNIEnv
	*/
	JNIEnv *get_jni_env() const;

	/**
	* ��ȡJava��Ķ���
	*/
	jobject get_jobj() const;

private:

	JNIEnv *m_env;

	jobject m_jobj;

};

/**
* �ӽ��̵�ʵ��
* @author wangqiang
* @date 2014-03-14
*/
class Child : public ProcessBase
{
public:

	Child();

	virtual ~Child();

	virtual void do_work();

	virtual bool create_child();

	virtual void catch_child_dead_signal();

	virtual void on_child_end();

	bool create_channel();

private:

	/**
	* �������������¼�
	*/
	void handle_parent_die();

	/**
	* ���������̷��͵���Ϣ
	*/
	void listen_msg();

	/**
	* ��������������.
	*/
	void restart_parent();

	/**
	* �������Ը����̵���Ϣ
	*/
	void handle_msg(const char* msg);

	/**
	* �̺߳�����������⸸�����Ƿ�ҵ�
	*/
	void* parent_monitor();

	void start_parent_monitor();

	/**
	* ���������������ǰ�������ĳ�Ա������Ϊ�̺߳���ʹ��
	*/
	union
	{
		void* (*thread_rtn)(void*);

		void* (Child::*member_rtn)();
	}RTN_MAP;
};
#endif 