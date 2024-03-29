package cn.com.cyy.server2.receiver;

import java.io.IOException;

import cn.com.cyy.server2.finals.BroadCastFinals;
import cn.com.cyy.server2.finals.SharedprefenceFinals;
import cn.com.cyy.server2.service.SocketNetService;
import cn.com.cyy.server2.ui.MainActivity;
import cn.com.cyy.server2.ui.SettingActivity;
import cn.com.cyy.server2.util.Content;
import cn.com.cyy.server2.util.UpUserAddress;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

public class ConnectReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();  
        
        
        if(activeInfo==null){
        	Log.i("MYERROR", "a无网络 aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
          Content.IS_CONNECT_NET=false;
			// 关闭上传位置
			if (OperConnReceiver.upUserAddress != null && UpUserAddress.client !=null) {
					UpUserAddress.stopLocationClient();
					//OperConnReceiver.upUserAddress = null;
			}
			// 关闭IO流
			if (SocketNetService.br != null) {
				try {
					SocketNetService.br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				SocketNetService.br = null;
			}
			if (SocketNetService.bw != null) {
				try {
					SocketNetService.bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SocketNetService.bw = null;
			}
			// 关闭Socket连接
			if (SocketNetService.socket != null) {
				try {
					SocketNetService.socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SocketNetService.socket = null;
			}
			// 关闭启动Socket的线程
			if (SocketNetService.NetThread != null) {
				SocketNetService.NetThread = null;
			}
			// 退出循环读取服务器数据的While
			if (SocketNetService.writerThread != null) {
				SocketNetService.writerThread.setInitBooleanMain(false);
			}
			// 关闭向服务器读写数据的线程
			if (SocketNetService.writerThread != null) {
				SocketNetService.writerThread = null;
			}
			
			Intent intent1 =new Intent(context.getApplicationContext(),SocketNetService.class);
			context.stopService(intent1);
            Log.i("MYMYMY", "没有网络时 ConnectReceiver");
			
        }else{
        	  Content.IS_CONNECT_NET=true;
  			Intent intent1 =new Intent(context.getApplicationContext(),SocketNetService.class);
  			context.startService(intent1);
  		  Log.i("MYMYMY", "网络时 ConnectReceiver");
        }
        
        
        
        
		
	}
	

}
