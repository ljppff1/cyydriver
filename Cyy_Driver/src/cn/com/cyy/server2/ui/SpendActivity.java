package cn.com.cyy.server2.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.cyy.server2.R;

/**
 * 启动动画的Activity
 * @author hurenji
 */
public class SpendActivity extends Activity {

	private RelativeLayout mRlSpendLayout;
	private ImageView mIvSpendIcon;
	private TextView mTvSpendText;
	
	// 定义动画效果
	private TranslateAnimation translate;	// 平移动画
	private AlphaAnimation aa;				// 渐变动画
	
	private boolean isLogin = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spend);
		isLogin = getSharedPreferences("isBoolean", MODE_PRIVATE).getBoolean("is_login", false);
		if(isLogin){
			// 进入主界面
			SpendActivity.this.startActivity(new Intent(SpendActivity.this , MainActivity.class));
			finish();
		}
		
	

		mRlSpendLayout = (RelativeLayout) this.findViewById(R.id.mRlSpendLayout);
		mIvSpendIcon = (ImageView) this.findViewById(R.id.mIvSpendIcon);
		mTvSpendText = (TextView) this.findViewById(R.id.mTvSpendText);
		
		translate = new TranslateAnimation(0, 0, 0, -30);
		translate.setDuration(1500);
		translate.setFillAfter(true);
		aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(3000);
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mRlSpendLayout.setAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mTvSpendText.setVisibility(View.VISIBLE);
				mIvSpendIcon.setAnimation(translate);
				translate.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						try {
							Thread.sleep(1000);
							if(isLogin){
								// 进入主界面
								SpendActivity.this.startActivity(new Intent(SpendActivity.this , MainActivity.class));
							}else{
								// 休眠1500ms，跳转到登录界面
								SpendActivity.this.startActivity(new Intent(SpendActivity.this , LoginActivity.class));
							}
							// 关闭当前Activity
							SpendActivity.this.finish();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		
	}
}
