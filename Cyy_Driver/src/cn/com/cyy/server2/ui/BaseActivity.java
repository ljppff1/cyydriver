package cn.com.cyy.server2.ui;

import java.io.File;

import cn.com.cyy.server2.util.AppManager;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class BaseActivity extends Activity{
	// 是否允许全屏
	private boolean allowFullScreen = true;
	// 是否允许销毁
	private boolean allowDestroy = true;

	private View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		allowFullScreen = true;
		AppManager.getAppManager().addActivity(this); // 添加Activity到堆栈
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	//	AppManager.getAppManager().finishActivity(this.getClass()); // 结束Activity&从堆栈中移除
		
	}

	public boolean isAllowFullScreen() {
		return allowFullScreen;
	}

	/**
	 * 设置是否可以全屏
	 * 
	 * @param allowFullScreen
	 */
	public void setAllowFullScreen(boolean allowFullScreen) {
		this.allowFullScreen = allowFullScreen;
	}

	public void setAllowDestroy(boolean allowDestroy) {
		this.allowDestroy = allowDestroy;
	}

	public void setAllowDestroy(boolean allowDestroy, View view) {
		this.allowDestroy = allowDestroy;
		this.view = view;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imeManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
        imeManager.hideSoftInputFromWindow(getWindow().getDecorView()  
                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);  
		return false;};



	
}
