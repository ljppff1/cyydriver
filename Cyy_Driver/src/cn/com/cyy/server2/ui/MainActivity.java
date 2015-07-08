 package cn.com.cyy.server2.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.cyy.server2.R;
import cn.com.cyy.server2.adapter.HelpMsgAdapter;
import cn.com.cyy.server2.finals.BroadCastFinals;
import cn.com.cyy.server2.model.HelpMsgModel;
import cn.com.cyy.server2.net.CreateSocketJson;
import cn.com.cyy.server2.receiver.ConnectReceiver;
import cn.com.cyy.server2.receiver.ListenerSocketReceiver;
import cn.com.cyy.server2.receiver.OperConnReceiver;
import cn.com.cyy.server2.receiver.ScreenReceiver;
import cn.com.cyy.server2.service.SocketNetService;
import cn.com.cyy.server2.util.AppManager;
import cn.com.cyy.server2.util.Content;
import cn.com.cyy.server2.util.UpUserAddress;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

@SuppressWarnings("deprecation")
public class MainActivity extends BaseActivity implements View.OnClickListener {

	// ����ȫ�ֲ����ļ��Ŀؼ�
	private ImageView mIvActionBarLeft;
	private TextView mTvActionBarCenter;
	private ImageView mIvActionBarRight;

	// ���������ֿؼ�
	private TextView mTvMainHelpNum;
	private TextView mTvMainOnLineNum;
	private static LinearLayout mLlMainSomeInfo; // һЩ��Ϣ,λ����Ϣ,�ӵ�������
	private TextView mTvMainMyAddress; // �ҵ�λ����Ϣ
	// private LinearLayout mLlMainMsgs;
	private static ListView mLvMainMsgs;
	private Button mBtMainModel;
	private Button mBtMainServer;
	private Button mBtMainStatus;
	// ����Ĳ���
	private MapView mMapView;
	// ���ڰٶȵ�ͼ��������Զ���
	// ��λ��ص�����
	private LocationClient mLocClient;
	private LocationMode mCurrentMode;
	private BitmapDescriptor mCurrentMarker;
	private GeoCoder mSearch;

	private BaiduMap mBaiduMap;
	private Button mBtTestMaiPosition;

	// UI���
	private boolean isFirstLoc = true; // �Ƿ��״ζ�λ
	private boolean isClick = false; // �Ƿ����ֶ���λ
	private Marker mMarkerA;
	BitmapDescriptor bdA = null;

	// ����AddView�еĲ����ļ�
	private ImageView mIvLlViewCancel;
	private TextView mTvLlViewTime;
	private TextView mTvLlViewMsgs;

	// ����Dilaog�еĲ���
	private SlidingDrawer mSdDialogMainMsgSliding;
	private TextView mSdHandle;

	public static ListenerSocketReceiver lisSocketReceiver;

	// ����ChildView��View��id
	private int childID = 0;
	private View view;
	private CountDownTimer timer;

	// ����һ��ȫ�ֵ�Intent���ڽ�����ת
	private Intent skipIntent;
	// ��������Service��Inent
	public static Intent serviceIntent;

	// ��ȡ��Ļ�ĸ߿�
	private int width;
	private int height;
	private long exitTime = 0;

	// ������ť����ʾ(�ϻ���ͼ���»�����)
	private boolean isUpSlide = false;
	private float moveY = 0;
	private MyLocationListener myListener = new MyLocationListener();

