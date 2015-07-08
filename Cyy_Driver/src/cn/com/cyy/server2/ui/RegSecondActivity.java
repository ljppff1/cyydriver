package cn.com.cyy.server2.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.cyy.server2.R;
import cn.com.cyy.server2.finals.HandlerFinals;
import cn.com.cyy.server2.finals.SharedprefenceFinals;
import cn.com.cyy.server2.fragment.Register3Fragment;
import cn.com.cyy.server2.model.FirmNameModel;
import cn.com.cyy.server2.model.ServerModel;
import cn.com.cyy.server2.net.ParserJson;
import cn.com.cyy.server2.net.SetMapToServer;
import cn.com.cyy.server2.util.AppManager;
import cn.com.cyy.server2.util.DialogListSelectUtil;

public class RegSecondActivity extends BaseActivity{
	private ImageView mIvRegisterHead;
	private EditText mEtRegisterActualName;
	private RadioGroup mRgRegisterSex;
	private RadioButton mRbRegisterBoy;
	private RadioButton mRbRegisterGirl;
	private TextView mTvRegisterSelectFirm;
	private TextView mTvRegisterSelectServer;
	private Button mBtRegisterNext;
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int PHOTO_REQUEST_CUT = 2;// 结果

	private String takeSerPar;
	private String takeSerParId;
	private ProgressDialog loadingDialog = null;

	// 性别
	private String sex = "";

	// 返回头像的字符串
	private String base64Bitmap = "";

	// 选择公司或者是服务
	private int select = 0; // 0 表示公司, 1表示服务
	private String firm = "";
	
	// 记录选中的服务
	private String takeSer = "";
	private String takeSerId = "";
	private String server = "";		// 存入Shared中的服务id

