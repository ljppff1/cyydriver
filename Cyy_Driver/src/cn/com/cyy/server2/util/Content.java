package cn.com.cyy.server2.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final  class Content {
	//推送消息过来的表名
	public static final String PUSH_MSG_TABLE ="msg";
	//消息表中的电话
	public static final String MSG_TELEPHONE ="M_phone";
	//消息表中的地址
	public static final String MSG_ADDRESS ="M_address";
	//消息表中的经度
	public static final String MSG_LONGITUDE ="M_longitude";
	//消息表中的纬度
	public static final String MSG_LATITUDE ="M_latitude";
	//消息表中的状态
	public static final String MSG_STATUS ="M_status";
	
	public static final String HOME_URL = "http://192.168.0.132:8080"; // 测试库
/**
	public static final String SOCKET ="connect1.cyy928.com";
	public static final String HTTP ="http://c1.cyy928.com";
**/
//	public static final String SOCKET ="192.168.1.148";
	
	
	public static final String SOCKET ="192.168.1.148";
	public static final String HTTP ="http://192.168.1.148";

 //socket	192.168.1.148
	//115.29.245.128

	//用来判断是否联网
	public static boolean IS_CONNECT_NET =true;
	//用来判断是否退出登录
	public static boolean IS_CANCEL_LOGIN_NET =false;
	//用来判断是否是离线
	public static boolean IS_OFF_LINE_NET =false;
	//用来显示倒计时的时间
	public static int LONG_TIME =10*1000;
	
	
	
}
