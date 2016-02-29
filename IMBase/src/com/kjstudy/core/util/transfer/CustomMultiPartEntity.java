package com.kjstudy.core.util.transfer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

/**
 * @author duxiyao
 *
 * 重写MultipartEntity的writeTo方法从而监控上传文件进度。
 */
public class CustomMultiPartEntity extends MultipartEntity {
	private final ProgressListener listener;

	public CustomMultiPartEntity(final ProgressListener listener) {
		super();
		this.listener = listener;
	}

	public CustomMultiPartEntity(final HttpMultipartMode mode,
			final ProgressListener listener) {
		super(mode);
		this.listener = listener;
	}

	public CustomMultiPartEntity(HttpMultipartMode mode, final String boundary,
			final Charset charset, final ProgressListener listener) {
		super(mode, boundary, charset);
		this.listener = listener;
	}

	@Override
	public void writeTo(final OutputStream outstream) throws IOException {
		super.writeTo(new CountingOutputStream(outstream, this.listener));
	}
}
