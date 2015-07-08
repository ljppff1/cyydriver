package cn.com.cyy.server2.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import cn.com.cyy.server2.R;
import cn.com.cyy.server2.util.ImageTools;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CameraActivity extends BaseActivity {
	private ImageView mIVtakephoto1;
	private ImageView mIVtakephoto2;
	private ImageView mIVtakephoto4;
	private ImageView mIVtakephoto3;
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	File tempFile = new File(Environment.getExternalStorageDirectory()
			+ "/CyyDriver/", getPhotoFileName());
    File tempFile1 ;
	private LinearLayout mLLphoto1;
	private LinearLayout mLLphoto2;
	private View mLLphoto3;
	private View mLLphoto4;
	private static final int SCALE = 5;//照片缩小比例
    private int  flag=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_takephoto);
		createFile();
		
		initView();
	}

	private void createFile() {
	    File sdcardDir =Environment.getExternalStorageDirectory();
          String path=sdcardDir.getPath()+"/CyyDriver";
          File path1 = new File(path);
         if (!path1.exists()) {
          //若不存在，创建目录，可以在应用启动的时候创建
          path1.mkdirs();
        }
		
	}

	private void initView() {
	    mLLphoto1 =(LinearLayout)this.findViewById(R.id.mLLphoto1);
	    mLLphoto1.setOnClickListener(listener);
	    mLLphoto2 =(LinearLayout)this.findViewById(R.id.mLLphoto2);
	    mLLphoto2.setOnClickListener(listener);
	    mLLphoto3 =(LinearLayout)this.findViewById(R.id.mLLphoto3);
	    mLLphoto3.setOnClickListener(listener);
	    mLLphoto4 =(LinearLayout)this.findViewById(R.id.mLLphoto4);
	    mLLphoto4.setOnClickListener(listener);
	    
		mIVtakephoto1 =(ImageView)this.findViewById(R.id.mIVtakephoto1);
		mIVtakephoto1.setOnClickListener(listener);
		mIVtakephoto2 =(ImageView)this.findViewById(R.id.mIVtakephoto2);
		mIVtakephoto2.setOnClickListener(listener);
		mIVtakephoto3 =(ImageView)this.findViewById(R.id.mIVtakephoto3);
		mIVtakephoto3.setOnClickListener(listener);
		mIVtakephoto4 =(ImageView)this.findViewById(R.id.mIVtakephoto4);
		mIVtakephoto4.setOnClickListener(listener);
		
	}
	OnClickListener listener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
/*			case R.id.mIVtakephoto1:
				flag=1;
				showPicturePicker(getApplicationContext(),1);
				break;
			case R.id.mIVtakephoto2:
				flag=2;
				showPicturePicker(getApplicationContext(),2);
				break;
			case R.id.mIVtakephoto3:
				flag=3;
				showPicturePicker(getApplicationContext(),3);
				break;
			case R.id.mIVtakephoto4:
				flag=4;
				showPicturePicker(getApplicationContext(),4);
				break;
*/			case R.id.mLLphoto1:
				flag=1;
				showPicturePicker(CameraActivity.this,1);
				break;
			case R.id.mLLphoto2:
				flag=2;
				showPicturePicker(CameraActivity.this,2);
				break;
			case R.id.mLLphoto3:
				flag=3;
				showPicturePicker(CameraActivity.this,3);
				break;
			case R.id.mLLphoto4:
				flag=4;
				showPicturePicker(CameraActivity.this,4);
				break;

			default:
				break;
			}
			
		}

	};
	
	public void showPicturePicker(Context context,int i){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("图片来源");
		builder.setNegativeButton("取消", null);
		builder.setItems(new String[]{"拍照","相册"}, new DialogInterface.OnClickListener() {
			//类型码
			int REQUEST_CODE;
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case TAKE_PICTURE:
					REQUEST_CODE = TAKE_PICTURE;
					// 调用系统的拍照功能
					Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					// 指定调用相机拍照后照片的储存路径
					intent1.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(tempFile));
					tempFile1=tempFile;
					startActivityForResult(intent1, TAKE_PICTURE);
					break;
				case CHOOSE_PICTURE:
					REQUEST_CODE = CHOOSE_PICTURE;
					Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
						REQUEST_CODE = CHOOSE_PICTURE;
					openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
					startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
					break;

				default:
					break;
				}
			}
		});
		builder.create().show();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case TAKE_PICTURE:
				//将保存在本地的图片取出并缩小后显示在界面上
				Bitmap bitmap = BitmapFactory.decodeFile(tempFile1.getPath());
				Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
				//由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
				bitmap.recycle();
				
				//将处理过的图片显示在界面上，并保存到本地
				switch (flag) {
				case 1:
					mIVtakephoto1.setImageBitmap(newBitmap);
					break;
				case 2:
					mIVtakephoto2.setImageBitmap(newBitmap);
					break;
				case 3:
					mIVtakephoto3.setImageBitmap(newBitmap);
					break;
				case 4:
					mIVtakephoto4.setImageBitmap(newBitmap);
					break;
				default:
					break;
				}
				
				break;

			case CHOOSE_PICTURE:
				ContentResolver resolver = getContentResolver();
				//照片的原始资源地址
				Uri originalUri = data.getData(); 
	            try {
	            	//使用ContentProvider通过URI获取原始图片
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
					if (photo != null) {
						//为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
						Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
						//释放原始图片占用的内存，防止out of memory异常发生
						photo.recycle();
						switch (flag) {
						case 1:
							mIVtakephoto1.setImageBitmap(smallBitmap);
							break;
						case 2:
							mIVtakephoto2.setImageBitmap(smallBitmap);
							break;
						case 3:
							mIVtakephoto3.setImageBitmap(smallBitmap);
							break;
						case 4:
							mIVtakephoto4.setImageBitmap(smallBitmap);
							break;
						default:
							break;
						}
						
					}
				} catch (FileNotFoundException e) {
				    e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
				
			default:
				break;
			}
		}
	}
	
	// 使用系统当前日期加以调整作为照片的名称
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";

	}

}