	private CancelBro cancelBro = null; // ����һ��BroadCastReceiver
	private static List<HelpMsgModel> listAll = new ArrayList<HelpMsgModel>();
	private static HelpMsgAdapter adapter = null;

	
	// ��ȡ�Լ���λ����Ϣ
	private Handler addressHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Log.i("MYMYMY", "11111111111111111111111111");
				String addresss = (String) msg.obj;
				mTvMainMyAddress.setText(addresss);
				upUserAddress();
				break;
			case 2:
				initListView();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	private SharedPreferences userInfo;
	private JSONObject jsonObject;
	/**
	 * �ϴ��û��������
	 * @param lat	�����γ��
	 * @param longt	����ľ���
	 */
	private void upUserAddress(){
		try {
			userInfo = getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
			jsonObject = new JSONObject();
			jsonObject.put("uid" , userInfo.getString("uId", ""));
			jsonObject.put("company_id" , userInfo.getString("companyId", ""));
			jsonObject.put("action", "UpPosition");
			jsonObject.put("latitude",  userInfo.getString("loginLatitude", ""));
			jsonObject.put("longitude", userInfo.getString("loginLongitude", ""));
			String json = "app" + jsonObject.toString() + "\n";
			if(SocketNetService.writerThread!=null){
			SocketNetService.writerThread.SetStr(json);
			Log.i("MYMYMY", "asdfasdfasdfasdfasdf"+userInfo.getString("loginLatitude", "")+":"+userInfo.getString("loginLongitude", "")+"��д��д��д��д��д��д��д��д��д��д");

			}
			Log.i("MYMYMY", "asdfasdfasdfasdfasdf"+userInfo.getString("loginLatitude", "")+":"+userInfo.getString("loginLongitude", ""));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private ConnectReceiver cr;
	private boolean isLogin;
	private AddBro addBro;
	private ScreenReceiver screenReceiver;
	private static ImageView mIVgps;
	private static TextView mTVSpread;
	private static RelativeLayout mRLmapview1;
	private static RelativeLayout mRLlistview;
	private static RelativeLayout mRLmapview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Content.IS_CANCEL_LOGIN_NET=false;
		Log.i("MYMYMY", "ONCREATE");
		 isLogin = getSharedPreferences("isBoolean", MODE_PRIVATE).getBoolean("is_login", false);
		if(!isLogin){
			// ����Spend����
			MainActivity.this.startActivity(new Intent(MainActivity.this , SpendActivity.class));
			AppManager.getAppManager().finishActivity();
		}else{

		
		initServiceAndBroadcast();
		initView();
	    OperateMap();
		initListView();
		startService(serviceIntent);
	//	CreateAlarmManager();
		
		}
		
	}
	
	private void initServiceAndBroadcast() {
		// ��ʼ������socket��ʼ���Ĺ㲥
		IntentFilter socketFilter = new IntentFilter();
		socketFilter.addAction(BroadCastFinals.BROAD_LISTENER_SOCKET);
		lisSocketReceiver = new ListenerSocketReceiver();
		this.registerReceiver(lisSocketReceiver, socketFilter);
		
		IntentFilter connectReceiver =new IntentFilter();
		connectReceiver.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		 cr = new ConnectReceiver();
		this.registerReceiver(cr, connectReceiver);
		
		// ʵ����ȫ�ֵ���ת
		skipIntent = new Intent();
		serviceIntent = new Intent();
		// ��������
		serviceIntent.setClass(this, SocketNetService.class);
		serviceIntent.setAction("scyy.service.SocketNetService");
		try{
		// ��ʼ��ɾ����Ϣ��Item�Ĺ㲥
		cancelBro = new CancelBro();
		IntentFilter filter = new IntentFilter("cancel_msgs_item");
		this.registerReceiver(cancelBro, filter);
		}catch(Exception e){
			e.printStackTrace();
		}
	/*
		try{
		// ��ʼ������ ��Ϣ��Item�Ĺ㲥
		addBro = new AddBro();
		IntentFilter filter = new IntentFilter("add_msgs_item");
		this.registerReceiver(addBro, filter);
		}catch(Exception e){
			e.printStackTrace();
		}*/
		try{
			screenReceiver =new ScreenReceiver();
			IntentFilter filter =new IntentFilter();
		    filter.addAction(Intent.ACTION_SCREEN_ON);  
		    filter.addAction(Intent.ACTION_SCREEN_OFF);  
		    filter.addAction(Intent.ACTION_USER_PRESENT);  
            this.registerReceiver(screenReceiver, filter);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		

	}

	private void initView() {
		// ʵ����ȫ�ֲ����ļ�
		mIvActionBarLeft = (ImageView) this.findViewById(R.id.mIvActionBarLeft);
		mTvActionBarCenter = (TextView) this
				.findViewById(R.id.mTvActionBarCenter);
		mIvActionBarRight = (ImageView) this
				.findViewById(R.id.mIvActionBarRight);
		// ʵ���������ֿؼ�
		mTvMainHelpNum = (TextView) this.findViewById(R.id.mTvMainHelpNum);
		mTvMainOnLineNum = (TextView) this.findViewById(R.id.mTvMainOnLineNum);
		mLlMainSomeInfo = (LinearLayout) this
				.findViewById(R.id.mLlMainSomeInfo);
		mTvMainMyAddress = (TextView) this.findViewById(R.id.mTvMainMyAddress);
		
		// mLlMainMsgs = (LinearLayout) this.findViewById(R.id.mLlMainMsgs);
		mLvMainMsgs = (ListView) this.findViewById(R.id.mLvMainMsgs);
		mBtMainModel = (Button) this.findViewById(R.id.mBtMainModel);
		mBtMainServer = (Button) this.findViewById(R.id.mBtMainServer);
		mBtMainStatus = (Button) this.findViewById(R.id.mBtMainStatus);
		
		//������Ǹ���ͼ��Ӧ��һ��
		mLlMainSomeInfo =(LinearLayout)this.findViewById(R.id.mLlMainSomeInfo);
		mRLlistview =(RelativeLayout)this.findViewById(R.id.mRLlistview);
		mRLmapview =(RelativeLayout)this.findViewById(R.id.mRLmapview);
		mRLmapview1 =(RelativeLayout)this.findViewById(R.id.mRLmapview2);
		mIVgps =(ImageView)this.findViewById(R.id.mIVgps);
		mTVSpread =(TextView)this.findViewById(R.id.mTVSpread);
		mTVSpread.setOnClickListener(listener);
		
		mLlMainSomeInfo.setOnClickListener(listener);
		if(Content.IS_OFF_LINE_NET){
			mBtMainStatus.setText("����");
		}else{
		mBtMainStatus.setText("����");
		}
		mBtMainStatus.setOnClickListener(listener);
		mBtTestMaiPosition = (Button) this
				.findViewById(R.id.mBtTestMaiPosition);
		// ���ó�ʼ��ֵ
		mTvActionBarCenter.setText(this.getResources().getString(R.string.app_name_title));
		// ���ü����¼�
		mIvActionBarLeft.setOnClickListener(this);
		mIvActionBarRight.setOnClickListener(this);
		//mLvMainMsgs.setOnItemClickListener(this);
		//�ȴ���Ԯ
		mBtMainServer.setOnClickListener(this);
	}

	OnClickListener listener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mBtMainStatus:
				if(mBtMainStatus.getText().equals("����")){
					mBtMainStatus.setText("����");
					Content.IS_OFF_LINE_NET=true;
					String str = CreateSocketJson.OffLineJson(MainActivity.this);
					if(SocketNetService.writerThread != null){
					SocketNetService.writerThread.SetStr(str);
					}
					doOutLine();
				}else{
					mBtMainStatus.setText("����");
					Content.IS_OFF_LINE_NET=false;
					MainActivity.this.startService(serviceIntent);
				}
				break;
			case R.id.mLlMainSomeInfo:
				mRLmapview.setVisibility(View.VISIBLE);
				mRLmapview1.setVisibility(View.GONE);
				mTVSpread.setVisibility(View.VISIBLE);
				mIVgps.setVisibility(View.GONE);
				
				break;
			case R.id.mTVSpread:
				mRLmapview.setVisibility(View.GONE);
				mRLmapview1.setVisibility(View.VISIBLE);
				mIVgps.setVisibility(View.VISIBLE);
				mTVSpread.setVisibility(View.GONE);
				closeClient();

				break;

			default:
				break;
			}
			
		}

