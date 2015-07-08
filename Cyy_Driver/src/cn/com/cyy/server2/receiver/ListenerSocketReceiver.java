package cn.com.cyy.server2.receiver;

import cn.com.cyy.server2.service.SocketNetService;
import cn.com.cyy.server2.ui.MainActivity;
import cn.com.cyy.server2.util.Content;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

/**
 * 监听Socket状态的广播
 * 
 * @author hurneji
 */
public class ListenerSocketReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {

		// 监听手机网络的状态

		Bundle bundle = intent.getBundleExtra("bundle");
		Log.i("MYMYMY","到了这个网络判断 a");

		if (bundle.getInt("flag") == 0 && bundle != null) { // Socket连接断开
			// 第一次启动为true
			SocketNetService.isFristConn = true;
			// 循环使其连接
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Log.i("MYMYMY","到了这个网络判断 b");
						Thread.sleep(5000); // 休眠5秒
						   boolean isLogin = context.getSharedPreferences("isBoolean", context.MODE_PRIVATE).getBoolean("is_login", false);
						if(Content.IS_CONNECT_NET&&isLogin&&!Content.IS_OFF_LINE_NET){
							Log.i("MYMYMY","到了这个网络判断 c");
							//context.stopService(MainActivity.serviceIntent);
						context.startService(MainActivity.serviceIntent);
						System.out.println("5秒");
						}else{
							return;
						}
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		
		
		
		
		
		
	}

}
