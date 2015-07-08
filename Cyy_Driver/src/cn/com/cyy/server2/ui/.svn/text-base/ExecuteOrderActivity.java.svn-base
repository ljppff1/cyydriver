package cn.com.cyy.server2.ui;

import cn.com.cyy.server2.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExecuteOrderActivity extends BaseActivity {
	private TextView mTvActionBarCenter;
	private ImageView mIvActionBarRight;
	private TextView mTVBackReturn;
	private Button mBtMainServer;
	private Button mBtMainStatus;
	private TextView mTVPhoneNumber;
	private RelativeLayout mRLcamera;
	private RelativeLayout mRLrecord;
	private RelativeLayout mRLsignature;
	private ProgressBar mPBexecute;
	private TextView mIVexecutecamera;
	private TextView mIVexecuterecord;
	private TextView mIVexecutesignature;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_execute_order);
		initView();
		
		
		
	}

	private void initView() {
		//actionbar
		mTvActionBarCenter =(TextView)this.findViewById(R.id.mTvActionBarCenter);
		mTvActionBarCenter.setText("执行救援");
		mIvActionBarRight =(ImageView)this.findViewById(R.id.mIvActionBarRight);
		mIvActionBarRight.setOnClickListener(listener);
		mTVBackReturn =(TextView)this.findViewById(R.id.mTVBackReturn);
		mTVBackReturn.setOnClickListener(listener);
		//buttom
		mBtMainServer=(Button)this.findViewById(R.id.mBtMainServer);
		mBtMainServer.setOnClickListener(listener);
		mBtMainServer.setText("完成");
		mBtMainStatus=(Button)this.findViewById(R.id.mBtMainStatus);
		mBtMainStatus.setOnClickListener(listener);
		mBtMainStatus.setText("中止");
		//联系车主
		mTVPhoneNumber=(TextView)this.findViewById(R.id.mTVPhoneNumber);
		mTVPhoneNumber.setOnClickListener(listener);
		
		//三个Button拍照、记录、签名
		mRLcamera =(RelativeLayout)this.findViewById(R.id.mRLcamera);
		mRLcamera.setOnClickListener(listener);
		mRLrecord =(RelativeLayout)this.findViewById(R.id.mRLrecord);
		mRLrecord.setOnClickListener(listener);
		mRLsignature =(RelativeLayout)this.findViewById(R.id.mRLsignature);
		mRLsignature.setOnClickListener(listener);
		mPBexecute =(ProgressBar)this.findViewById(R.id.mPBexecute);
		mIVexecutecamera=(TextView)this.findViewById(R.id.mIVexecutecamera);
		mIVexecuterecord=(TextView)this.findViewById(R.id.mIVexecuterecord);
		mIVexecutesignature=(TextView)this.findViewById(R.id.mIVexecutesignature);
		
		
		
	}
	
	OnClickListener listener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mIvActionBarRight: // 进入设置界面
				startActivity(new Intent(ExecuteOrderActivity.this, SettingActivity.class));
				break;
			case R.id.mTVBackReturn:
				finish();
				break;
			case R.id.mTVPhoneNumber:
				  Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+10086));     
				  startActivity(intent);     
				break;
				//进入拍照界面
			case R.id.mRLcamera:
				startActivity(new Intent(getApplicationContext(), CameraActivity.class));
				break;
				//进入记录界面
			case R.id.mRLrecord:
				startActivity(new Intent(getApplicationContext(), NoteActivity.class));
				break;
			case R.id.mRLsignature:
				startActivity(new Intent(getApplicationContext(), HandWritingActivity.class));
				break;
			default:
				break;
			}

		}
	};

}
