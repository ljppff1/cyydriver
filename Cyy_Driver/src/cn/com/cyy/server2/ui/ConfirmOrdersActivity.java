package cn.com.cyy.server2.ui;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;

import cn.com.cyy.server2.R;
import cn.com.cyy.server2.model.HelpMsgModel;
import cn.com.cyy.server2.net.CreateSocketJson;
import cn.com.cyy.server2.service.SocketNetService;
import cn.com.cyy.server2.ui.TestMainActivity.MyLocationListener;
import cn.com.cyy.server2.util.AppManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmOrdersActivity extends BaseActivity{
	// 定位相关的属性
	private LocationClient mLocClient;
	private MyLocationListener myListener = new MyLocationListener();
	private LocationMode mCurrentMode;
	private BitmapDescriptor mCurrentMarker;
	//标记目标点
	private Marker mMarkerA;
	private InfoWindow mInfoWindow;
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.loading_point_medium);

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Button mBtTestMaiPosition;
	
	// UI相关
	private boolean isFirstLoc = true; // 是否首次定位
	private boolean isClick = false;		// 是否点击手动定位
	private Button mBtMainServer;   //确认接单
	private Button mBtMainStatus;
	private TextView mTvActionBarCenter;
	private TextView mTVPhoneNumber;
	private TextView mTVBackReturn;
	private String serverType;
	private String carInfo;
	private String insurer;
	private String userPhone;
	//服务类型
	private TextView mTVCarTypeName;
	//车辆信息
	private TextView mTVCarLicensePlate;
	//保险公司
	private TextView mTVInsuranceCompany;
	private String taskId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_confirm_order);
		initData();
        initView();
		// 定义地图显示类型
		mCurrentMode = LocationMode.NORMAL; // 普通显示

		// 初始化地图
		mMapView = (MapView) this.findViewById(R.id.bmapView);
		mBtTestMaiPosition = (Button) this.findViewById(R.id.mBtTestMaiPosition);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(18).build()));
		
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);
		initOverlay();
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(final Marker marker) {
				Button button = new Button(getApplicationContext());
				button.setBackgroundResource(R.drawable.popup);
				OnInfoWindowClickListener listener = null;
				if (marker == mMarkerA) {
					button.setText("到达这");
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							LatLng ll = marker.getPosition();
							LatLng llNew = new LatLng(ll.latitude + 0.005,
									ll.longitude + 0.005);
							marker.setPosition(llNew);
							mBaiduMap.hideInfoWindow();
						}
					};
					LatLng ll = marker.getPosition();
					mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
					mBaiduMap.showInfoWindow(mInfoWindow);
				}
				return true;
			}
		});
		//mCurrentMarker = null; // 当传入null时,使用百度地图自带的
		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.sns_shoot_location_pressed);
		// 设置地图显示的一些基本属性
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker));
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		
		
		// 手动定位的监听
		mBtTestMaiPosition.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isClick = true;
				Toast.makeText(getApplication(), "正在定位...", Toast.LENGTH_SHORT).show();
				mLocClient.requestLocation();
			}
		});
		
		
	}
    
	private void initData() {
		Bundle bundle =getIntent().getExtras();
	     serverType =bundle.getString("serverType");
	     carInfo =bundle.getString("carInfo");
	     insurer =bundle.getString("insurer");
	     userPhone =bundle.getString("userPhone");
	     taskId  =bundle.getString("taskId");
	}

	private void initOverlay() {
		LatLng llA = new LatLng(22.663175, 113.650244);
		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
				.zIndex(9).draggable(true);
		mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
        mBaiduMap.addOverlay(ooA);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(llA));
        
	}

	//初始化布局
	private void initView() {
		mBtMainServer =(Button)this.findViewById(R.id.mBtMainServer);
		mBtMainServer.setText(this.getResources().getString(R.string.order_makesure));
		mBtMainStatus =(Button)this.findViewById(R.id.mBtMainStatus);
		mBtMainStatus.setText(this.getResources().getString(R.string.outline));
		mTvActionBarCenter =(TextView)this.findViewById(R.id.mTvActionBarCenter);
		mTvActionBarCenter.setText(this.getResources().getString(R.string.app_name_title));
		mBtMainServer.setOnClickListener(listener);
		mTVPhoneNumber =(TextView)this.findViewById(R.id.mTVPhoneNumber);
		mTVPhoneNumber.setOnClickListener(listener);
		mTVBackReturn =(TextView)this.findViewById(R.id.mTVBackReturn);
		mTVBackReturn.setOnClickListener(listener);
		
		mTVCarTypeName =(TextView)this.findViewById(R.id.mTVCarTypeName);
		mTVCarTypeName.setText(serverType);
		mTVCarLicensePlate =(TextView)this.findViewById(R.id.mTVCarLicensePlate);
		mTVCarLicensePlate.setText(carInfo);
		mTVInsuranceCompany =(TextView)this.findViewById(R.id.mTVInsuranceCompany);
		mTVInsuranceCompany.setText(insurer);
		mTVPhoneNumber =(TextView)this.findViewById(R.id.mTVPhoneNumber);
		mTVPhoneNumber.setText(userPhone);
		
		
	}

	OnClickListener listener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mTVPhoneNumber:
				  Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+userPhone));     
				  startActivity(intent);     
				break;
			case R.id.mTVBackReturn:
				  finish();
				break;
			case R.id.mBtMainServer:
				makesureOrder();

			default:
				break;
			}
			
		}

		private void makesureOrder() {
			String str = CreateSocketJson
					.sendRequestGrab(ConfirmOrdersActivity.this,taskId);
			Log.i("JSON", str);
			if(SocketNetService.writerThread!=null){
			// 写
			SocketNetService.writerThread.SetStr(str);
			}
			
			//目前直接是成功的
		/*	Intent intent =new Intent(ConfirmOrdersActivity.this, RescueActivity.class);
			startActivity(intent);
			*/
		}
		
		
	};
	
	public static void nextPage(String json	,Context context) {
		try {
			Log.i("JSON", json.toString());
			JSONObject jsonObject = new JSONObject(json);
			int code=jsonObject.getInt("err_code");
			switch (code) {
			case 0:
				//目前直接是成功的
				Intent intent =new Intent(context.getApplicationContext(), RescueActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
				AppManager.getAppManager().finishActivity();
				break;
			case 20211:
				AppManager.getAppManager().finishActivity();
				Toast.makeText(context, "订单已被抢", 0).show();
				break;
				
			default:
				break;
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppManager.getAppManager().finishActivity();
			Toast.makeText(context, "抢单出错", 0).show();
			
		}
  
		
		
	}

	// 定位SDK的监听函数
	public class MyLocationListener implements BDLocationListener {

		// 返回定位结果
		@Override
		public void onReceiveLocation(BDLocation arg0) {
			//Toast.makeText(getApplication(), "onReceiveLocation", Toast.LENGTH_SHORT).show();
			// map view 销毁后不再处理新接收的位置
			if (arg0 == null || mMapView == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(arg0.getRadius()).direction(100)
					.latitude(arg0.getLatitude())
					.longitude(arg0.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc || isClick) {
				isFirstLoc = false;
				isClick = false;
				LatLng ll = new LatLng(arg0.getLatitude(), arg0.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}

		}

		// 返回POI查询结果
		@Override
		public void onReceivePoi(BDLocation arg0) {

		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

}

	
	

