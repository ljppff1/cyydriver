package cn.com.cyy.server2.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import cn.com.cyy.server2.R;
import cn.com.cyy.server2.finals.HandlerFinals;
import cn.com.cyy.server2.finals.SharedprefenceFinals;
import cn.com.cyy.server2.net.SetMapToServer;
import cn.com.cyy.server2.util.DialogListSelectUtil;

/**
 * 注册第三步
 * 
 * @author hurenji
 * 
 */
public class Register3Fragment extends Fragment implements OnClickListener {

	private ImageView mIvRegisterIdCardImage;
	private Button mBtRegisterUpImage;
	private Button mBtRegisterSubmit;

	// 身份证图片的base64
	private String base64IdCard = "";

	// 用户注册成功的Handler
	private Handler registerOkHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HandlerFinals.JSON_INFO:
				Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT)
						.show();
				SharedprefenceFinals.register_shared.edit().clear().commit();
				SharedprefenceFinals.register_shared = null;
				getActivity().finish();
				break;
			case HandlerFinals.JSON_IO_ERROR:
				Toast.makeText(getActivity(), "注册失败,请稍后重试", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	// 使用静态方法实例化Fragment
	public static Register3Fragment newInstance(String label) {
		Register3Fragment fragment = new Register3Fragment();
		Bundle args = new Bundle();
		args.putString("label", label);
		fragment.setArguments(args);
		return fragment;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_register3, null);
		mIvRegisterIdCardImage = (ImageView) view
				.findViewById(R.id.mIvRegisterIdCardImage);
		mBtRegisterUpImage = (Button) view
				.findViewById(R.id.mBtRegisterUpImage);
		mBtRegisterSubmit = (Button) view.findViewById(R.id.mBtRegisterSubmit);

		String base64s = SharedprefenceFinals.register_shared.getString(
				"base64_idCard", "");
		if (!base64s.equals("")) {
			Bitmap bit_idCard = DialogListSelectUtil.base64ToBitmap(base64s);
			mIvRegisterIdCardImage.setImageBitmap(bit_idCard);
		}

		// 设置控件点击监听
		mBtRegisterUpImage.setOnClickListener(this);
		mBtRegisterSubmit.setOnClickListener(this);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	// 控件点击监听的公共类
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mBtRegisterUpImage: // 上传身份证正面图片
			base64Bitmap();
			break;
		case R.id.mBtRegisterSubmit: // 提交审核;
			Drawable d = getResources().getDrawable(R.drawable.ic_launcher);
			BitmapDrawable bd = (BitmapDrawable) d;
			Bitmap b = bd.getBitmap();
			String sixfor = DialogListSelectUtil.bitmapToBase64(b);
			SharedprefenceFinals.register_shared.edit()
					.putString("base64_idCard", sixfor).commit();
			SetMapToServer.sumbitRegister(getActivity(), registerOkHandler);
			/*
			 * if(!base64IdCard.equals("")){
			 * SetMapToServer.sumbitRegister(getActivity(), registerOkHandler);
			 * }else{ Toast.makeText(getActivity(), "为了您更好的使用,请上传身份证正面照片",
			 * Toast.LENGTH_LONG).show(); }
			 */
			break;
		}
	}

	// 调用手机相机拍摄
	private void base64Bitmap() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			String saveDir = Environment.getExternalStorageDirectory()
					+ "/cyy_driver/head_photo";
			File dir = new File(saveDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			File file = new File(saveDir, "id_card.jpg");
			/*
			 * if (!file.exists()) { try { file.createNewFile(); } catch
			 * (IOException e) { e.printStackTrace();
			 * Toast.makeText(getActivity(), "图片拍摄失败,请重试",
			 * Toast.LENGTH_LONG).show(); } }
			 */
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			getActivity().startActivityForResult(intent, 0);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			String saveDir = Environment.getExternalStorageDirectory()
					+ "/cyy_driver/head_photo";
			File file = new File(saveDir, "id_card.jpg");
			if (file.exists()) {
				FileInputStream in = null;
				// StringBuffer buffer = new StringBuffer();
				try {
					in = new FileInputStream(file);
					Bitmap bitmap = BitmapFactory.decodeStream(in);
					if (bitmap != null) {
						mIvRegisterIdCardImage.setImageBitmap(bitmap);
						base64IdCard = DialogListSelectUtil
								.bitmapToBase64(bitmap);
						Editor editor = SharedprefenceFinals.register_shared
								.edit();
						editor.putString("base64_idCard", base64IdCard);
						editor.commit();
					} else {
						Toast.makeText(getActivity(), "图片处理失败,请重试",
								Toast.LENGTH_LONG).show();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getActivity(), "图片拍摄失败,请重试", Toast.LENGTH_LONG)
						.show();
			}

		}
	}

}
