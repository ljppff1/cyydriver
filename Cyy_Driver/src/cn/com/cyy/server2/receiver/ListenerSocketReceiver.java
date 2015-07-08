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
 * ����Socket״̬�Ĺ㲥
 * 
 * @author hurneji
 */
public class ListenerSocketReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {

		// �����ֻ������״̬

		Bundle bundle = intent.getBundleExtra("bundle");
		Log.i("MYMYMY","������������ж� a");

		if (bundle.getInt("flag") == 0 && bundle != null) { // Socket���ӶϿ�
			// ��һ������Ϊtrue
			SocketNetService.isFristConn = true;
			// ѭ��ʹ������
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Log.i("MYMYMY","������������ж� b");
						Thread.sleep(5000); // ����5��
						   boolean isLogin = context.getSharedPreferences("isBoolean", context.MODE_PRIVATE).getBoolean("is_login", false);
						if(Content.IS_CONNECT_NET&&isLogin&&!Content.IS_OFF_LINE_NET){
							Log.i("MYMYMY","������������ж� c");
							//context.stopService(MainActivity.serviceIntent);
						context.startService(MainActivity.serviceIntent);
						System.out.println("5��");
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
