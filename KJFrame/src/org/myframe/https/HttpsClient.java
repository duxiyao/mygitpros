package org.myframe.https;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.myframe.https.certificate.HttpsHostnameVerifier;
import org.myframe.https.certificate.HttpsX509TrustManager;
import org.myframe.ui.ViewInject;
import org.myframe.utils.MLoger;
import org.myframe.utils.NetUtil;

public class HttpsClient {
	private static HttpsClient mInstance = new HttpsClient();

	private SSLContext mSslContext = null;
	private final int TIME_OUT = 60 * 1000; // 超时时间
	private final String LINE_END = "\r\n";// 换行

	private final String CHARSET = "utf-8"; // 设置编码
	private final String PREFIX = "--"; // 前缀
	private String CONTENT_TYPE = "multipart/form-data"; // 内容类型
	private String mUrl;
	private HashMap<String, String> mParams = new HashMap<String, String>();
	private boolean isPost = true;

	private HttpsClient() {
	}

	public static HttpsClient getInstance() {
		return mInstance;
	}

	public HttpsClient setParams(HashMap<String, String> p) {
		if (p != null)
			this.mParams = p;
		return this;
	}

	public HttpsClient addParam(String k, String v) {
		this.mParams.put(k, v);
		return this;
	}

	public HttpsClient setMethodGet() {
		isPost = false;
		return this;
	}

	public HttpsClient c(String url) {
		this.mUrl = url;
		return this;
	}

	private SSLContext initCertificate() throws Exception {
		// 设置SSLContext
		SSLContext sslcontext = SSLContext.getInstance("TLS");
		X509TrustManager[] xtmArray = new X509TrustManager[] { new HttpsX509TrustManager() };
		sslcontext.init(null, xtmArray, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext
				.getSocketFactory());
		// 设置验证方式
		HttpsURLConnection
				.setDefaultHostnameVerifier(new HttpsHostnameVerifier());

		return sslcontext;

	}

	public HttpsURLConnection openConnection() throws IOException {
		HttpsURLConnection connection = null;

		try {

			if (!isPost) { // 如果是get请求则拼接URL地址，一般情况下不会走https的get请求
				mUrl += packageTextParamsForGet();
			}
			// 初始化连接
			URL connecter = new URL(mUrl);
			connection = (HttpsURLConnection) connecter.openConnection();
			// 设置安全套接工厂

			connection.setSSLSocketFactory(mSslContext.getSocketFactory());
			connection.setConnectTimeout(TIME_OUT);
			connection.setReadTimeout(TIME_OUT);
			// connection.setRequestProperty("User-Agent",
			// "Mozilla/5.0 ( compatible ) ");
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("Content-Type", "*/*;charset=UTF-8");
			connection.setRequestProperty("Connection", "Keep-Alive");
			if (isPost) {
				connection.setRequestMethod("POST");
				connection.setUseCaches(false);
				connection.setInstanceFollowRedirects(false);
			}
			// 设置输入输出流
			connection.setDoInput(true);
			if (isPost)
				connection.setDoOutput(true);

		} catch (IOException ioe) {
			throw ioe;
		}
		// 查询params里面是否有数据
		DataOutputStream out = new DataOutputStream(
				connection.getOutputStream());
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : mParams.entrySet())
			sb.append(entry.getValue() + LINE_END);
		// 向输入流里面写入,需要提交的数据
		out.write(sb.toString().getBytes());
		out.flush();
		out.close();
		return connection;
	}

	protected String packageTextParamsForGet() {

		StringBuilder stringBuilder = new StringBuilder();
		// 依次取出params里面的参数进行拼接
		for (Map.Entry<String, String> entry : mParams.entrySet()) {
			stringBuilder.append(entry.getKey() + "=" + entry.getValue() + "&");
		}
		String tmp = stringBuilder.toString();
		if (tmp.endsWith("&")) {
			tmp = "?" + tmp;
			int len = tmp.length();
			tmp = tmp.substring(0, len - 1);
		}
		MLoger.debug("packageTextParamsForGet:" + tmp);
		return tmp;
	}

	private String parseResponse(InputStream inputStream) throws IOException {
		if (inputStream == null) {
			throw new NullPointerException(
					"parseResponse fail, inputstream is null");
		}
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream, CHARSET));
		StringBuilder stringBuilder = new StringBuilder(); // 这里使用StringBulider节省字符串拼接时的冗余
		String readLine = null;
		while ((readLine = bufferedReader.readLine()) != null) {
			stringBuilder.append(readLine);
		}
		return stringBuilder.toString();
	}

	public void exec(HttpsCb cb) {

		try {
			if (!NetUtil.isConnected()) {
				ViewInject.toast("找不到网络了...");
				return;
			}
		} catch (Exception e) {
			return;
		}
		if (mSslContext == null) {
			try {
				mSslContext = initCertificate();
			} catch (Exception e) {
				e.printStackTrace();
				ViewInject.toast("init ssl failed");
				return;
			}
		}
		try {
			HttpsURLConnection conn = openConnection();
			int code = conn.getResponseCode();
			MLoger.debug("httpcode-->" + code);

			if (code == 200 || code == 201) { // 网络请求成功
				String data = parseResponse(conn.getInputStream());
				MLoger.debug("response data-->" + data);
				if (cb != null)
					cb.onResponse(data);
			} else {
				MLoger.debug("error httpcode-->" + code);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
