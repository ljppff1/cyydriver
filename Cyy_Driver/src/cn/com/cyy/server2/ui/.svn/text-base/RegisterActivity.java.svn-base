package cn.com.cyy.server2.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cyy.server2.R;
import cn.com.cyy.server2.fragment.Register1Fragment;
import cn.com.cyy.server2.fragment.Register2Fragment;
import cn.com.cyy.server2.fragment.Register3Fragment;

/**
 * 注册的Activity
 * 
 * @author hurenji
 * 
 */
public class RegisterActivity extends FragmentActivity implements
		OnClickListener {

	// 定义全局布局文件的控件
	private ImageView mIvActionBarLeft;
	private TextView mTvActionBarCenter;
	private ImageView mIvActionBarRight;

	private FrameLayout mFlRegisterLayout;
	// 记录Fragment的数量
	private int fragment_num = 0;

	// 定义Fragment管理,定义Fragment
	private FragmentManager manager;
	private Register1Fragment register1Fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regiest);

		// 实例化控件
		mIvActionBarLeft = (ImageView) this.findViewById(R.id.mIvActionBarLeft);
		mTvActionBarCenter = (TextView) this
				.findViewById(R.id.mTvActionBarCenter);
		mIvActionBarRight = (ImageView) this
				.findViewById(R.id.mIvActionBarRight);

		mFlRegisterLayout = (FrameLayout) this
				.findViewById(R.id.mFlRegisterLayout);

		// 实例化Fragment管理
		manager = getSupportFragmentManager();
		register1Fragment = Register1Fragment.newInstance("register1");

		// 初始化设置
		mIvActionBarRight.setVisibility(View.INVISIBLE);
		mIvActionBarLeft.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.back));
		CharSequence charSequence = getResources().getString(R.string.register);
		mTvActionBarCenter.setText(charSequence);

		// 设置监听
		mIvActionBarLeft.setOnClickListener(this);

	}

	@Override
	protected void onStart() {
		super.onStart();

		replaceFragment(register1Fragment, "register1");

	}

	// 所有点击监听的公共方法
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mIvActionBarLeft:
			backStackFragment();
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			backStackFragment();
			return true;
		}
		return false;
	}

	/**
	 * 添加或切换Fragment的方法
	 * 
	 * @param fragment
	 *            fragment
	 * @param tag
	 *            标记
	 */
	public void replaceFragment(Fragment fragment, String tag) {
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.addToBackStack(tag);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
		transaction.replace(R.id.mFlRegisterLayout, fragment, tag);
		transaction.commit();
		fragment_num += 1;
	}

	private void backStackFragment() {
		switch (fragment_num) {
		case 3:
			manager.popBackStack("register2" , 0);
			fragment_num -= 1;
			break;
		case 2:
			manager.popBackStack("register1" , 0);
			fragment_num -= 1;
			break;
		case 1:
			RegisterActivity.this.finish();
			break;
		}
	}

}
