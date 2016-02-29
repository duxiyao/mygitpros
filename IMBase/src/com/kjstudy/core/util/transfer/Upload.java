package com.kjstudy.core.util.transfer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.text.TextUtils;

/**
 * @author duxiyao
 * 
 *         文件上传类。
 */
public class Upload extends AbstractTransfered {
	protected ParamsUpload params;
	protected long totalSize = 1;

	public Upload(ParamsUpload params) {
		this.params = params;
	}

	public void upload(ProgressListener listener) {
		try {
			HttpEntity entity = getEntity(listener);// getEntity1(tmpBytes);

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(params.url);
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost, localContext);
			int code = response.getStatusLine().getStatusCode();
			if (code == HttpStatus.SC_OK) {
				HttpEntity resEntity = response.getEntity();
				String result = EntityUtils.toString(resEntity, "utf-8");
				System.out.println(result);
				if (null != listener)
					listener.onResponse(true,result, null);
				// if (result.contains(SCRequest.RESULT_SUCCESS))
				// return true;
			} else {
				System.out.println("status code:" + code);
			}
			throw new Exception(String.valueOf(code));
		} catch (Exception e) {
			e.printStackTrace();
			if (null != listener)
				listener.onResponse(false,"", e);
		}
	}

	@SuppressWarnings("deprecation")
	private HttpEntity getEntity(final ProgressListener listener)
			throws Exception {
		if (params.url == null)
			throw new RuntimeException("you must set up url first");
		CustomMultiPartEntity entity = new CustomMultiPartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE, new ProgressListener() {
					@Override
					public void transferred(long percent) {
						if (null != listener)
							listener.transferred((long) ((percent / (float) totalSize) * 100));
					}

					@Override
					public void onResponse(boolean isOk ,String ret, Exception e) {
						// TODO Auto-generated method stub

					}
				});
		if(params.params!=null){
			for(NameValuePair nvp :params.params)				
				addStringPart(nvp.getName(), nvp.getValue(), entity);
		}
		if(params.sparams!=null){
			for(String k :params.sparams.keySet())
				entity.addPart(k, params.sparams.get(k));
		}
//		if (params.file != null)
//			entity.addPart("file", new FileBody(params.file,
//					ContentType.DEFAULT_BINARY, params.file.getName()));
//		if (params.fileImg != null)
//			entity.addPart("fileImg", new ByteArrayBody(params.fileImg,
//					ContentType.DEFAULT_BINARY, "tmpimg.jpg"));
		totalSize = entity.getContentLength();
		return entity;
	}

	protected void addStringPart(String k, String v,
			CustomMultiPartEntity entity) throws UnsupportedEncodingException {
		if (!TextUtils.isEmpty(v))
			entity.addPart(k, new StringBody(v));
	}

	@Override
	public void exe(ProgressListener listener) {
		upload(listener);
	}
}
