package com.kjstudy.core.util.transfer;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author duxiyao
 * 
 * 重写OutputStream的write方法，达到监控进度功能。
 */
public class CountingOutputStream extends FilterOutputStream {

	private final ProgressListener listener;
	private long transferred;

	public CountingOutputStream(final OutputStream out,
			final ProgressListener listener) {
		super(out);
		this.listener = listener;
		this.transferred = 0;
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		out.write(b, off, len);
		this.transferred += len;
		if (null != this.listener)
			this.listener.transferred(this.transferred);
	}

//	 public void write(int b) throws IOException {
//	 out.write(b);
//	 this.transferred++;
//	 if (null != this.listener)
//	 this.listener.transferred(this.transferred);
//	 }

}
