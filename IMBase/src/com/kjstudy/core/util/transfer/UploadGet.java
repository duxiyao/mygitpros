package com.kjstudy.core.util.transfer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

public class UploadGet extends Upload {

	private boolean isFirst = true;

	public UploadGet(ParamsUpload params) {
		super(params);
	}

	@Override
	protected void addStringPart(String k, String v,
			CustomMultiPartEntity entity) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		String tmp = k + "=" + v;
		if (isFirst) {
			sb.append("?");
			isFirst = false;
		} else {
			sb.append("&");
		}
		sb.append(tmp);
		params.url += sb.toString();
	}
}
