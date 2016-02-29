package com.kjstudy.core.util;

public class IntentNameUtil {
	public static final String IM_RECE_IN_DB = "im_save_in_db";
	public static final String IM_SEND_IN_DB = "im_send_in_db";
	public static final String IM_UPDATE_SEND_IN_DB = "im_update_send_in_db";

	/**
	 * @date 2015年11月4日
	 * @author duxiyao
	 * @description 注册成功
	 */
	public static final String REGISTER_SUCCESS = "user_register_success";
	public static final String LOGIN_SUCCESS = "user_login_success";

	/**
	 * @date 2015年11月4日
	 * @author duxiyao
	 * @description 记住的上次的用户信息已经获取，可以使用{@link Global.getCURUSER()}
	 */
	public static final String ON_LAST_USER_LOGIN = "user_login_success";
	/**
	 * @date 2015年11月5日
	 * @author duxiyao
	 * @description 上传头像成功
	 */
	public static final String ON_UPLOAD_HEAD_IMG_SUCCESS = "upload_head_img_success";
	/**
	 * 修改个人信息成功
	 */
	public static final String ON_ALTER_PERSONAL_INFO_SUCCESS = "alter_personal_info_success";
	
	/** 
	* @Fields SERVICE_ACTION_ON_REQ_STU_TEA_DATA : 获取学生、老师个人身份的信息
	* @author duxiyao 
	* @date 2015年11月25日 下午8:12:33 
	*/ 
	public static final String SERVICE_ACTION_ON_REQ_STU_TEA_DATA="download_stu_tea_personal_info";
	/** 
	* @Fields SERVICE_ACTION_ON_UP_REAL_TIME_POS : 上传实时位置
	* @author duxiyao 
	* @date 2015年12月3日 下午2:49:24 
	*/ 
	public static final String SERVICE_ACTION_ON_UP_REAL_TIME_POS="up_real_time_pos";
	public static final String SERVICE_ACTION_ON_STOP_REAL_TIME_POS="stop_up_real_time_pos";
}