		private void doOutLine() {
			// �ر��ϴ�λ��
			if (OperConnReceiver.upUserAddress != null && UpUserAddress.client !=null) {
					UpUserAddress.stopLocationClient();
					Log.i("MYMYMY", "	UpUserAddress.stopLocationClient();");
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
            
			if(serviceIntent!=null){
				MainActivity.this.stopService(serviceIntent);
			}
			
		}

		private void closeClient() {
			if(mLocClient!=null){
				mLocClient.stop();
				mLocClient = null;
				}
		}
	};


	private void initListView() {
		for (int i = 0,len=listAll.size(); i <len; i++) {
		long aa=	listAll.get(i).getDownTime();
			long bb =System.currentTimeMillis();
			if(aa<bb){
				listAll.remove(i);
				len--;
				i--;
			}
		}
	   adapter = new HelpMsgAdapter(listAll, MainActivity.this, addressHandler );
	   mLvMainMsgs.setAdapter(adapter);
	if(listAll.size()!=0){
		mLlMainSomeInfo.setVisibility(View.GONE);
		mRLmapview1.setVisibility(View.GONE);
		mRLmapview.setVisibility(View.GONE);
		mRLlistview.setVisibility(View.VISIBLE);
		adapter.notifyDataSetChanged();
	}else{
		mLlMainSomeInfo.setVisibility(View.VISIBLE);
		mRLmapview.setVisibility(View.GONE);
		mRLmapview1.setVisibility(View.VISIBLE);
		mIVgps.setVisibility(View.VISIBLE);
		mTVSpread.setVisibility(View.GONE);
		mRLlistview.setVisibility(View.GONE);
	}
		
		
/*		for (int i = 0; i < listAll.size(); i++) {
			if(listAll.get(i).getTimeEnd()){
				listAll.remove(i);
				Log.i("MYMYOO", i+" initListView remove ");
			}
		}

		if(listAll.size()!=0){
		adapter = new HelpMsgAdapter(listAll, MainActivity.this);
		mLvMainMsgs.setAdapter(adapter);
		
		mLlMainSomeInfo.setVisibility(View.GONE);
		mRLmapview.setVisibility(View.GONE);
		mRLlistview.setVisibility(View.VISIBLE);
		
		
		mRLmapview1.setVisibility(View.GONE);

		adapter.notifyDataSetChanged();
		}else{
			mLlMainSomeInfo.setVisibility(View.VISIBLE);
			mRLmapview.setVisibility(View.GONE);
			mRLlistview.setVisibility(View.GONE);
			mRLmapview1.setVisibility(View.VISIBLE);
			mIVgps.setVisibility(View.VISIBLE);
			mTVSpread.setVisibility(View.GONE);
		}
		*/
	}

