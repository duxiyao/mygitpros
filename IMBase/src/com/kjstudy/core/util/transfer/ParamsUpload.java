package com.kjstudy.core.util.transfer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.content.AbstractContentBody;

/**
 * @author duxiyao
 * 
 *         文件上传下载类参数，根据接口文档中接口12、13编码。
 */
public class ParamsUpload {
	public ParamsUpload() {
		params = new ArrayList<NameValuePair>();
		sparams = new HashMap<String, AbstractContentBody>();
	}

	public List<NameValuePair> params;
	/**
	 * 文件上传地址。
	 */
	public String url;

	public HashMap<String, AbstractContentBody> sparams;
}
