/*
 * Copyright (c) 2014,KJFrameForAndroid Open Source Project,张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kymjs.kjframe.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.zip.ZipFile;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Video;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.util.Log;

/**
 * 文件与流处理工具类<br>
 * 
 * <b>创建时间</b> 2014-8-14
 * 
 * @author kymjs (https://github.com/kymjs)
 * @version 1.1
 */
public final class FileUtils {
    /**
     * 检测SD卡是否存在
     */
    public static boolean checkSDcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    /**
     * 将文件保存到本地
     */
    public static void saveFileCache(byte[] fileData, String folderPath,
            String fileName) {
        File folder = new File(folderPath);
        folder.mkdirs();
        File file = new File(folderPath, fileName);
        ByteArrayInputStream is = new ByteArrayInputStream(fileData);
        OutputStream os = null;
        if (!file.exists()) {
            try {
                file.createNewFile();
                os = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while (-1 != (len = is.read(buffer))) {
                    os.write(buffer, 0, len);
                }
                os.flush();
            } catch(Exception e) {
                throw new RuntimeException(
                        FileUtils.class.getClass().getName(), e);
            } finally {
                closeIO(is, os);
            }
        }
    }

    /**
     * 从指定文件夹获取文件
     * 
     * @return 如果文件不存在则创建,如果如果无法创建文件或文件名为空则返回null
     */
    public static File getSaveFile(String folderPath, String fileNmae) {
        File file = new File(getSavePath(folderPath) + File.separator
                + fileNmae);
        try {
            file.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 获取SD卡下指定文件夹的绝对路径
     * 
     * @return 返回SD卡下的指定文件夹的绝对路径
     */
    public static String getSavePath(String folderName) {
        return getSaveFolder(folderName).getAbsolutePath();
    }

    /**
     * 获取文件夹对象
     * 
     * @return 返回SD卡下的指定文件夹对象，若文件夹不存在则创建
     */
    public static File getSaveFolder(String folderName) {
        File file = new File(getSDCardPath() + File.separator + folderName
                + File.separator);
        file.mkdirs();
        return file;
    }

    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 输入流转byte[]<br>
     */
    public static final byte[] input2byte(InputStream inStream) {
        if (inStream == null) {
            return null;
        }
        byte[] in2b = null;
        BufferedInputStream in = new BufferedInputStream(inStream);
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int rc = 0;
        try {
            while ((rc = in.read()) != -1) {
                swapStream.write(rc);
            }
            in2b = swapStream.toByteArray();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(inStream, in, swapStream);
        }
        return in2b;
    }

    /**
     * 把uri转为File对象
     */
    public static File uri2File(Activity aty, Uri uri) {
        if (SystemTool.getSDKVersion() < 11) {
            // 在API11以下可以使用：managedQuery
            String[] proj = { MediaStore.Images.Media.DATA };
            @SuppressWarnings("deprecation")
            Cursor actualimagecursor = aty.managedQuery(uri, proj, null, null,
                    null);
            int actual_image_column_index = actualimagecursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor
                    .getString(actual_image_column_index);
            return new File(img_path);
        } else {
            // 在API11以上：要转为使用CursorLoader,并使用loadInBackground来返回
            String[] projection = { MediaStore.Images.Media.DATA };
            CursorLoader loader = new CursorLoader(aty, uri, projection, null,
                    null, null);
            Cursor cursor = loader.loadInBackground();
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return new File(cursor.getString(column_index));
        }
    }

    /**
     * 复制文件
     * 
     * @param from
     * @param to
     */
    public static void copyFile(File from, File to) {
        if (null == from || !from.exists()) {
            return;
        }
        if (null == to) {
            return;
        }
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(from);
            if (!to.exists()) {
                to.createNewFile();
            }
            os = new FileOutputStream(to);
            copyFileFast(is, os);
        } catch(Exception e) {
            throw new RuntimeException(FileUtils.class.getClass().getName(), e);
        } finally {
            closeIO(is, os);
        }
    }

    /**
     * 快速复制文件（采用nio操作）
     * 
     * @param is
     *            数据来源
     * @param os
     *            数据目标
     * @throws IOException
     */
    public static void copyFileFast(FileInputStream is, FileOutputStream os)
            throws IOException {
        FileChannel in = is.getChannel();
        FileChannel out = os.getChannel();
        in.transferTo(0, in.size(), out);
    }

    /**
     * 关闭流
     * 
     * @param closeables
     */
    public static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch(IOException e) {
                throw new RuntimeException(
                        FileUtils.class.getClass().getName(), e);
            }
        }
    }

    /**
     * 图片写入文件
     * 
     * @param bitmap
     *            图片
     * @param filePath
     *            文件路径
     * @return 是否写入成功
     */
    public static boolean bitmapToFile(Bitmap bitmap, String filePath) {
        boolean isSuccess = false;
        if (bitmap == null) {
            return isSuccess;
        }
        File file = new File(filePath.substring(0,
                filePath.lastIndexOf(File.separator)));
        if (!file.exists()) {
            file.mkdirs();
        }

        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(filePath),
                    8 * 1024);
            isSuccess = bitmap.compress(CompressFormat.PNG, 100, out);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeIO(out);
        }
        return isSuccess;
    }

    /**
     * 从文件中读取文本
     * 
     * @param filePath
     * @return
     */
    public static String readFile(String filePath) {
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
        } catch(Exception e) {
            throw new RuntimeException(FileUtils.class.getName()
                    + "readFile---->" + filePath + " not found");
        }
        return inputStream2String(is);
    }

    /**
     * 从assets中读取文本
     * 
     * @param name
     * @return
     */
    public static String readFileFromAssets(Context context, String name) {
        InputStream is = null;
        try {
            is = context.getResources().getAssets().open(name);
        } catch(Exception e) {
            throw new RuntimeException(FileUtils.class.getName()
                    + ".readFileFromAssets---->" + name + " not found");
        }
        return inputStream2String(is);
    }

    /**
     * 输入流转字符串
     * 
     * @param is
     * @return 一个流中的字符串
     */
    public static String inputStream2String(InputStream is) {
        if (null == is) {
            return null;
        }
        StringBuilder resultSb = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            resultSb = new StringBuilder();
            String len;
            while (null != (len = br.readLine())) {
                resultSb.append(len);
            }
        } catch(Exception ex) {
        } finally {
            closeIO(is);
        }
        return null == resultSb ? null : resultSb.toString();
    }

    // /**
    // * 纠正图片角度（有些相机拍照后相片会被系统旋转）
    // *
    // * @param path
    // * 图片路径
    // */
    // public static void correctPictureAngle(String path) {
    // int angle = BitmapOperateUtil.readPictureDegree(path);
    // if (angle != 0) {
    // Bitmap image = BitmapHelper.rotate(angle,
    // BitmapCreate.bitmapFromFile(path, 1000, 1000));
    // bitmapToFile(image, path);
    // }
    // }

    /** TAG for log messages. */
    static final String TAG = "FileUtils";

    private static final int X_OK = 1;

    private static boolean libLoadSuccess;

    static {
        try {
            System.loadLibrary("access");
            libLoadSuccess = true;
        } catch(UnsatisfiedLinkError e) {
            libLoadSuccess = false;
            Log.d(TAG, "libaccess.so failed to load.");
        }
    }

    /**
     * use it to calculate file count in the directory recursively
     */
    private static int fileCount = 0;

    /**
     * Whether the filename is a video file.
     * 
     * @param filename
     * @return
     */
    /*
     * public static boolean isVideo(String filename) { String mimeType =
     * getMimeType(filename); if (mimeType != null &&
     * mimeType.startsWith("video/")) { return true; } else { return false; } }
     */

    /**
     * Whether the URI is a local one.
     * 
     * @param uri
     * @return
     */
    public static boolean isLocal(String uri) {
        if (uri != null && !uri.startsWith("http://")) {
            return true;
        }
        return false;
    }

    /***
     * 判断文件夹是否存在
     * 
     * @param path
     *            文件路径
     */
    public boolean isFileExists(String path) {
        File mFile = new File(path);
        if (!mFile.exists()) {
            return false;
        }
        return true;
    }

    /***
     * 判断文件夹是否存在,不存在生成
     * 
     * @param path
     *            文件路径
     */
    public void createMkdirsFile(String path) {
        File mFile = new File(path);
        if (!mFile.exists()) {
            mFile.mkdirs();
        }
    }

    /**
     * 判断有名有同名的文件
     * 
     * @param url
     *            文件名
     * @param path
     *            文件路径
     */
    public boolean isSameFile(String path, String url) {
        File mFile = new File(path + url);
        if (mFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the extension of a file name, like ".png" or ".jpg".
     * 
     * @param uri
     * @return Extension including the dot("."); "" if there is no extension;
     *         null if uri was null.
     */
    public static String getExtension(String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }

    /**
     * Returns true if uri is a media uri.
     * 
     * @param uri
     * @return
     */
    public static boolean isMediaUri(String uri) {
        if (uri.startsWith(Audio.Media.INTERNAL_CONTENT_URI.toString())
                || uri.startsWith(Audio.Media.EXTERNAL_CONTENT_URI.toString())
                || uri.startsWith(Video.Media.INTERNAL_CONTENT_URI.toString())
                || uri.startsWith(Video.Media.EXTERNAL_CONTENT_URI.toString())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Convert File into Uri.
     * 
     * @param file
     * @return uri
     */
    public static Uri getUri(File file) {
        if (file != null) {
            return Uri.fromFile(file);
        }
        return null;
    }

    /**
     * Convert Uri into File.
     * 
     * @param uri
     * @return file
     */
    public static File getFile(Uri uri) {
        if (uri != null) {
            String filepath = uri.getPath();
            if (filepath != null) {
                return new File(filepath);
            }
        }
        return null;
    }

    /**
     * Returns the path only (without file name).
     * 
     * @param file
     * @return
     */
    public static File getPathWithoutFilename(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                // no file to be split off. Return everything
                return file;
            } else {
                String filename = file.getName();
                String filepath = file.getAbsolutePath();

                // Construct path without file name.
                String pathwithoutname = filepath.substring(0,
                        filepath.length() - filename.length());
                if (pathwithoutname.endsWith("/")) {
                    pathwithoutname = pathwithoutname.substring(0,
                            pathwithoutname.length() - 1);
                }
                return new File(pathwithoutname);
            }
        }
        return null;
    }

    /**
     * Constructs a file from a path and file name.
     * 
     * @param curdir
     * @param file
     * @return
     */
    public static File getFile(String curdir, String file) {
        String separator = "/";
        if (curdir.endsWith("/")) {
            separator = "";
        }
        File clickedFile = new File(curdir + separator + file);
        return clickedFile;
    }

    public static File getFile(File curdir, String file) {
        return getFile(curdir.getAbsolutePath(), file);
    }

    public static String formatSize(Context context, long sizeInBytes) {
        return Formatter.formatFileSize(context, sizeInBytes);
    }

    public static String formatDate(Context context, long dateTime) {
        return DateFormat.getDateFormat(context).format(new Date(dateTime));
    }

    public static int getFileCount(File file) {
        fileCount = 0;
        calculateFileCount(file);
        return fileCount;
    }

    /**
     * @param f
     *            - file which need be checked
     * @return if is archive - returns true othewise
     */
    public static boolean checkIfZipArchive(File f) {
        try {
            new ZipFile(f);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    private static void calculateFileCount(File file) {
        if (!file.isDirectory()) {
            fileCount++;
            return;
        }
        if (file.list() == null) {
            return;
        }
        for (String fileName : file.list()) {
            File f = new File(file.getAbsolutePath() + File.separator
                    + fileName);
            calculateFileCount(f);
        }
    }

    /**
     * Native helper method, returns whether the current process has execute
     * privilages.
     * 
     * @param a
     *            File
     * @return returns TRUE if the current process has execute privilages.
     */
    public static boolean canExecute(File mContextFile) {
        try {
            // File.canExecute() was introduced in API 9. If it doesn't exist,
            // then
            // this will throw an exception and the NDK version will be used.
            Method m = File.class.getMethod("canExecute", new Class[] {});
            Boolean result = (Boolean) m.invoke(mContextFile);
            return result;
        } catch(Exception e) {
            if (libLoadSuccess) {
                return access(mContextFile.getPath(), X_OK);
            } else {
                return false;
            }
        }
    }

    // Native interface to unistd.h's access(*char, int) method.
    public static native boolean access(String path, int mode);

    /**
     * 复制文件
     * 
     * @param srcPath
     *            源文件绝对路径
     * @param dirPath
     *            目标文件所在目录
     * @return boolean
     */
    public static boolean copyFile(String srcPath, String dirPath,
            String fileName) {
        boolean flag = false;

        File srcFile = new File(srcPath);
        if (!srcFile.exists()) { // 源文件不存在
            System.out.println("源文件不存在");
            return false;
        }
        // 获取待复制文件的文件名
        if (TextUtils.isEmpty(fileName)) {
            fileName = srcPath.substring(srcPath.lastIndexOf(File.separator));
        }
        String destPath = dirPath + fileName;
        if (destPath.equals(srcPath)) { // 源文件路径和目标文件路径重复
            System.out.println("源文件路径和目标文件路径重复!");
            return false;
        }
        File destFile = new File(destPath);
        if (destFile.exists() && destFile.isFile()) { // 该路径下已经有一个同名文件
            System.out.println("目标目录下已有同名文件!");
            return false;
        }

        File destFileDir = new File(dirPath);
        destFileDir.mkdirs();
        try {
            FileInputStream fis = new FileInputStream(srcPath);
            FileOutputStream fos = new FileOutputStream(destFile);
            byte[] buf = new byte[1024];
            int c;
            while ((c = fis.read(buf)) != -1) {
                fos.write(buf, 0, c);
            }
            fis.close();
            fos.close();

            flag = true;
        } catch(IOException e) {
            //
        }

        if (flag) {
            System.out.println("复制文件成功!");
        }

        return flag;
    }

    public static void move(String srcFilePath, String destFilePath) {
        String fileName = "";
        String dirPath = "";
        try {
            fileName = destFilePath.substring(
                    destFilePath.lastIndexOf("/") + 1, destFilePath.length());
            dirPath = destFilePath.substring(0,
                    destFilePath.lastIndexOf("/") + 1);
        } catch(Exception e) {
            e.printStackTrace();
            return;
        }
        if (copyFile(srcFilePath, dirPath, fileName)) {
            File file = new File(srcFilePath);
            try {
                file.delete();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取文件，返回以byte数组形式的数据
     * 
     * @param filePath
     *            要读取的文件路径名
     * @return
     */
    public static byte[] readFileBytes(String filePath) {
        try {
            if (isFileExist(filePath)) {
                FileInputStream fi = new FileInputStream(filePath);
                return readInputStream(fi);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static byte[] readInputStream(InputStream in) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            byte[] b = new byte[in.available()];
            int length = 0;
            while ((length = in.read(b)) != -1) {
                os.write(b, 0, length);
            }

            b = os.toByteArray();

            in.close();
            in = null;

            os.close();
            os = null;

            return b;

        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void copyFromAssets(AssetManager am, String fileName,
            String outFileName) throws IOException {
        InputStream myInput;
        OutputStream myOutput = new FileOutputStream(outFileName);
        myInput = am.open(fileName);
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while (length > 0) {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }
        myOutput.flush();
        myInput.close();
        myOutput.close();
    }
}