	private void CreateAlarmManager() {
		
		//�õ�ʹ��AlarmManager��Ȩ��
		AlarmManager alarms=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
		//AlarmManagerҪ��������,��NotificationService(���Ǹ��Զ���Service�Ժ�Ჹ��)
		Intent intent=new Intent(this,SocketNetService.class);
		PendingIntent pendingIntent=PendingIntent.getService(this,0,intent,0);
	    //��ʼʱ��
	    long firstime=SystemClock.elapsedRealtime();
		//60��һ�����ڣ���ͣ�ķ��͹㲥
	    alarms.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, 5*60*1000, pendingIntent);
	    
	}
	public static void cancelUpdateBroadcast(Context ctx){
	    AlarmManager am = getAlarmManager(ctx);
            // ȡ��ʱע��UpdateReceiver.class����������ʱһ��,������Ҫ��ȷȡ��
	    Intent i = new Intent(ctx, SocketNetService.class);  
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, i, 0);
	    am.cancel(pendingIntent);
	}
    public static AlarmManager getAlarmManager(Context ctx){
	return (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
}

	private void OperateMap() {
		mMapView = (MapView) this.findViewById(R.id.bmapView);
		// ��ʼ������������
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(new GetCodeAddress());
		// ���ó����������ͼ��
		bdA = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
		// mCurrentMode = LocationMode.NORMAL; // ��ͨ��ʾ
		mCurrentMode = LocationMode.FOLLOWING; // ����
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapStatus(MapStatusUpdateFactory
				.newMapStatus(new MapStatus.Builder().zoom(18).build()));
		mCurrentMarker = null; // ������nullʱ,ʹ�ðٶȵ�ͼ�Դ���

		// ���õ�ͼ��ʾ��һЩ��������
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, null));
		// ������λͼ��
		mBaiduMap.setMyLocationEnabled(true);
		// ��λ��ʼ��
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setScanSpan(200000000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		// �ֶ���λ�ļ���
		mBtTestMaiPosition.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isClick = true;
				Toast.makeText(getApplication(), "���ڶ�λ...", Toast.LENGTH_SHORT)
						.show();
				Log.i("MYMYMY", "mLocClient___mBtTestMaiPosition");
				if(mLocClient!=null){
				mLocClient.requestLocation();
				}
				
			}
		});
	}

	// ����������
	public class GetCodeAddress implements OnGetGeoCoderResultListener {
		@Override
		public void onGetGeoCodeResult(GeoCodeResult arg0) {
		}

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
			Message msg = Message.obtain();
			msg.obj = arg0.getAddress();
			msg.what=1;
			addressHandler.sendMessage(msg);
		}

	}
	// ��λSDK�ļ�������
	public class MyLocationListener implements BDLocationListener {

		// ���ض�λ���
		@Override
		public void onReceiveLocation(BDLocation arg0) {
			// map view ���ٺ��ٴ����½��յ�λ��
			Log.i("MYMYMY", arg0.getLongitude()+":asdfasdfloginLatitude"+ arg0.getLatitude()+"");

			if (arg0 == null || mMapView == null) {
				return;
			}
			if (isFirstLoc || isClick) {
				isFirstLoc = false;
				isClick = false;
				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(arg0.getRadius()).direction(100)
						.latitude(arg0.getLatitude())
						.longitude(arg0.getLongitude()).build();
				mBaiduMap.setMyLocationData(locData);
				LatLng ll = new LatLng(arg0.getLatitude(), arg0.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
				getSharedPreferences("userInfo", MODE_PRIVATE).edit().putString("loginLongitude", arg0.getLongitude()+"").putString("loginLatitude", arg0.getLatitude()+"").commit();
				Log.i("MYMYMY", arg0.getLongitude()+":asdfasdfloginLatitude"+ arg0.getLatitude()+"");
		//		addressHandler.sendMessage(Message.obtain());
				mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
				
				
			}

		}

		// ����POI��ѯ���
		@Override
		public void onReceivePoi(BDLocation arg0) {

		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i("MYMYMY", "ONSTART");
		if(isLogin){
			
/*			for (int i = 0,len=listAll.size(); i <len; i++) {
				if(listAll.get(i).getTimeEnd()){
					listAll.remove(i);
					len--;
					i--;
				}
			}
*/		
/*			for (int i = 0,len=listAll.size(); i <len; i++) {
			long aa=	listAll.get(i).getDownTime();
				long bb =System.currentTimeMillis();
				if(aa<bb){
					listAll.remove(i);
					len--;
					i--;
				}
			}
		adapter = new HelpMsgAdapter(listAll, MainActivity.this);
		mLvMainMsgs.setAdapter(adapter);
		if(listAll.size()!=0){
			mLlMainSomeInfo.setVisibility(View.GONE);
			mRLmapview1.setVisibility(View.GONE);
			mRLmapview.setVisibility(View.GONE);
			mRLlistview.setVisibility(View.VISIBLE);
			
			adapter.notifyDataSetChanged();
		}else{
			mLlMainSomeInfo.setVisibility(View.VISIBLE);
			mRLmapview.setVisibility(View.GONE);
			mRLmapview1.setVisibility(View.VISIBLE);
			mIVgps.setVisibility(View.VISIBLE);
			mTVSpread.setVisibility(View.GONE);

			mRLlistview.setVisibility(View.GONE);
		}
*/		
			initListView();
		}
		// new Thread(new addRunnable()).start();
	}

	// ��ȡ�����������������񣬽�����ʾ��ListView��
	public static void showListTask(List<HelpMsgModel> lists, Context context) {
		listAll.add(lists.get(0));
		
		if (listAll.size() > 0) {
			//ע��
/*			SmallUtil smallUtil = SmallUtil.newInstance();
			smallUtil.vibraAlert(context);
			if(SmallUtil.mp!=null&&SmallUtil.mp.isPlaying()==false){
				SmallUtil.mp.start();
			}
*/	
			addNotification(context,lists);
            mLlMainSomeInfo.setVisibility(View.GONE);
            mRLmapview.setVisibility(View.GONE);
            mRLmapview1.setVisibility(View.GONE);
            mRLlistview.setVisibility(View.VISIBLE);
    		mLvMainMsgs.setAdapter(adapter);
            adapter.notifyDataSetChanged();
           }else{
	        mLlMainSomeInfo.setVisibility(View.VISIBLE);
           	mRLmapview.setVisibility(View.GONE);
	        mRLlistview.setVisibility(View.GONE);
	        mRLmapview1.setVisibility(View.VISIBLE);
			mIVgps.setVisibility(View.VISIBLE);
			mTVSpread.setVisibility(View.GONE);

		}
	}

	

	private static void addNotification(Context context,List<HelpMsgModel> lists) {
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.icon = R.drawable.icon;
		notification.tickerText = "����֪ͨ";
		notification.defaults = Notification.DEFAULT_SOUND;
		notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;
		
		Intent intent = new Intent(context, MainActivity.class);
		
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		//FLAG_NO_CREATEһֱ�ܵ�ǰ̨�ģ�FLAG_UPDATE_CURRENTҲ�ǿ��Ե�ǰ̨һֱ��
		notification.setLatestEventInfo(context,"����֪ͨ","��"+lists.get(0).getUserName()+"��������", pendingIntent);
		manager.notify(R.drawable.icon, notification);
		
	}

	// �����㲥���յ���
	private class CancelBro extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			Bundle bundle = intent.getBundleExtra("bundle");
			int p = bundle.getInt("position");

			listAll.remove(p);  

			adapter.notifyDataSetChanged();

		}
	}
	// �����㲥���յ���
	private class AddBro extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//HelpMsgModel	 helpMsgList =(HelpMsgModel) getIntent().getSerializableExtra("LIST");
		String resultjson =	getIntent().getStringExtra("LIST");
