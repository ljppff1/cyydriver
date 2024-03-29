package cn.com.cyy.server2.receiver;

import java.io.Serializable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.com.cyy.server2.finals.SocketActionFinals;
import cn.com.cyy.server2.model.HelpMsgModel;
import cn.com.cyy.server2.net.CreateSocketJson;
import cn.com.cyy.server2.net.ParserJson;
import cn.com.cyy.server2.service.SocketNetService;
import cn.com.cyy.server2.ui.ConfirmOrdersActivity;
import cn.com.cyy.server2.ui.ExecuteOrderActivity;
import cn.com.cyy.server2.ui.MainActivity;
import cn.com.cyy.server2.util.UpUserAddress;

/**
 * 处理Socket是的广播类
 * 
 * @author hurenji
 */

public class OperConnReceiver extends BroadcastReceiver {

	private Context context;
	private String action_result;
	private String json_result;
	
	private JSONObject jsonObject;
	public static UpUserAddress upUserAddress;
	
	private boolean isOpenAddress = false;
	
	private Intent intent;

	@Override
	public void onReceive(Context context, Intent intent) {

		this.context = context;
		
		Bundle bundle = intent.getBundleExtra("bundle");
		action_result = bundle.getString("action");
		json_result = bundle.getString("json");
		Log.i("MYMYMY", "OperConnReceiver");

		List<HelpMsgModel> helpMsgList;
		// System.out.println("code-->" + code_result +"\n" + "json-->" +
		// json_result);
		switch (action_result) {
		case SocketActionFinals.ACTION_RESPONSE_INIT:	// 初始化连接
			initConn();
			break;
			
		case SocketActionFinals.ACTION_TASK_INFO:		// 获取任务列表的信息
			System.out.println("ACTION_TASK_INFO");
			 helpMsgList = ParserJson.taskListInfo(json_result);

			 
			 
			 if(helpMsgList!=null){
				MainActivity.showListTask(helpMsgList , context);
//				Intent intent1 =new Intent("add_msgs_item");
//		        intent1 .putExtra("LIST", json_result);
//				 context.sendBroadcast(intent1); 
				 } 
			break;
		case SocketActionFinals.ACTION_CATCH_ASSIGNMENT:
			  ConfirmOrdersActivity.nextPage(json_result,context);
			break;
		case SocketActionFinals.ACTION_SCREEN_OFF:
			Log.i("MYMYMY","ScreenOpelddddddddddddddddddddddddd");
			ToServerAddress();
			break;
		
		}
			
		}

	
	
	// Socket初始化连接的方法
	private void initConn(){
		try {
			Log.i("MYMYMY", "initConn");

			jsonObject = new JSONObject(json_result);
			String code_result = jsonObject.getString("err_code");
			System.out.println("code_result-->" + code_result);
			if(code_result.equals("0")){	// 初始话成功，可采取后续操作
				
				if(!isOpenAddress){
					System.out.println("isOpenAddress-->" + isOpenAddress);
					isOpenAddress = true;
					ToServerAddress();
				}
				
			}else{	// 初始化失败。重新开始初始化
				if(isOpenAddress){
					isOpenAddress = false;
				}
				System.out.println("重新初始化");
				Thread.sleep(3000);
				alreadyConnInit();
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 初始化Socket成功后,启动上传用户地理坐标的信息
	private void ToServerAddress(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				upUserAddress = new UpUserAddress(context.getApplicationContext());
				upUserAddress.addressInfo();
				Log.i("MYMYMY", "ToServerAddress");

			}
		}).start();
	}
	
	private void alreadyConnInit(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i("MYMYMY", "alreadyConnInit");

				String str = CreateSocketJson.LoginInfoJson(context.getApplicationContext());
				System.out.println("str!!-->" +str);
				// 写
				SocketNetService.writerThread.SetStr(str);
				// 读
				SocketNetService.writerThread.getStr();
			}
		}).start();
	}

}
