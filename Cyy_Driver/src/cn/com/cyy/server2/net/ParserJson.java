package cn.com.cyy.server2.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import cn.com.cyy.server2.model.FirmNameModel;
import cn.com.cyy.server2.model.HelpMsgModel;
import cn.com.cyy.server2.model.ServerModel;
import cn.com.cyy.server2.util.Content;

/**
 * 获取的JSON解析
 * 
 * @author hurenji
 * 
 */
public class ParserJson {

	private static JSONObject jsonObject = null;
	

	// 解析用户登录是否成功
	public static String loginInfoJson(String json , Context context) {
		String info = "";
		try {
			System.out.println("json-->" + json);
			jsonObject = new JSONObject(json);
			JSONObject child = jsonObject.getJSONObject("userInfo");
			int code = jsonObject.getInt("err_code");
			System.out.println("json--> " + json);
			switch (code) { 
			case 0:
				info = "登录成功";
				SharedPreferences userInfo = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
				Editor ed = userInfo.edit();
				ed.putString("uId", child.getString("uid"));
				ed.putString("companyId", child.getString("company_id"));
				ed.putString("action", "appSockConnect");
				ed.putString("name", child.getString("name"));
				ed.putString("mobile", child.getString("mobile"));
				ed.putString("serviceName", child.getString("service_type_name"));
				ed.putString("serviceTypeID", child.getString("service_id"));
				ed.putString("loginLongitude", child.getString("loginLongitude"));
				ed.putString("loginLatitude", child.getString("loginLatitude"));
				ed.commit();
				break;
			case 10001:
				info = "用户不存在";
				break;
			case 10002:
				info = "密码错误";
				break;
			case 10004:
				info = "账号或密码错误ss";
				break;
			case 10005:
				info = "密码长度错误,长度在 6-16位之间";
				break;
			default:
				info = "登录失败,请稍候重新登录";
				break;
			}
			return info;
		} catch (JSONException e) {
			e.printStackTrace();
			return "拉取失败,请稍候重新登录";
		}
	}
	
	// 解析验证码是否成功
		public static String autoCodeInfoJson(String json , Context context) {
			String info = "";
			try {
				System.out.println("json-->" + json);
				jsonObject = new JSONObject(json);
				int code = jsonObject.getInt("err_code");
				switch (code) { 
				case 0:
					info = "请注意查收";
					break;
				case 20043:
					info = "获取验证码过于频繁";
					break;
				default:
					info = "验证码获取失败";
					break;
				}
				return info;
			} catch (JSONException e) {
				e.printStackTrace();
				return "拉取失败,请稍候重新登录";
			}
		}
	
