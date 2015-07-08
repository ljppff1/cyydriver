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
 * ���������Ƿ�Ͽ��Ĺ�����
 * 
 * @author hurenji
 */
public class NetListenerUtil {

	private static boolean isNetCon = true;

	/**
	 * ��Socket�Ͽ�ʱ���ر����к�Socket�йص�����
	 */
	public static void clostAllNet(Context context) {

			// �ر��ϴ�λ��
			if (OperConnReceiver.upUserAddress != null && UpUserAddress.client !=null) {
					UpUserAddress.stopLocationClient();
					//OperConnReceiver.upUserAddress = null;
			}
			// �ر�IO��
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
			// �ر�Socket����
			if (SocketNetService.socket != null) {
				try {
					SocketNetService.socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SocketNetService.socket = null;
			}
			// �ر�����Socket���߳�
			if (SocketNetService.NetThread != null) {
				SocketNetService.NetThread = null;
			}
			// �˳�ѭ����ȡ���������ݵ�While
			if (SocketNetService.writerThread != null) {
				SocketNetService.writerThread.setInitBooleanMain(false);
			}
			// �ر����������д���ݵ��߳�
			if (SocketNetService.writerThread != null) {
				SocketNetService.writerThread = null;
			}
			// �رշ���
			
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
