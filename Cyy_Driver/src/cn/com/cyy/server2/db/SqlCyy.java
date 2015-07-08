package cn.com.cyy.server2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SqlCyy {
	/** 数据库操作对象 **/
	public SQLiteDatabase db;

	private MySql mySql;

	public SqlCyy(Context context) {
		mySql = MySql.getInstance(context);
		db = mySql.getWritableDatabase();
	}

	
	
	
	
}
