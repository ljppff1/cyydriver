package cn.com.cyy.server2.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Toast;
import cn.com.cyy.server2.R;
import cn.com.cyy.server2.finals.HandlerFinals;
import cn.com.cyy.server2.finals.SharedprefenceFinals;
import cn.com.cyy.server2.net.ParserJson;
import cn.com.cyy.server2.net.SetMapToServer;
import cn.com.cyy.server2.util.AppManager;
import cn.com.cyy.server2.util.DialogListSelectUtil;

public class RegThirdActivity extends BaseActivity{
	private ImageView mIvRegisterIdCardImage;
	private Button mBtRegisterUpImage;
	private Button mBtRegisterSubmit;
	private ProgressDialog loadingDialog = null;
	private boolean flag;

	// 身份证图片的base64
	private String base64IdCard = "";

	// 用户注册成功的Handler
	private Handler registerOkHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			loadingDialog.cancel();
			switch (msg.what) {
			
			case HandlerFinals.JSON_INFO:
				Toast.makeText(RegThirdActivity.this, "注册成功", Toast.LENGTH_SHORT)
						.show();
				SharedprefenceFinals.register_shared.edit().clear().commit();
				SharedprefenceFinals.register_shared = null;
				
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                AppManager.getAppManager().finishActivity();
				break;
			case HandlerFinals.JSON_IO_ERROR:
				String info =	ParserJson.registerInfoJson((String) msg.obj,RegThirdActivity.this);
				Toast.makeText(RegThirdActivity.this,info, Toast.LENGTH_SHORT)
						.show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                AppManager.getAppManager().finishActivity();
				break;
			case HandlerFinals.JSON_IO_CONNECT:
				Toast.makeText(getApplicationContext(), "请检查网络", Toast.LENGTH_SHORT).show();
				break;

			}
		}
	};
	private CheckBox mCBRegister;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_register3);
        initView();
        
	}

	
	private void initView() {
		mIvRegisterIdCardImage = (ImageView) this
				.findViewById(R.id.mIvRegisterIdCardImage);
		mBtRegisterUpImage = (Button) this
				.findViewById(R.id.mBtRegisterUpImage);
		mBtRegisterSubmit = (Button) this.findViewById(R.id.mBtRegisterSubmit);
		mCBRegister =(CheckBox)this.findViewById(R.id.mCBRegister);
		

/*		String base64s = SharedprefenceFinals.register_shared.getString(
				"base64_idCard", "");
		if (!base64s.equals("")) {
			Bitmap bit_idCard = DialogListSelectUtil.base64ToBitmap(base64s);
			mIvRegisterIdCardImage.setImageBitmap(bit_idCard);
		}
*/
		// 设置控件点击监听
		mBtRegisterUpImage.setOnClickListener(listener);
		mBtRegisterSubmit.setOnClickListener(listener);
		mCBRegister.setOnCheckedChangeListener(listener1);

	}
     
	OnCheckedChangeListener listener1 =new OnCheckedChangeListener() {
		

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked){
				flag =true;
			}else{
				flag=false;
			}
			
		}
	};
	
	OnClickListener listener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mBtRegisterUpImage: // 上传身份证正面图片
				base64Bitmap();
				break;
			case R.id.mBtRegisterSubmit: // 提交审核;
/*				Drawable d = getResources().getDrawable(R.drawable.ic_launcher);
				BitmapDrawable bd = (BitmapDrawable) d;
				Bitmap b = bd.getBitmap();
				String sixfor = DialogListSelectUtil.bitmapToBase64(b);
				SharedprefenceFinals.register_shared.edit()
						.putString("base64_idCard", sixfor).commit();
						
*/
				if(flag){
				loadingDialog = new ProgressDialog(RegThirdActivity.this);
				loadingDialog.setMessage("正在提交...");
				loadingDialog.show();
				SetMapToServer.sumbitRegister(RegThirdActivity.this, registerOkHandler);
				}else{
				Toast.makeText(getApplicationContext(), "请同意协议后提交", 0).show();
				}
				/*
				 * if(!base64IdCard.equals("")){
				 * SetMapToServer.sumbitRegister(RegThirdActivity.this;, registerOkHandler);
				 * }else{ Toast.makeText(RegThirdActivity.this;, "为了您更好的使用,请上传身份证正面照片",
				 * Toast.LENGTH_LONG).show(); }
				 */
				break;

			}
	}

   

	
};
	private long exitTime=0;
	private Bitmap bitmap;
//调用手机相机拍摄
	private void base64Bitmap() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			String saveDir = Environment.getExternalStorageDirectory()
					+ "/cyydriver/";
			File dir = new File(saveDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			File file = new File(saveDir, "id_card.jpg");
			/*
			 * if (!file.exists()) { try { file.createNewFile(); } catch
			 * (IOException e) { e.printStackTrace();
			 * Toast.makeText(RegThirdActivity.this;, "图片拍摄失败,请重试",
			 * Toast.LENGTH_LONG).show(); } }
			 */
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			startActivityForResult(intent, 1);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case 1:
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			String saveDir = Environment.getExternalStorageDirectory()
					+ "/cyydriver/";
			File file = new File(saveDir, "id_card.jpg");
			if (file.exists()) {
				FileInputStream in = null;
				// StringBuffer buffer = new StringBuffer();
				try {
					in = new FileInputStream(file);
					BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmapOptions.inSampleSize = 4;
                     bitmap = BitmapFactory.decodeStream(in, null, bitmapOptions);

                    // bitmap = BitmapFactory.decodeStream(in);
					if (bitmap != null) {
						mIvRegisterIdCardImage.setImageBitmap(bitmap);
						base64IdCard = DialogListSelectUtil
								.bitmapToBase64(bitmap);
						Editor editor = SharedprefenceFinals.register_shared
								.edit();
						editor.putString("base64_idCard", base64IdCard);
						editor.commit();
					} else {
						Toast.makeText(RegThirdActivity.this, "图片处理失败,请重试",
								Toast.LENGTH_LONG).show();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(RegThirdActivity.this, "图片拍摄失败,请重试", Toast.LENGTH_LONG).show();
			}
		}
		break;
		default:
			break;

		}		
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出注册流程",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				AppManager.getAppManager().finishActivity();				
			}
			return true;
			}
		return true;
	}

	
	
}