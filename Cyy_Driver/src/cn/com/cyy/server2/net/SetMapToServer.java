package cn.com.cyy.server2.net;

import java.util.HashMap;
import java.util.Map;

import cn.com.cyy.server2.finals.SharedprefenceFinals;

import android.content.Context;
import android.os.Handler;

/**
 * ��װMap���Ϸ�����������
 * 
 * @author hurenji
 * 
 */
public class SetMapToServer {

	public static Map<String, String> map = new HashMap<String, String>();

	/**
	 * ���ֻ������ȡ��֤��
	 * 
	 * @param context
	 *            ������
	 * @param handler
	 *            ��Ϣ����
	 * @param phone
	 *            �绰����
	 */
	public static void getCodeMap(Context context, Handler handler, String phone) {
		System.out.println("phone-->" + phone);
		map.put("mobile", phone);
		ConnNet.conn(context, handler, map , "getVerifyCode");
	}

	/**
	 * �û�ע��
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
	 * �û���¼
	 * @param contxt
	 * @param handler
	 * @param account	�˺�
	 * @param password  ����
	 */
	public static void userLogin(Context context , Handler handler , String account , String password){
		System.out.println("account--> " + account + " , " + "password--> " + password);
		map.put("mobile", account);
		map.put("password", password);
		map.put("longitude", "");		// ����
		map.put("latitude", "");		// γ��
		ConnNet.conn(context, handler, map , "login");
	}
	
	/**
	 * �޲�������
	 * @param context
	 * @param handler
	 * @param parameter	��������
	 */
	public static void noParameter(Context context , Handler handler ,String parameter){
		ConnNet.conn(context, handler, map , parameter);
	}

}
