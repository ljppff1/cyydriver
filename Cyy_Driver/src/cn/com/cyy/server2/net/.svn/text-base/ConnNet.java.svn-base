package cn.com.cyy.server2.net;

import hprose.client.HproseHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import cn.com.cyy.server2.finals.HandlerFinals;
import cn.com.cyy.server2.util.Content;

/**
 * 网络连接,并获取Json的工具类
 * 
 * @author hurenji
 */
public class ConnNet {

	public static HproseHttpClient client;
	private static String result = "";
	//public static HttpClient client;
	
	//private static BufferedReader br = null;

	public static void conn(final Context context, final Handler handler, final Map<String,String> map , final String medhod) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = null;
				try {
					
					
					
					TelephonyManager manager = (TelephonyManager) context
							.getSystemService(Context.TELEPHONY_SERVICE);
					String deviceId = manager.getDeviceId();
					/*client = new DefaultHttpClient();
					
					HttpPost post = new HttpPost("http://192.168.1.148/api.php?app=Android&version=1.0&deviceCode="
							+ deviceId);
					
					 List<NameValuePair> parameters = new ArrayList<NameValuePair>(); 
					Set keySet = map.keySet();
					Iterator it = keySet.iterator();
					while(it.hasNext()){
						parameters.add(new BasicNameValuePair((String)it.next(), map.get(it.next())));
						System.out.println("key-->" + (String)it.next() + "+++" + "value-->" +map.get(it.next()) );
					}
					UrlEncodedFormEntity formEntiry = new UrlEncodedFormEntity(parameters);
					post.setEntity(formEntiry);
					// 执行请求
					HttpResponse response = client.execute(post);
					
					br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					StringBuffer sb = new StringBuffer();
					String line = "";
					while((line = br.readLine())!=null){
						sb.append(line);
					}
					br.close();
					String result = sb.toString();
					System.out.println("result-->" + result);*/
					if(isNetwork(context)){
					client = new HproseHttpClient();
					client.useService(Content.HTTP+"/api.php?app=Android&version=1.0&deviceCode="
							+ deviceId);
					result = (String) client.invoke(medhod,
							new Object[] { map });
					System.out.println("result-->" + result);
					msg = Message.obtain();
					msg.what = HandlerFinals.JSON_INFO;
					msg.obj = result;
					handler.sendMessage(msg);
					}else{
						msg = Message.obtain();
						msg.what = HandlerFinals.JSON_IO_CONNECT;
						handler.sendMessage(msg);
					}
				} catch (IOException e) {
					String exception = e.getMessage();
						msg = Message.obtain();
						msg.what = HandlerFinals.JSON_IO_ERROR;
						msg.obj = exception;
						handler.sendMessage(msg);
					 e.printStackTrace();
					
				}
			}
		}).start();

	}
    public static boolean isNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager == null) {
            return false;
        }  
        if( connectivityManager.getActiveNetworkInfo()==null){
            return false;
        }
         return     connectivityManager.getActiveNetworkInfo().isAvailable();
    }

	
}
