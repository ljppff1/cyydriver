package cn.com.cyy.server2.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import cn.com.cyy.server2.R;
import cn.com.cyy.server2.model.HelpMsgModel;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

/**
 * 计算服务人员与车主之间的距离
 * 
 * @author hurenji
 */
public class CountRangeUtil {
	private Context context;
	private LocationClient client;
	private double longitude;
	private double latitude;
	private LatLng latLng1;
	private LatLng latLng2;
	private int time = 0;
	private SharedPreferences userInfo;

	private Handler handler;
	private Message msgs;
	private Map<String, Object> map;

	public CountRangeUtil(Context context, double longitude , double latitude, final GetRangeResult result) {
		this.context = context;
		this.latitude = latitude;
		this.longitude = longitude;
		time = Integer.parseInt(context.getResources().getString(
				R.string.down_time));
		userInfo = context.getSharedPreferences("userInfo",
				Context.MODE_PRIVATE);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Map<String, Object> map = (Map<String, Object>) msg.obj;
				int r = (int) map.get("ranges");
				int t = (int) map.get("time");
				result.getResult(t, r);

			}
		};

		init();
	}

	// 获取位置距离
	private void init() {
		// Looper.prepare();
		latLng1 = new LatLng(latitude, longitude);
		new Thread(new Runnable(){
			@Override
			public void run() {
				
			}
		}).start();
		String mLongitude = userInfo.getString("loginLongitude", "");
		String mLatitude = userInfo.getString("loginLatitude", "");
		latLng2 = new LatLng(Double.parseDouble(mLatitude), Double.parseDouble(mLongitude));
		double ranges = DistanceUtil.getDistance(latLng1, latLng2);

		/*
		 * client = new LocationClient(context);
		 * client.registerLocationListener(new MyLoactionListener());
		 * LocationClientOption option = new LocationClientOption();
		 * option.setOpenGps(true); option.setCoorType("bd09ll");
		 * option.setScanSpan(1000); client.setLocOption(option);
		 * client.start();
		 */
		// Looper.loop();

	}

	private class MyLoactionListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation arg0) {
			if (arg0 == null) {
				System.out.println("arg0-->" + arg0);
				return;
			}
			latLng2 = new LatLng(arg0.getLatitude(), arg0.getLongitude());
			double ranges = DistanceUtil.getDistance(latLng1, latLng2);

			if (time == 1) {
				client.stop();
			}
			time--;
			msgs = Message.obtain();
			map.put("ranges", ranges);
			map.put("time", time);
			msgs.obj = map;
			handler.sendMessage(msgs);
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
		}

	}

	public interface GetRangeResult {
		public void getResult(int t, int r);
	}

	/*
	 * private Context context; private LocationClient client; private double
	 * longitude; private double latitude; private LatLng latLng1; private
	 * LatLng latLng2; private int time = 20;
	 * 
	 * private Handler handler; private Message msgs; private Map<String,
	 * Object> map;
	 *//**
	 * 显示车主距离和任务的倒计时
	 * 
	 * @param p
	 *            任务的下标
	 * @param tashMap
	 *            任务的信息储存
	 * @param context
	 *            上下文
	 * @param textTime
	 *            倒计时
	 * @param list
	 *            任务
	 * @param longitude
	 *            经度
	 * @param latitude
	 *            纬度
	 * @param getRangeResult
	 *            传送给外部的接口类
	 */
	/*
	 * public CountRangeUtil(final List<HelpMsgModel> list, final int p, final
	 * Map<Object, Map<String, Object>> taskMap, Context context, final TextView
	 * textId, double longitude, double latitude, final GetRangeResult
	 * getRangeResult) { this.context = context; this.latitude = latitude;
	 * this.longitude = longitude; map = new HashMap<String, Object>(); handler
	 * = new Handler() {
	 * 
	 * @Override public void handleMessage(Message msg) { Map<String, Object> m
	 * = (Map<String, Object>) msg.obj; double r = (double) m.get("ranges"); int
	 * t = (int) m.get("time"); if
	 * (textId.getTag().equals(list.get(p).getTaskId())) { taskMap.put(p, m);
	 * getRangeResult.getResult(taskMap); }
	 * 
	 * textTime.setText(t+""); textRange.setText("距" + (int)r + "米");
	 * 
	 * super.handleMessage(msg); } }; init();
	 * 
	 * }
	 * 
	 * // 获取位置距离 private void init() { // Looper.prepare(); latLng1 = new
	 * LatLng(latitude, longitude); client = new LocationClient(context);
	 * client.registerLocationListener(new MyLoactionListener());
	 * LocationClientOption option = new LocationClientOption();
	 * option.setOpenGps(true); option.setCoorType("bd09ll");
	 * option.setScanSpan(1000); client.setLocOption(option); client.start(); //
	 * Looper.loop();
	 * 
	 * }
	 * 
	 * private class MyLoactionListener implements BDLocationListener {
	 * 
	 * @Override public void onReceiveLocation(BDLocation arg0) { if (arg0 ==
	 * null) { System.out.println("arg0-->" + arg0); return; } latLng2 = new
	 * LatLng(arg0.getLatitude(), arg0.getLongitude()); double ranges =
	 * DistanceUtil.getDistance(latLng1, latLng2);
	 * 
	 * if (time == 1) { client.stop(); } time--; msgs = Message.obtain();
	 * map.put("ranges", ranges); map.put("time", time); msgs.obj = map;
	 * handler.sendMessage(msgs); }
	 * 
	 * @Override public void onReceivePoi(BDLocation arg0) { }
	 * 
	 * }
	 * 
	 * public interface GetRangeResult { public void getResult(Map<Object,
	 * Map<String, Object>> taskMap); }
	 */
}
