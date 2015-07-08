package cn.com.cyy.server2.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;
import cn.com.cyy.server2.R;

/**
 * 对话框列表选择
 * 
 * @author hurenji
 * 
 */
@SuppressLint("NewApi")
public class DialogListSelectUtil {

	/**
	 * 上传图片的方式选择
	 * 
	 * @param context
	 * @param imageView
	 *            头像的控件
	 * @return 图片的Base64
	 */
	public static String UpHeadImag(final Context context,
			final ImageView imageView) {
		// 定义Dialog
		String[] items = { "拍照上传头像", "手机相册选择" };
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("请选择上传方式");
		builder.setItems(items, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dealDilaog(which, context, imageView);
			}
		}).setNegativeButton("取消上传", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).show();

		return "";
	}

	// 处理图片选择
	private static String dealDilaog(int what, Context context,
			ImageView imageView) {
		String base64Bitmap = "";
		switch (what) {
		case 0: // 拍照上传
			// 调用手机相机
			// 判断手机的储值卡是否可用
			String state = Environment.getExternalStorageState();
			if (state.equals(Environment.MEDIA_MOUNTED)) {
				String saveDir = Environment.getExternalStorageDirectory()
						+ "/cyy_driver/head_photo";
				File dir = new File(saveDir);
				if (!dir.exists()) {
					dir.mkdir();
				}
				File file = new File(saveDir, "head.jpg");
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
						Toast.makeText(context, "图片拍摄失败,请重试", Toast.LENGTH_LONG)
								.show();
						return "";
					}
				}
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				context.startActivity(intent);

				// 剪切图片
				Bitmap bit = cropImage(intent, context);
				if (bit != null) {
					imageView.setImageBitmap(bit);
					base64Bitmap = bitmapToBase64(bit);
				} else {
					Toast.makeText(context, "图片拍摄失败，请重新拍摄", Toast.LENGTH_LONG)
							.show();
				}

			} else {
				Toast.makeText(context, "手机外置储存控件不可用", Toast.LENGTH_LONG)
						.show();
			}

			break;
		case 1: // 从手机相册中选择
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			context.startActivity(intent);
			// 剪切图片
			Bitmap bit = cropImage(intent, context);
			if (bit != null) {
				imageView.setImageBitmap(bit);
				base64Bitmap = bitmapToBase64(bit);
			} else {
				Toast.makeText(context, "图片拍摄失败，请重新拍摄", Toast.LENGTH_LONG)
						.show();
			}

			break;
		}

		return base64Bitmap;
	}

	// 图片裁剪
	private static Bitmap cropImage(Intent intent, Context context) {
		Bitmap bitmap = null;
		Uri uri = intent.getData();
		Intent intent1 = new Intent("com.android.camera.action.CROP");
		intent1.setDataAndType(uri, "image/*");
		intent1.putExtra("crop", "true"); // 可剪切
		intent1.putExtra("aspectX", 1);
		intent1.putExtra("aspectY", 1);
		intent1.putExtra("outputX", 100);
		intent1.putExtra("outputY", 100);
		intent1.putExtra("scale", true);
		intent1.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent1.putExtra("return-data", true);// 若为false则表示不返回数据
		intent1.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent1.putExtra("noFaceDetection", true);
		context.startActivity(intent1);

		/*
		 * bitmap = BitmapFactory.decodeStream(context.getContentResolver()
		 * .openInputStream(uri));
		 */
		bitmap = intent1.getParcelableExtra("data");

		return bitmap;
	}

	// Bitmap转换成base64
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = "";
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				baos.flush();
				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
			result = "";
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// base64转bitmap
	public static Bitmap base64ToBitmap(String base64Data) {  
	    byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);  
	    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);  
	}  
	
}
