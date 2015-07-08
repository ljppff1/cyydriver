package cn.com.cyy.server2.db;

import cn.com.cyy.server2.util.Content;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySql extends SQLiteOpenHelper{
	private static MySql mInstance = null;
	//创建推送消息的表
	private static final String create ="Create Table "
			+Content.PUSH_MSG_TABLE +"( _id integer primary key,"
			+Content.MSG_ADDRESS +" text,"+Content.MSG_LATITUDE +" text,"
			+Content.MSG_LONGITUDE +" text,"+ Content.MSG_TELEPHONE +" text,"
			+Content.MSG_STATUS +" text)";
	
	public MySql(Context context) {
		super(context, "cyy.db", null, 1);
	}
	
	/** 单例模式 **/
	static synchronized MySql getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new MySql(context);
		}
		return mInstance;
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(create);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
}
