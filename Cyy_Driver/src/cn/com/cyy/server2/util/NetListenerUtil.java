package cn.com.cyy.server2.util;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.com.cyy.server2.finals.BroadCastFinals;
import cn.com.cyy.server2.receiver.OperConnReceiver;
import cn.com.cyy.server2.service.SocketNetService;
import cn.com.cyy.server2.ui.MainActivity;

/**
 * 监听网络是否断开的工具类
 * 
 * @author hurenji
 */
public class NetListenerUtil {

	private static boolean isNetCon = true;

	/**
	 * 当Socket断开时，关闭所有和Socket有关的连接
	 */
	public static void clostAllNet(Context context) {

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
					// TODO Auto-generated catch block
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
			// 关闭服务
			
			  if(MainActivity.serviceIntent!=null){
			  context.stopService(MainActivity.serviceIntent); }
			
			/*if(Content.IS_CONNECT_NET){*/
			   boolean isLogin = context.getSharedPreferences("isBoolean", context.MODE_PRIVATE).getBoolean("is_login", false);
			   boolean isconnect =Content.IS_CONNECT_NET;
			  if(isconnect&&isLogin){
			Intent intent = new Intent();
			intent.setAction(BroadCastFinals.BROAD_LISTENER_SOCKET);
			Bundle bundle = new Bundle();
			bundle.putInt("flag", 0);
			intent.putExtra("bundle", bundle);
			context.sendBroadcast(intent);
			 }
			
		/*	}else{
				
			}*/

	}

}
