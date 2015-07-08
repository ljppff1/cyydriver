package cn.com.cyy.server2.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.cyy.server2.R;
import cn.com.cyy.server2.finals.HandlerFinals;
import cn.com.cyy.server2.finals.SharedprefenceFinals;
import cn.com.cyy.server2.net.CreateSocketJson;
import cn.com.cyy.server2.net.ParserJson;
import cn.com.cyy.server2.net.SetMapToServer;
import cn.com.cyy.server2.service.SocketNetService;
import cn.com.cyy.server2.util.AppManager;

/**
 * 用户登录的Activity
 * 
 * @author hurenji
 * 
 */
public class LoginActivity extends BaseActivity  {

	// 定义主布局控件
	private EditText mEtLoginAccout;
	private EditText mEtLoginPassword;
	private Button mBtLoginEntry;
	private TextView mTvLoginForgetPwd;
	private Button mBtLoginRegister;
	private long exitTime = 0;
	// 定义跳转的Intent
	private Intent skipIntent;
	
	// 用户登录时的提示对话框
	private ProgressDialog loginDialog = null;
	
	// 用户登录
	private SharedPreferences isBoolean = null;

	// 用户登录的Handler
	private Handler loginHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String result = (String) msg.obj;
			switch (msg.what) {
			case HandlerFinals.JSON_INFO:
				System.out.println("getJson-->" + result);
				String msgs = ParserJson.loginInfoJson(result , LoginActivity.this);
				if(msgs.equals("登录成功")){
					//String str = CreateSocketJson.LoginInfoJson(LoginActivity.this);
					//SocketNetService.ioInfo(str);
					Toast.makeText(getApplication(), msgs, Toast.LENGTH_SHORT).show();
					isBoolean.edit().putBoolean("is_login", true).putString("username", mEtLoginAccout.getText().toString()).putString("password", mEtLoginPassword.getText().toString()).commit();
					skipIntent =new Intent();
					skipIntent.setClass(LoginActivity.this, MainActivity.class);
					LoginActivity.this.startActivity(skipIntent);
					loginDialog.cancel();
					AppManager.getAppManager().finishActivity();
				}
				break;
			case HandlerFinals.JSON_IO_ERROR:
				String msgs2 = ParserJson.loginInfoJson(result , LoginActivity.this);
				loginDialog.dismiss();
				Toast.makeText(getApplication(), msgs2, Toast.LENGTH_SHORT)
						.show();
				break;
			case HandlerFinals.JSON_IO_CONNECT:
				Toast.makeText(getApplicationContext(), "请检查网络", Toast.LENGTH_SHORT).show();
				break;

			}
		}
	};
	private LinearLayout mLLLogin1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// 实例化
		isBoolean = getSharedPreferences("isBoolean", MODE_PRIVATE);
		String username1 = isBoolean.getString("username", "");
		String password1 = isBoolean.getString("password", "");
		// 实例化控件
		mEtLoginAccout = (EditText) this.findViewById(R.id.mEtLoginAccout);
		mEtLoginPassword = (EditText) this.findViewById(R.id.mEtLoginPassword);
		mEtLoginAccout.setText(username1);
		mEtLoginPassword.setText(password1);
		mBtLoginEntry = (Button) this.findViewById(R.id.mBtLoginEntry);
		mTvLoginForgetPwd = (TextView) this
				.findViewById(R.id.mTvLoginForgetPwd);
		mBtLoginRegister = (Button) this.findViewById(R.id.mBtLoginRegister);
        
 
		// 初始化配置
		mTvLoginForgetPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 为忘记密码设置下划线
		// 设置监听
		mBtLoginEntry.setOnClickListener(listener);
		mTvLoginForgetPwd.setOnClickListener(listener);
		mBtLoginRegister.setOnClickListener(listener);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 进入应用需打开GPS
		//openHintGPS();
	}
	
	OnClickListener listener =new OnClickListener() {
		
		// 控件点击监听的公共方法
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mBtLoginEntry: // 登录按钮
				/*
				 * skipIntent.setClass(LoginActivity.this, MainActivity.class);
				 * LoginActivity.this.startActivity(skipIntent);
				 */
				String account = mEtLoginAccout.getText().toString().trim();
				String password = mEtLoginPassword.getText().toString().trim();
				if (account.equals("") || password.equals("")) {
					Toast.makeText(getApplication(), "请填写正确的账号或者密码",
							Toast.LENGTH_SHORT).show();
				} else {
					loginDialog = new ProgressDialog(LoginActivity.this);
					loginDialog.setMessage("登录中,请稍候...");
					loginDialog.show();
					SetMapToServer.userLogin(LoginActivity.this, loginHandler, account, password);
				}

				break;
			case R.id.mTvLoginForgetPwd: // 忘记密码

				break;
			case R.id.mBtLoginRegister: // 注册按钮
				SharedprefenceFinals.instantiationShared(getApplication());
		
				
				skipIntent=new Intent();
				skipIntent.setClass(LoginActivity.this, RegFirstActivity.class);
				startActivity(skipIntent);
				break;
			}
		}	};


		
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				
				AppManager.getAppManager().AppExit(getApplicationContext());
				android.os.Process.killProcess(android.os.Process.myPid());
			}
			return true;
			}
		return true;
	}

	// 弹出打开GPS的对话框,询问用户是否打开GPS
	@SuppressWarnings("deprecation")
	private void openHintGPS() {
		boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled(getContentResolver(),
				LocationManager.GPS_PROVIDER);

		if(gpsEnabled==false){	// 如果GPS未打开
			// 弹出对话框提示用户是否打开GPS
			Dialog dialog = new Dialog(LoginActivity.this , R.style.MyDialog);
			View dialogView = View.inflate(LoginActivity.this, R.layout.dialog_isopen_gps, null);
			dialog.setContentView(dialogView);
			dialog.show();
		}
	}
	
	
	

	
}