	private Handler noParaHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			loadingDialog.cancel();
			switch (msg.what) {
			case HandlerFinals.JSON_INFO:
				String result = (String) msg.obj;
				if (select == 0) { // 公司
					final List<FirmNameModel> list = ParserJson.firmJson(result);
					String[] items = new String[list.size()];
					for (int i = 0; i < list.size(); i++) {
						items[i] = list.get(i).getName();
					}
					AlertDialog.Builder builder = new Builder(RegSecondActivity.this);
					builder.setTitle("请选择公司").setItems(items,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									mTvRegisterSelectFirm.setText(list.get(which)
											.getName());
									firm = list.get(which).getId();
									dialog.dismiss();
									isInputOk();
								}
							});
					builder.create().show();
				} else if (select == 1) { // 服务
					List<Object> listObj = ParserJson.selectServerJson(result);
					final List<ServerModel> listParent = (List<ServerModel>) listObj
							.get(0);
					final Map<String, List<ServerModel>> map = (Map<String, List<ServerModel>>) listObj
							.get(1);
					// 服务类型
					String[] serverModel = new String[listParent.size()];
					// 子服务
					for (int i = 0; i < listParent.size(); i++) {
						serverModel[i] = listParent.get(i).getServierName();
					}

					final AlertDialog.Builder builder = new Builder(RegSecondActivity.this);
					builder.setTitle("请选择服务类型");
					builder.setItems(serverModel,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									final String name = listParent.get(which).getServierName();
									String[] serverChild  = new String[map.get(name).size()];
									for (int j = 0; j < map.get(name).size(); j++) {
										serverChild[j] = map.get(name).get(j).getServierName();
									}
									
									if(map.get(name).size()==0){
										builder.show();
										Toast.makeText(RegSecondActivity.this, "你查看的服务没有子服务类,请重新选择", Toast.LENGTH_SHORT).show();
									}else if(map.get(name).size()>0){
										System.out.println("来的呀");
										final AlertDialog.Builder builder1 = new Builder(RegSecondActivity.this);
										builder1.setTitle(name+"--子服务");
										builder1.setItems(serverChild, new DialogInterface.OnClickListener() {
											private boolean flag=false;

											@Override
											public void onClick(DialogInterface dialog, int which) {
												String d[]=takeSerId.split(",");
												for(int i=0; i<d.length; i++) {
													if(map.get(name).get(which).getId().equals(d[i])){
														flag =true;
														break;
													}else{
														flag =false;
													}
												}

												
												if(flag){
													mTvRegisterSelectServer.setText(takeSer);
													dialog.dismiss();
													isInputOk();
												}else{
													takeSer += map.get(name).get(which).getServierName() + ",";
													takeSerId += map.get(name).get(which).getId() + ",";
													Log.i("RegSecondActivity","takeSerId+"+takeSerId);
													if(",".equals(takeSer.substring(takeSer.length()-1, takeSer.length()))){
														takeSer = takeSer.substring(0,takeSer.length()-1);
														Log.i("RegSecondActivity","takeSer"+takeSer);
													}
													mTvRegisterSelectServer.setText(takeSer);
													takeSer = takeSer+ ",";
													Log.i("RegSecondActivity","takeSer++"+takeSer);
													dialog.dismiss();
													isInputOk();
												}

												
												
												
												
											}
										});
										builder1.setNegativeButton("返回到上一级", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												builder.show();
												dialog.dismiss();
											}
										});
										builder1.create().show();
										dialog.dismiss();
									}
								}
							});
					// 取消
					builder.setNegativeButton("取消先择的服务", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					builder.create().show();
				}else if (select == 2) { // 服务
					final List<ServerModel> listParent = ParserJson.selectServerIdJson(result);
					String[] serverModel=new String[listParent.size()];
					// 服务类型
					for (int i = 0; i < listParent.size(); i++) {
						serverModel[i] = listParent.get(i).getServierName();
					}

					final AlertDialog.Builder builder = new Builder(RegSecondActivity.this);
					builder.setTitle("请选择服务类型");
					builder.setItems(serverModel,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									final String name = listParent.get(which).getServierName();
												takeSerPar =name;
												takeSerParId = listParent.get(which).getId();
												mTvRegisterSelectServerId.setText(takeSerPar);
												dialog.dismiss();
												isInputOk();
												Log.i("RegSecondActivity","takeSerParId"+takeSerParId);
												Log.i("RegSecondActivity","takeSerPar"+takeSerPar);
												
											}
							});
					// 取消
					builder.setNegativeButton("取消先择的服务", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					builder.create().show();
				}

				
				break;
			case HandlerFinals.JSON_IO_ERROR:
				Toast.makeText(RegSecondActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
				break;
			case HandlerFinals.JSON_IO_CONNECT:
				Toast.makeText(getApplicationContext(), "请检查网络", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
	
		}

	};
	private String saveDir;
	private File tempFile;
	private String head_base64;
	private TextView mTvRegisterSelectServerId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_register2);
		
		
		 saveDir = Environment.getExternalStorageDirectory()
					+ "/cyydriver/";
			File dir = new File(saveDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			 tempFile = new File(saveDir, getPhotoFileName());

        initView();
        
        
	}
	// 使用系统当前日期加以调整作为照片的名称
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";

	}

	
	private void initView() {
		// 实例化控件
		mIvRegisterHead = (ImageView) this.findViewById(R.id.mIvRegisterHead);
		mEtRegisterActualName = (EditText) this
				.findViewById(R.id.mEtRegisterActualName);
		mRgRegisterSex = (RadioGroup) this.findViewById(R.id.mRgRegisterSex);
		mRbRegisterBoy = (RadioButton) this.findViewById(R.id.mRbRegisterBoy);
		mRbRegisterGirl = (RadioButton) this.findViewById(R.id.mRbRegisterGirl);
		mTvRegisterSelectFirm = (TextView) this
				.findViewById(R.id.mTvRegisterSelectFirm);
		mTvRegisterSelectServer = (TextView) this
				.findViewById(R.id.mTvRegisterSelectServer);
		mTvRegisterSelectServerId=(TextView)this.findViewById(R.id.mTvRegisterSelectServerId);
		mBtRegisterNext = (Button) this.findViewById(R.id.mBtRegisterNext);

/*
 		// 初始化Sharedprefences中的注册的第二步
//		head_base64 =SharedprefenceFinals.register_shared.getString("head_base64", "");
//		if(""!=head_base64||!head_base64.equals(null)){
//			mIvRegisterHead.setImageBitmap(DialogListSelectUtil.base64ToBitmap(head_base64));
//		}
//		server = SharedprefenceFinals.register_shared.getString("server", "");
//		// 初始化
//		mEtRegisterActualName.setText(SharedprefenceFinals.register_shared.getString("real_name", ""));
//		sex = SharedprefenceFinals.register_shared.getString("sex", "");
//		mTvRegisterSelectFirm.setText(SharedprefenceFinals.register_shared.getString("firm", ""));
//		mTvRegisterSelectServer.setText(SharedprefenceFinals.register_shared.getString("takeSer", ""));
//        mTvRegisterSelectServerId.setText(SharedprefenceFinals.register_shared.getString("takeSerPar", ""));
//		// 实例化注册第三步的Fragment
//		register3Fragment = Register3Fragment.newInstance("register3");

		isInputOk();
		*/
		if(sex.equals("1")){
			mRbRegisterBoy.setChecked(true);
			mRbRegisterGirl.setChecked(false);
		}else if("2".equals(sex)){
			mRbRegisterBoy.setChecked(false);
			mRbRegisterGirl.setChecked(true);
		}

		// 初始化设置下一步按钮不能被点击
		mBtRegisterNext.setClickable(false);
		// 设置监听
		mIvRegisterHead.setOnClickListener(listener);
		mTvRegisterSelectFirm.setOnClickListener(listener);
		mTvRegisterSelectServer.setOnClickListener(listener);
		mTvRegisterSelectServerId.setOnClickListener(listener);
		mBtRegisterNext.setOnClickListener(listener);
		// 性别选择的监听
		mRgRegisterSex
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == mRbRegisterBoy.getId()) {
							sex = "1";
							isInputOk();
						} else if (checkedId == mRbRegisterGirl.getId()) {
							sex = "2";
							isInputOk();
						}
					}
				});
		// 姓名输入监听
		mEtRegisterActualName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				isInputOk();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		isInputOk();
	}
     
	OnClickListener listener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mIvRegisterHead: // 上传头像
				
				/*base64Bitmap = DialogListSelectUtil.UpHeadImag(getActivity(),
						mIvRegisterHead);*/

				uploadImage();
				
				break;
			case R.id.mTvRegisterSelectFirm: // 选择公司
				select = 0;
				SetMapToServer.noParameter(RegSecondActivity.this, noParaHandler,
						"geCompany");
				loadingDialog = new ProgressDialog(RegSecondActivity.this);
				loadingDialog.setMessage("正在加载...");
				loadingDialog.show();

				break;
			case R.id.mTvRegisterSelectServer: // 选择服务项目

				select = 1;
				SetMapToServer.noParameter(RegSecondActivity.this, noParaHandler,
						"getServiceItem");
				loadingDialog = new ProgressDialog(RegSecondActivity.this);
				loadingDialog.setMessage("正在加载...");
				loadingDialog.show();

				break;
			case R.id.mTvRegisterSelectServerId: // 选择服务项目

				select = 2;
				SetMapToServer.noParameter(RegSecondActivity.this, noParaHandler,
						"getServiceItem");
				loadingDialog = new ProgressDialog(RegSecondActivity.this);
				loadingDialog.setMessage("正在加载...");
				loadingDialog.show();

				break;

			case R.id.mBtRegisterNext: // 下一步
				String real_name = mEtRegisterActualName.getText().toString()
						.trim();
				// String firm = mTvRegisterSelectFirm.getText().toString().trim();
