package com.kjstudy.core.util;

import java.util.List;

import org.kymjs.kjframe.KJDB;

import com.imbase.MyApplication;
import com.kjstudy.core.io.FileAccessor;

public class DBUtil {

	public static KJDB getDB() {
		return KJDB.create(MyApplication.getInstance().getApplicationContext(),
				FileAccessor.DBPATH, FileAccessor.DBNAME, true);
	}
	
	public static boolean isExists(Class<?> clazz, String strWhere) {
	    return getDB().isExists(clazz, strWhere);
	}

	public static void save(Object obj) {
		KJDB.create(MyApplication.getInstance().getApplicationContext(),
				FileAccessor.DBPATH, FileAccessor.DBNAME, true).save(obj);
	}

	public static boolean saveBindId(Object obj) {
		return KJDB.create(MyApplication.getInstance().getApplicationContext(),
				FileAccessor.DBPATH, FileAccessor.DBNAME, true).saveBindId(obj);
	}

	public static void update(Object obj) {
		KJDB.create(MyApplication.getInstance().getApplicationContext(),
				FileAccessor.DBPATH, FileAccessor.DBNAME, true).update(obj);
	}

	public static void update(Object obj, String sqlWhere) {
		KJDB.create(MyApplication.getInstance().getApplicationContext(),
				FileAccessor.DBPATH, FileAccessor.DBNAME, true).update(obj,
				sqlWhere);
	}

	public static <T> T findOne(Class<T> clazz, String sqlWhere) {
		try {
			return KJDB
					.create(MyApplication.getInstance().getApplicationContext(),
							FileAccessor.DBPATH, FileAccessor.DBNAME, true)
					.findAllByWhere(clazz, sqlWhere).get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> List<T> findAllByWhere(Class<T> clazz, String sqlWhere) {
		return KJDB.create(MyApplication.getInstance().getApplicationContext(),
				FileAccessor.DBPATH, FileAccessor.DBNAME, true).findAllByWhere(
				clazz, sqlWhere);
	}
}