		//验证注册是否成功
		// 解析用户登录是否成功
		public static String registerInfoJson(String json , Context context) {
			String info = "";
			try {
				System.out.println("json-->" + json);
				jsonObject = new JSONObject(json);
				int code = jsonObject.getInt("err_code");
				switch (code) { 
				case 0:
					info = "注册成功";
					break;
				case 20041:
					info = "验证码错误";
					break;
				case 20151:
					info = "账号已被注册";
					break;
				case 20034:
					info="手机号码格式错误";
					break;
				default:
					info = "注册失败";
					break;
				}
				return info;
			} catch (JSONException e) {
				e.printStackTrace();
				return "拉取失败,请稍候重新登录";
			}
		}
	// 公司名称和Id
	public static List<FirmNameModel> firmJson(String json){
		
		List<FirmNameModel> list = new ArrayList<FirmNameModel>();
		FirmNameModel model = null;
		try {
			jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			for (int i=0 ; i<jsonArray.length() ; i++){
				model = new FirmNameModel();
				model.setId(jsonArray.getJSONObject(i).getString("id"));
				model.setName(jsonArray.getJSONObject(i).getString("companyName"));
				list.add(model);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	// 服务名称和子项目
	public static List<Object> selectServerJson(String json){
		
		List<ServerModel> listParent = new ArrayList<ServerModel>();
		List<ServerModel> listChild = null;
		List<Object> listObject = new ArrayList<Object>();
		Map<String,List<ServerModel>> map = new HashMap<String,List<ServerModel>>();
		ServerModel modelParent = null;
		ServerModel modelChild = null;
		try {
			jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			for(int i=0 ; i<jsonArray.length() ; i++){
				listChild = new ArrayList<ServerModel>();
				modelParent = new ServerModel();
				modelParent.setId(jsonArray.getJSONObject(i).getString("id"));
				modelParent.setServierName(jsonArray.getJSONObject(i).getString("title"));
				listParent.add(modelParent);
				for(int j=0 ; j<jsonArray.getJSONObject(i).getJSONArray("child").length() ; j++){
					modelChild = new ServerModel();
					modelChild.setId(jsonArray.getJSONObject(i).getJSONArray("child").getJSONObject(j).getString("id"));
					modelChild.setServierName(jsonArray.getJSONObject(i).getJSONArray("child").getJSONObject(j).getString("title"));
					listChild.add(modelChild);
				}
				map.put(modelParent.getServierName() , listChild);
			}
			listObject.add(listParent);
			listObject.add(map);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listObject;
	}
	
	// 服务名称
	public static List<ServerModel> selectServerIdJson(String json){
		
		List<ServerModel> listParent = new ArrayList<ServerModel>();
		ServerModel modelParent = null;
		try {
			jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			for(int i=0 ; i<jsonArray.length() ; i++){
				modelParent = new ServerModel();
				modelParent.setId(jsonArray.getJSONObject(i).getString("id"));
				modelParent.setServierName(jsonArray.getJSONObject(i).getString("title"));
				listParent.add(modelParent);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listParent;
	}
	
	/**
	 * 获取援助任务的信息
	 * @param json	援助任务的信息
	 * @return 援助任务的信息的List集合
	 */
	public static List<HelpMsgModel> taskListInfo(String json){
		
		List<HelpMsgModel> list = new ArrayList<HelpMsgModel>();
		HelpMsgModel model = null;
		try {
			System.out.println("toJson-->" + json);
			jsonObject = new JSONObject(json);
			JSONObject child = jsonObject.getJSONObject("assignment");
			model = new HelpMsgModel();
			model.setTaskId(child.getString("id"));
			model.setServerType(child.getString("service_text"));
			model.setAddress(child.getString("address"));
			model.setLongitude(child.getString("longitude"));
			model.setLatitude(child.getString("latitude"));
			model.setUserName(child.getString("carUserName"));
			model.setUserPhone(child.getString("tel"));
			model.setUserCarNum(child.getString("car_number"));
			model.setUserCarType(child.getString("car_type"));
			model.setDownTime(System.currentTimeMillis()+(child.getInt("expire"))*1000);
			model.setTimeEnd(false);
			//model.setInsurer(child.getString(""));
			//model.setOther(child.getString(""));
			list.add(model);
			return list;
		} catch (JSONException e) {
			System.out.println("Task is error");
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 获取援助任务的信息1
	 * @param json	援助任务的信息
	 * @return 援助任务的信息的List集合
	 */
	public static HelpMsgModel taskListInfo1(String json){
		
		HelpMsgModel model = null;
		try {
			System.out.println("toJson-->" + json);
			jsonObject = new JSONObject(json);
			JSONObject child = jsonObject.getJSONObject("assignment");
			model = new HelpMsgModel();
			model.setTaskId(child.getString("id"));
			model.setServerType(child.getString("service_text"));
			model.setAddress(child.getString("address"));
			model.setLongitude(child.getString("longitude"));
			model.setLatitude(child.getString("latitude"));
			model.setUserName(child.getString("carUserName"));
			model.setUserPhone(child.getString("tel"));
			model.setUserCarNum(child.getString("car_number"));
			model.setUserCarType(child.getString("car_type"));
			model.setDownTime(System.currentTimeMillis()+Content.LONG_TIME);
			model.setTimeEnd(false);
			//model.setInsurer(child.getString(""));
			//model.setOther(child.getString(""));
			return model;
		} catch (JSONException e) {
			System.out.println("Task is error");
			e.printStackTrace();
			return null;
		}
		
	}
}