/*				if(server.equals("")&&!takeSerId.equals("")){
					if(",".equals(takeSerId.substring(takeSerId.length()-1, takeSerId.length()))){
						server = takeSerId.substring(0 , takeSerId.length()-1);
						Log.i("RegSecondActivity","firstServer"+server);

					}else{
						server = takeSerId.substring(0 , takeSerId.length());
						Log.i("RegSecondActivity","secondServer"+server);
					}
				}
*/				 				if(!takeSerId.equals("")){
             	if(",".equals(takeSerId.substring(takeSerId.length()-1, takeSerId.length()))){
	             	server = takeSerId.substring(0 , takeSerId.length()-1);
	           	Log.i("RegSecondActivity","firstServer"+server);

	          }else{
	             	server = takeSerId.substring(0 , takeSerId.length());
	            	Log.i("RegSecondActivity","secondServer"+server);
	                  }
                  }

				// mTvRegisterSelectServer.getText().toString().trim();

				Editor editor = SharedprefenceFinals.register_shared.edit();
				editor.putString("head_base64", base64Bitmap);
				editor.putString("real_name", real_name);
				editor.putString("sex", sex);
				editor.putString("firm", firm);
				editor.putString("takeSer", takeSer);
				editor.putString("server", server);
				editor.putString("takeSerPar", takeSerPar);
				editor.putString("takeSerParId", takeSerParId);
				Log.i("RegSecondActivity", sex+"firm ="+firm+"server ="+server+"takeserPar"+takeSerPar+"takeSerParId"+takeSerParId+""+"v  "+takeSerId);
				editor.commit();
                 
				startActivity(new Intent(RegSecondActivity.this, RegThirdActivity.class));
				AppManager.getAppManager().finishActivity();
				break;
			}
		}
	
};
	private long exitTime=0;

