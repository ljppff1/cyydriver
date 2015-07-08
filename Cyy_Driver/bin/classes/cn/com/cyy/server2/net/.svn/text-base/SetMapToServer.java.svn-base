package cn.com.cyy.server2.net;

import java.util.HashMap;
import java.util.Map;

import cn.com.cyy.server2.finals.SharedprefenceFinals;

import android.content.Context;
import android.os.Handler;

/**
 * 封装Map集合发送至服务器
 * 
 * @author hurenji
 * 
 */
public class SetMapToServer {

	public static Map<String, String> map = new HashMap<String, String>();

	/**
	 * 以手机号码获取验证码
	 * 
	 * @param context
	 *            上下文
	 * @param handler
	 *            消息处理
	 * @param phone
	 *            电话号码
	 */
	public static void getCodeMap(Context context, Handler handler, String phone) {
		System.out.println("phone-->" + phone);
		map.put("mobile", phone);
		ConnNet.conn(context, handler, map , "getVerifyCode");
	}

	/**
	 * 用户注册
	 * @param context
	 * @param handler	
	 */
	public static void sumbitRegister(Context context, Handler handler) {
		String mobile = SharedprefenceFinals.register_shared.getString("phone",
				"");
		String headImg = SharedprefenceFinals.register_shared.getString(
				"head_base64", "");
		String password = SharedprefenceFinals.register_shared.getString(
				"password", "");
		String name = SharedprefenceFinals.register_shared.getString(
				"real_name", "");
		String sex = SharedprefenceFinals.register_shared.getString("sex", "");
		String company_id = SharedprefenceFinals.register_shared.getString(
				"firm", "");
		String server_id = SharedprefenceFinals.register_shared.getString(
				"server", "");
		String idcardimg = SharedprefenceFinals.register_shared.getString(
				"base64_idCard", "");
		String takeSerParId =SharedprefenceFinals.register_shared.getString("takeSerParId","");

		map.put("mobile", mobile);
		map.put("headImg", headImg);
		map.put("password", password);
		map.put("name", name);
		map.put("sex", sex);
		map.put("company_id", company_id);
		map.put("service_item", server_id);
		map.put("idCardImg", "");
		map.put("service_id", takeSerParId);
		ConnNet.conn(context, handler, map , "memberRegister");

	}
	
	/**
	 * 用户登录
	 * @param contxt
	 * @param handler
	 * @param account	账号
	 * @param password  密码
	 */
	public static void userLogin(Context context , Handler handler , String account , String password){
		System.out.println("account--> " + account + " , " + "password--> " + password);
		map.put("mobile", account);
		map.put("password", password);
		map.put("longitude", "");		// 经度
		map.put("latitude", "");		// 纬度
		ConnNet.conn(context, handler, map , "login");
	}
	
	/**
	 * 无参数服务
	 * @param context
	 * @param handler
	 * @param parameter	方法名称
	 */
	public static void noParameter(Context context , Handler handler ,String parameter){
		ConnNet.conn(context, handler, map , parameter);
	}

}
