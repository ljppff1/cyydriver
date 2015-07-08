package cn.com.cyy.server2.net;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.com.cyy.server2.finals.BroadCastFinals;
import cn.com.cyy.server2.service.SocketNetService;
import cn.com.cyy.server2.util.NetListenerUtil;

/**
 * Socket读写的线程
 * 
 * @author hurenji
 * 
 */
public class SocketIOWriterNet extends Thread {

	private JSONObject jsonObject;
	private Context context;

	private boolean isMainInit = true;

	public SocketIOWriterNet(Context context) {
		this.context = context;
	}

	/**
	 * 发送
	 * 
	 * @param str
	 */
	public void SetStr(String str) {
		try {
			if (SocketNetService.bw != null) {
				SocketNetService.bw.write(str);
				SocketNetService.bw.flush();
			} else {
				NetListenerUtil.clostAllNet(context);
			}
		} catch (Exception e) {
			/*
			 * System.out.println("网络错误...哈哈哈哈哈");
			 * NetListenerUtil.clostAllNet(context);
			 */
			e.printStackTrace();
		}
	}

	public void getStr() {
		try {
			while (isMainInit = true) {
				System.out.println("whiele vdsfgvsdf");
				try {
					SocketNetService.socket.sendUrgentData(0xFF);
					System.out.println("已发送");
				} catch (Exception e) {
					System.out.println("Error" + e); // 出错，则打印出错信息
					System.out.println("网络错误...哈哈哈哈哈");
					NetListenerUtil.clostAllNet(context);
					break;
				}
				String line = SocketNetService.br.readLine();
				if (line != null) {
					System.out.println("line-->" + line);
					jsonObject = new JSONObject(line);
					Intent broadIntent = new Intent();
					broadIntent.setAction(BroadCastFinals.BROAD_OPER_CONN);
					Bundle bundle = new Bundle();
					bundle.putString("action", jsonObject.getString("action"));
					bundle.putString("json", line);
					broadIntent.putExtra("bundle", bundle);
					context.sendBroadcast(broadIntent);
				}
				SocketNetService.br.ready();
				Thread.sleep(200);
			}
		} catch (IOException e) {
			e.printStackTrace();
			getStr();
			// System.out.println("网络错误...哈哈哈哈哈--------");
			// NetListenerUtil.clostAllNet(context);
		} catch (InterruptedException e) {
			getStr();
			e.printStackTrace();
		} catch (JSONException e) {
			getStr();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setInitBooleanMain(boolean bool) {
		isMainInit = bool;
	}

	/**
	 * 向服务器请求时的io流
	 * 
	 * @param str
	 *            要请求的数据
	 */
	@Override
	public void run() {

//		/getStr();

	}
}