private void uploadImage() {
	AlertDialog.Builder builder = new AlertDialog.Builder(RegSecondActivity.this);
	builder.setTitle("图片来源");
	builder.setNegativeButton("取消", null);
	builder.setItems(new String[]{"拍照","相册"}, new DialogInterface.OnClickListener() {
		//类型码
		int REQUEST_CODE;
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case TAKE_PICTURE:
				// 调用系统的拍照功能
				Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 指定调用相机拍照后照片的储存路径
				intent1.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(tempFile));
				startActivityForResult(intent1, 1);

				break;
				
			case CHOOSE_PICTURE:
				Intent intent2 = new Intent(Intent.ACTION_PICK, null);
				intent2.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent2, 2);
				break;

			default:
				break;
			}
		}
	});
	builder.create().show();
}

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	
		switch (requestCode) {
	 
		case 1:
			startPhotoZoom(Uri.fromFile(tempFile), 150);

			break;

		case 2:
			if (data != null) {

				startPhotoZoom(data.getData(), 150);
				// 传一个内部地址

			}
			break;
		case 3:
			if (data != null)
				setPicToView(data);
			break;

		default:
			break;
		}
	
}

// 将进行剪裁后的图片显示到UI界面上
private void setPicToView(Intent picdata) {
	Bundle bundle = picdata.getExtras();

	if (bundle != null) {
		Bitmap photo = bundle.getParcelable("data");
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);//
		base64Bitmap=DialogListSelectUtil.bitmapToBase64(photo);
		mIvRegisterHead.setImageBitmap(photo);
		
		
	}
	
}

private void startPhotoZoom(Uri uri, int size) {
	Intent intent = new Intent("com.android.camera.action.CROP");
	intent.setDataAndType(uri, "image/*");
	// crop为true是设置在开启的intent中设置显示的view可以剪裁
	intent.putExtra("crop", "true");

	// aspectX aspectY 是宽高的比例
	intent.putExtra("aspectX", 1);
	intent.putExtra("aspectY", 1);

	// outputX,outputY 是剪裁图片的宽高
	intent.putExtra("outputX", size);
	intent.putExtra("outputY", size);
	intent.putExtra("return-data", true);

	startActivityForResult(intent, 3);
}

// 判断用户输入是否完成或符合条件
/*
 * && !mTvRegisterSelectFirm.getText().toString().trim() .equals("") &&
 * !mTvRegisterSelectServer.getText().toString().trim() .equals("")
 */
private void isInputOk() {
	if (!mEtRegisterActualName.getText().toString().trim().equals("")
			&& mEtRegisterActualName.getText().toString().length() >= 2
			&& (mRbRegisterBoy.isChecked() || mRbRegisterGirl.isChecked())
			&& !mTvRegisterSelectFirm.getText().toString().trim()
					.equals("")
			&& !mTvRegisterSelectServer.getText().toString().trim()
					.equals("")
			&& !mTvRegisterSelectServerId.getText().toString().trim().equals("")) {

		mBtRegisterNext.setClickable(true);
		mBtRegisterNext
				.setBackgroundResource(R.drawable.select_dorangebg_to_sorangebg);

	} else {
		mBtRegisterNext.setClickable(false);
		mBtRegisterNext.setBackgroundResource(android.R.color.darker_gray);
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