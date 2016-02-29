package com.kjstudy.bean;

import com.kjstudy.bean.data.UserInfo;

public class EUserInfo extends Entity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1674443968120887517L;
	private UserInfo data;

	public UserInfo getData() {
		return data;
	}

	public void setData(UserInfo data) {
		this.data = data;
	}
}
