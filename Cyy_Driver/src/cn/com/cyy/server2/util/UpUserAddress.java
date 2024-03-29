package cn.com.cyy.server2.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;

import cn.com.cyy.server2.service.SocketNetService;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

/**
 * 上传用户当前位置的工具类
 * 
 * @author hurenji
 */
public class UpUserAddress {

	// 调用百度地图的实时定位，上传坐标位置
	public static LocationClient client;
	private Context context;
	
	private double latitude = 0.0;
	private double longitude = 0.0;
	private int timeOut = 40;
	private LatLng latLng1 = null;
	private LatLng latLng2 = null;
	
	private SharedPreferences userInfo;
	private JSONObject jsonObject = null;
	
	
	public UpUserAddress(Context context){
		this.context = context;
		userInfo = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		jsonObject = new JSONObject();
	}
	
	public void addressInfo(){
		Looper.prepare();
		System.out.println("addressInfo()");
		client = new LocationClient(context);
		client.registerLocationListener(new MyLoactionListener());
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setScanSpan(5000);
		client.setLocOption(option);
		Log.i("MYMYMY", "addressInfo");
		client.start();
		Looper.loop();
	}
	
	private class MyLoactionListener implements BDLocationListener{

		// 返回定位的坐标
		@Override
		public void onReceiveLocation(BDLocation arg0) {
			
			if(arg0 == null){
				System.out.println("arg0-->" +arg0);
				return; 
			}
			
		
			// 获取初始位置
				String latitude1 =userInfo.getString("loginLatitude", "") ;
				String longitude1 = userInfo.getString("loginLongitude", "") ;
				LatLng latLng11 = new LatLng(Double.valueOf(latitude1) , Double.valueOf(longitude1) );
				Log.i("MYMYMY", latitude1+"1111111111111"+longitude1);

			
			LatLng latLng22 = new LatLng(arg0.getLatitude() , arg0.getLongitude());
			Log.i("MYMYMY", arg0.getLatitude()+"2222222222222"+arg0.getLongitude());

			Log.i("MYMYMY","aaaaaaaaaaaaaaaaaaaaaaaa");
			Log.i("MYMYMY",DistanceUtil.getDistance(latLng11, latLng22)+"");
			if(DistanceUtil.getDistance(latLng11, latLng22)>=3.0||timeOut==0 ){
				//System.out.println("距离-->" + DistanceUtil.getDistance(latLng1, latLng2));
				// 两个坐标之间的距离大于50米，上传坐标
				upUserAddress(arg0.getLatitude()+"" , arg0.getLongitude()+"");
				latitude = arg0.getLatitude();
				longitude = arg0.getLongitude();
				latLng1 = new LatLng(latitude , longitude);
				timeOut = 28;
				Log.i("MYMYMY", latitude+"dddddd"+longitude);
				userInfo.edit().putString("loginLongitude", arg0.getLongitude()+"").putString("loginLatitude", arg0.getLatitude()+"").commit();
				//System.out.println("纬度: " + arg0.getLatitude());
				//System.out.println("经度: " + arg0.getLongitude());
			}
			timeOut--;
			//System.out.println("timeOut: " + timeOut);
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			
		}
		
	}
	
	/**
	 * 上传用户的坐标点
	 * @param lat	坐标的纬度
	 * @param longt	坐标的经度
	 */
	private void upUserAddress(String lat , String longt){
		try {
			jsonObject.put("uid" , userInfo.getString("uId", ""));
			jsonObject.put("company_id" , userInfo.getString("companyId", ""));
			jsonObject.put("action", "UpPosition");
			jsonObject.put("latitude", lat);
			jsonObject.put("longitude", longt);
			String json = "app" + jsonObject.toString() + "\n";
			if(SocketNetService.writerThread!=null){
			SocketNetService.writerThread.SetStr(json);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void stopLocationClient(){
		if(client!=null){
		client.stop();
		client = null;
		}
	}
	
	public boolean isStartClient(){
		return client.isStarted();
	}
	
	/*private LocationManager locationManager;
	private static UpUserAddress mUpUserAddress;
	private static Context context;
	private static MyLocationListener listener;

	// 1.构造方法
	private UpUserAddress() {
	};

	// 2.提供一个静态的方法 可以返回他的一个实例
	public static synchronized UpUserAddress getInstance(Context context) {

		if (mUpUserAddress == null) {

			synchronized (UpUserAddress.class) {
				if (mUpUserAddress == null) {
					mUpUserAddress = new UpUserAddress();
					UpUserAddress.context = context;
				}
			}
		}
		return mUpUserAddress;
	}

	// 获取GPS信息
	public void getLocation() {

		Looper.prepare();
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// 获取所有的定位方式
		// locationManager.getAllProviders();
		// 获取当前手机最好的位置提供者
		String provider = getProvider(locationManager);
		System.out.println("provider -->" + provider);
		// 注册监听事件
		// 每个一分钟获取当前位置信息
		// 位置每改变50米重新获取位置信息
		// getListener()位置发生改变时的回调方法
		locationManager.requestLocationUpdates(provider, 1000, 50,
				getListener());
		// 获取第一次的位置信息
		//Location lt = locationManager.getLastKnownLocation(provider);
		//System.out.println("经度-->" + lt.getLongitude() + " 纬度-->"
				//+ lt.getLatitude());
		Looper.loop();
	}

	// 停止GPS监听
	public void stopGPSListener() {
		locationManager.removeUpdates(getListener());
	}

	// 获取GPS监听实例
	private synchronized MyLocationListener getListener() {

		if (listener == null) {
			synchronized (UpUserAddress.class) {
				if (listener == null) {
					System.out.println("实例化LoactionListener");
					listener = new MyLocationListener();
				}
			}
		}
		return listener;

	}

	// 定位的监听类
	private class MyLocationListener implements LocationListener {

		*//**
		 * 当手机位置发生改变的时候, 调用的方法
		 *//*
		@Override
		public void onLocationChanged(Location location) {
			System.out.println("经度-->" + location.getLongitude());
			System.out.println("纬度-->" + location.getLatitude());
		}

		*//**
		 * 某一个设备的状态发生改变的时候 调用 可用-->不可用 不可用-->可用 status 当前状态 extras 额外消息
		 *//*
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			System.out.println("当前状态-->" + status);
		}

		*//**
		 * 某个设备被打开
		 *//*
		@Override
		public void onProviderEnabled(String provider) {
			System.out.println(provider + "被打开");
		}

		*//**
		 * 某个设备被禁用
		 *//*
		@Override
		public void onProviderDisabled(String provider) {
			System.out.println(provider + "被禁用");
		}

	}

	*//**
	 * @param locationManager2
	 *            位置管理服务
	 * @return 最好的位置提供者
	 *//*
	private String getProvider(LocationManager locationManager2) {

		// 设置查询条件
		Criteria criteria = new Criteria();
		// 定位精准度
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 是否需要定位海拔高度
		criteria.setAltitudeRequired(false);
		// 对手机耗电量的要求(获取频率)
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// 对速度变化是否敏感
		criteria.setSpeedRequired(true);
		// 是否运行产生开销（费用）
		criteria.setCostAllowed(true);
		// 如果置为ture只会返回当前打开的gps设备
		// 如果置为false如果设备关闭也会返回
		return locationManager2.getBestProvider(criteria, true);
	}
*/
}