/*			if(helpMsgList!=null){
			listAll.add(helpMsgList);
*/			if (listAll.size() > 0) {
				//ע��
	/*			SmallUtil smallUtil = SmallUtil.newInstance();
				smallUtil.vibraAlert(context);
				if(SmallUtil.mp!=null&&SmallUtil.mp.isPlaying()==false){
					SmallUtil.mp.start();
				}
	*/		
				//addNotification(context,helpMsgList);
	            mLlMainSomeInfo.setVisibility(View.GONE);
	            mRLmapview.setVisibility(View.GONE);
	            mRLmapview1.setVisibility(View.GONE);
	            mRLlistview.setVisibility(View.VISIBLE);
	    	//	adapter = new HelpMsgAdapter(listAll, MainActivity.this);
	    		mLvMainMsgs.setAdapter(adapter);
	            adapter.notifyDataSetChanged();
	           }else{
		        mLlMainSomeInfo.setVisibility(View.VISIBLE);
	           	mRLmapview.setVisibility(View.GONE);
		        mRLlistview.setVisibility(View.GONE);
		        mRLmapview1.setVisibility(View.VISIBLE);
				mIVgps.setVisibility(View.VISIBLE);
				mTVSpread.setVisibility(View.GONE);

			}

			}
		
	}

	// ���пؼ��ĵ���¼�����
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mIvActionBarLeft:
			
			//File file = new File("file:///assets/testTaskList.txt");
			try {
				
				InputStream in = getResources().getAssets().open("testTaskList.txt");;
				byte[] b = new byte[in.available()];
				int len = 0;
				String res = "";
				while((len = in.read(b))!=-1){
					res = new String(b , 0 , len);
				}
				in.close();
				JSONObject jsonObject = new JSONObject(res);
				Intent broadIntent = new Intent();
				broadIntent.setAction(BroadCastFinals.BROAD_OPER_CONN);
				Bundle bundle = new Bundle();
				bundle.putString("action", jsonObject.getString("action"));
				bundle.putString("json", res);
				broadIntent.putExtra("bundle", bundle);
				MainActivity.this.sendBroadcast(broadIntent);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.mIvActionBarRight: // �������ý���
			skipIntent.setClass(MainActivity.this, SettingActivity.class);
			MainActivity.this.startActivity(skipIntent);
			
			//overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

			break;
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(false);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	@Override
	protected void onDestroy() {
	
/*		// �˳�ʱ���ٶ�λ
		mLocClient.stop();
		// �رն�λͼ��
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
*/		// �ر��û�λ���ϴ����߳�
		// UpUserAddress.isMainStart = false;
		// �ر�Socket��ͨ�Ĺ㲥
		/*		
	if(isLogin){
		if(serviceIntent!=null){
	    MainActivity.this.stopService(serviceIntent);
		}
		if(lisSocketReceiver!=null){
		MainActivity.this.unregisterReceiver(lisSocketReceiver);
		}
		if(cr!=null){
		MainActivity.this.unregisterReceiver(cr);
		}
		// �ر��ϴ�λ��
		if (OperConnReceiver.upUserAddress != null
				&& UpUserAddress.client != null) {
			UpUserAddress.stopLocationClient();
			// OperConnReceiver.upUserAddress = null;
		}
		}
*/
		
		super.onDestroy();

		
	}

}