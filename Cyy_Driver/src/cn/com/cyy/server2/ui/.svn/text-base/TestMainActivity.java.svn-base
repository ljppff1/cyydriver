package cn.com.cyy.server2.ui;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import cn.com.cyy.server2.R;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatus.Builder;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

/**
 * 主界面Activity
 * 
 * @author hurenji
 */
public class TestMainActivity extends BaseActivity {

	// 定位相关的属性
	private LocationClient mLocClient;
	private MyLocationListener myListener = new MyLocationListener();
	private LocationMode mCurrentMode;
	private BitmapDescriptor mCurrentMarker;

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Button mBtTestMaiPosition;
	
	// UI相关
	private boolean isFirstLoc = true; // 是否首次定位
	private boolean isClick = false;		// 是否点击手动定位

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_main);

		// 定义地图显示类型
		mCurrentMode = LocationMode.NORMAL; // 普通显示

		// 初始化地图
		mMapView = (MapView) this.findViewById(R.id.bmapView);
		mBtTestMaiPosition = (Button) this.findViewById(R.id.mBtTestMaiPosition);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(18).build()));
		mCurrentMarker = null; // 当传入null时,使用百度地图自带的
		// 设置地图显示的一些基本属性
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, null));
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
