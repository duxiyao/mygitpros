package com.zbar.lib.bitmap;

/***
 * 亮度光源
 * @author Administrator
 *
 */
public abstract class LuminanceSource {

	private final int width;
	private final int height;

	protected LuminanceSource(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * 一个数组包含亮度数据。
	 * Fetches one row of luminance data from the underlying platform's bitmap.
	 * Values range from 0 (black) to 255 (white). Because Java does not have an
	 * unsigned byte type, callers will have to bitwise and with 0xff for each
	 * value. It is preferable for implementations of this method to only fetch
	 * this row rather than the whole image, since no 2D Readers may be
	 * installed and getMatrix() may never be called.
	 * 
	 * @param y
	 *            The row to fetch, which must be in [0,getHeight())
	 * @param row
	 *            An optional preallocated array. If null or too small, it will
	 *            be ignored. Always use the returned object, and ignore the
	 *            .length of the array.
	 * @return An array containing the luminance data.
	 */
	public abstract byte[] getRow(int y, byte[] row);

	/**
	 * 读取亮度数据为基础的位图。
	 * Fetches luminance data for the underlying bitmap. Values should be
	 * fetched using: {@code int luminance = array[y * width + x] & 0xff}
	 * 
	 * @return A row-major 2D array of luminance values. Do not use
	 *         result.length as it may be larger than width * height bytes on
	 *         some platforms. Do not modify the contents of the result.
	 */
	public abstract byte[] getMatrix();

	/**
	 * @return The width of the bitmap.位图的宽度。
	 */
	public final int getWidth() {
		return width;
	}

	/**
	 * @return The height of the bitmap.位图的高度
	 */
	public final int getHeight() {
		return height;
	}

	/**
	 * @return Whether this subclass supports cropping.获得这类是否支持剪切。
	 */
	public boolean isCropSupported() {
		return false;
	}

	/**返回与裁剪后的图像数据的一个新的对象
	 * Returns a new object with cropped image data. Implementations may keep a
	 * reference to the original data rather than a copy. Only callable if
	 * isCropSupported() is true.
	 * 
	 * @param left  左边的坐标，必须在[ 0，getwidth()）
	 *            The left coordinate, which must be in [0,getWidth())
	 * @param top   顶部的坐标，必须在[ 0，getheight()）
	 *            The top coordinate, which must be in [0,getHeight())
	 * @param width  矩形的裁剪宽度。
	 *            The width of the rectangle to crop.
	 * @param height 矩形的作物高度。
	 *            The height of the rectangle to crop.
	 * @return A cropped version of this object.一个版本的对象。
	 */
	public LuminanceSource crop(int left, int top, int width, int height) {
		//This luminance source does not support cropping.这个亮度源不支持裁剪。
		//Unsupported Operation Exception 不支持的操作异常
		throw new UnsupportedOperationException("This luminance source does not support cropping.");
	}

	/**无论这类支持逆时针旋转。
	 * @return Whether this subclass supports counter-clockwise rotation.
	 */
	public boolean isRotateSupported() {
		return false;
	}

	/**
	 * @return a wrapper of this {@code LuminanceSource} which inverts the
	 *         luminances it returns -- black becomes white and vice versa, and
	 *         each value becomes (255-value).
	 *         <br>
	 *         一个包装这个 {@code LuminanceSource} 反转亮度返回——黑色变成白色，反之亦然，每个值成为（255值）。
	 */
	public LuminanceSource invert() {
		return new InvertedLuminanceSource(this);
	}

	/**
	 * Returns a new object with rotated image data by 90 degrees
	 * counterclockwise. Only callable if {@link #isRotateSupported()} is true.
	 * <br>
	 * 返回一个新的对象的图像数据旋转90度顺时针。只可调用如果{@link #isRotateSupported()}是真的。
	 * 
	 * @return A rotated version of this object.一个旋转的物体。
	 */
	public LuminanceSource rotateCounterClockwise() {
//		/这个亮度源不支持旋转90度。
		throw new UnsupportedOperationException("This luminance source does not support rotation by 90 degrees.");
	}

	/**
	 * Returns a new object with rotated image data by 45 degrees
	 * counterclockwise. Only callable if {@link #isRotateSupported()} is true.
	 * <br>
	 * 返回一个新的对象的图像数据旋转45度顺时针。只可调用如果{@link #isRotateSupported()}是真的。
	 * 
	 * @return A rotated version of this object.一个旋转的物体。
	 */
	public LuminanceSource rotateCounterClockwise45() {
		//这个亮度源不支持旋转45度。
		throw new UnsupportedOperationException("This luminance source does not support rotation by 45 degrees.");
	}

	@Override
	public final String toString() {
		byte[] row = new byte[width];
		StringBuilder result = new StringBuilder(height * (width + 1));
		for (int y = 0; y < height; y++) {
			row = getRow(y, row);
			for (int x = 0; x < width; x++) {
				int luminance = row[x] & 0xFF;
				char c;
				if (luminance < 0x40) {
					c = '#';
				} else if (luminance < 0x80) {
					c = '+';
				} else if (luminance < 0xC0) {
					c = '.';
				} else {
					c = ' ';
				}
				result.append(c);
			}
			result.append('\n');
		}
		return result.toString();
	}

}
