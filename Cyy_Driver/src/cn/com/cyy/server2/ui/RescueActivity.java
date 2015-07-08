package cn.com.cyy.server2.ui;

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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RescueActivity extends BaseActivity{
	// ��λ��ص�����
	private LocationClient mLocClient;
	private MyLocationListener myListener = new MyLocationListener();
	private LocationMode mCurrentMode;
	private BitmapDescriptor mCurrentMarker;
	//���Ŀ���
	private Marker mMarkerA;
	private InfoWindow mInfoWindow;
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.loading_point_medium);

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Button mBtTestMaiPosition;
	
	// UI���
	private boolean isFirstLoc = true; // �Ƿ��״ζ�λ
	private boolean isClick = false;		// �Ƿ����ֶ���λ
	private Button mBtMainServer;   //ȷ�Ͻӵ�
	private Button mBtMainStatus;
	private TextView mTvActionBarCenter;
	private TextView mTVPhoneNumber;
	private TextView mTVBackReturn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_rescue);
        initView();
		// �����ͼ��ʾ����
		mCurrentMode = LocationMode.NORMAL; // ��ͨ��ʾ

		// ��ʼ����ͼ
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
					button.setText("������");
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
		//mCurrentMarker = null; // ������nullʱ,ʹ�ðٶȵ�ͼ�Դ���
		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.sns_shoot_location_pressed);
		// ���õ�ͼ��ʾ��һЩ��������
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker));
		// ������λͼ��
		mBaiduMap.setMyLocationEnabled(true);
		// ��λ��ʼ��
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		
		
		// �ֶ���λ�ļ���
		mBtTestMaiPosition.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isClick = true;
				Toast.makeText(getApplication(), "���ڶ�λ...", Toast.LENGTH_SHORT).show();
				mLocClient.requestLocation();
			}
		});
		
		
	}
    
	private void initOverlay() {
		LatLng llA = new LatLng(22.663175, 113.650244);
		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
				.zIndex(9).draggable(true);
		mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
        mBaiduMap.addOverlay(ooA);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(llA));
	}

	//��ʼ������
	private void initView() {
		mBtMainServer =(Button)this.findViewById(R.id.mBtMainServer);
		mBtMainServer.setText(this.getResources().getString(R.string.dorescue));
		mBtMainServer.setOnClickListener(listener);
		mBtMainStatus =(Button)this.findViewById(R.id.mBtMainStatus);
		mBtMainStatus.setText(this.getResources().getString(R.string.stop));
		mTvActionBarCenter =(TextView)this.findViewById(R.id.mTvActionBarCenter);
		mTvActionBarCenter.setText(this.getResources().getString(R.string.app_name_title_go));
		mTVPhoneNumber =(TextView)this.findViewById(R.id.mTVPhoneNumber);
		mTVPhoneNumber.setOnClickListener(listener);
		mTVBackReturn =(TextView)this.findViewById(R.id.mTVBackReturn);
		mTVBackReturn.setOnClickListener(listener);
	}

	OnClickListener listener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mTVPhoneNumber:
				  Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+10086));     
				  startActivity(intent);     
				break;
			case R.id.mTVBackReturn:
				  finish();
				break;
			case R.id.mBtMainServer:
				startActivity(new Intent(RescueActivity.this,ExecuteOrderActivity.class));
				break;

			default:
				break;
			}
			
		}
	};
	
	// ��λSDK�ļ�������
	public class MyLocationListener implements BDLocationListener {

		// ���ض�λ���
		@Override
		public void onReceiveLocation(BDLocation arg0) {
			//Toast.makeText(getApplication(), "onReceiveLocation", Toast.LENGTH_SHORT).show();
			// map view ���ٺ��ٴ����½��յ�λ��
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

		// ����POI��ѯ���
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
		// �˳�ʱ���ٶ�λ
		mLocClient.stop();
		// �رն�λͼ��
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

}

	
	

