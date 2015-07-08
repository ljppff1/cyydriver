package cn.com.cyy.server2.ui;

import java.io.IOException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.cyy.server2.R;
import cn.com.cyy.server2.net.CreateSocketJson;
import cn.com.cyy.server2.receiver.OperConnReceiver;
import cn.com.cyy.server2.service.SocketNetService;
import cn.com.cyy.server2.util.AppManager;
import cn.com.cyy.server2.util.Content;
import cn.com.cyy.server2.util.UpUserAddress;

/**
 * 用户设置的界面
 * @author hurenji
 */
public class SettingActivity extends BaseActivity{

	// 定义导航栏控件
	private ImageView mIvActionBarLeft;
	private RelativeLayout mRlSetting7;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		// 实例化导航栏控件
		mIvActionBarLeft = (ImageView) this.findViewById(R.id.mIvActionBarLeft);
		mIvActionBarLeft.setOnClickListener(listener);		
		mRlSetting7 =(RelativeLayout)this.findViewById(R.id.mRlSetting7);
		mRlSetting7.setOnClickListener(listener);
	}

	OnClickListener listener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mIvActionBarLeft:		// 返回到上一个Activity
				SettingActivity.this.finish();
				break;
			case R.id.mRlSetting7:
				cancelUser();
				break;
			}
		}
	};
	private SharedPreferences isBoolean;
	
	private void cancelUser() {
		Content.IS_CANCEL_LOGIN_NET =true;
             	// 关闭服务
				isBoolean = getSharedPreferences("isBoolean", MODE_PRIVATE);
				isBoolean.edit().putBoolean("is_login", false).commit();
				String str = CreateSocketJson.OffLineJson(SettingActivity.this);
				if(SocketNetService.writerThread != null){
				SocketNetService.writerThread.SetStr(str);
				}
			
					// 关闭上传位置
					if(OperConnReceiver.upUserAddress!=null||UpUserAddress.client!=null){
						UpUserAddress.stopLocationClient();
					}
					// 关闭IO流
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
					// 关闭Socket连接
					if (SocketNetService.socket != null) {
						try {
							SocketNetService.socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						SocketNetService.socket = null;
					}
					// 关闭启动Socket的线程
					if (SocketNetService.NetThread != null) {
						SocketNetService.NetThread = null;
					}
					// 退出循环读取服务器数据的While
					if (SocketNetService.writerThread != null) {
						SocketNetService.writerThread.setInitBooleanMain(false);
					}
					// 关闭向服务器读写数据的线程
					if (SocketNetService.writerThread != null) {
						SocketNetService.writerThread = null;
					}
				
				
				Intent intent =new Intent(SettingActivity.this,SocketNetService.class);
				this.stopService(intent);
				startActivity(new Intent(SettingActivity.this, LoginActivity.class));
				AppManager.getAppManager().finishAllActivity();
				
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
}
