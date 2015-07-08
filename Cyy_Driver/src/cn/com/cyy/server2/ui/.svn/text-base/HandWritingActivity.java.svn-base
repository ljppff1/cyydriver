package cn.com.cyy.server2.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import cn.com.cyy.server2.R;
import cn.com.cyy.server2.inter.DialogListener;
import cn.com.cyy.server2.util.ImageTools;

public class HandWritingActivity extends BaseActivity{
	private Bitmap mSignBitmap;
	private ImageView mIVHandWriting;
	private String signPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_handwriting);
		initView();
		
	}

	private void initView() {
		mIVHandWriting =(ImageView)this.findViewById(R.id.mIVHandWriting);
		mIVHandWriting.setOnClickListener(listener);
		
	}
	
	OnClickListener listener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mIVHandWriting:
				showDia();
				break;

			default:
				break;
			}
			
		}

	};
	private void showDia() {
		WritePadDialog writeTabletDialog = new WritePadDialog(
				HandWritingActivity.this, new DialogListener() {


					@Override
					public void refreshActivity(Object object) {							
						
						mSignBitmap = (Bitmap) object;
						signPath = createFile();
						Bitmap smallBitmap = ImageTools.zoomBitmap(mSignBitmap, mSignBitmap.getWidth() / 3, mSignBitmap.getHeight() / 3);

						/*BitmapFactory.Options options = new BitmapFactory.Options();
						options.inSampleSize = 15;
						options.inTempStorage = new byte[5 * 1024];
						Bitmap zoombm = BitmapFactory.decodeFile(signPath, options);*/														
						mIVHandWriting.setImageBitmap(smallBitmap);
					}
				});
		writeTabletDialog.show();
	}

	/**
	 * 创建手写签名文件
	 * 
	 * @return
	 */
	private String createFile() {
		ByteArrayOutputStream baos = null;
		String _path = null;
		try {
		    File sdcardDir =Environment.getExternalStorageDirectory();
	          String path=sdcardDir.getPath()+"/CyyDriver";
	          File path1 = new File(path);
	         if (!path1.exists()) {
	          //若不存在，创建目录，可以在应用启动的时候创建
	          path1.mkdirs();
	        }

			_path = sdcardDir+"/" + System.currentTimeMillis() + ".jpg";
			baos = new ByteArrayOutputStream();
			mSignBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] photoBytes = baos.toByteArray();
			if (photoBytes != null) {
				new FileOutputStream(new File(_path)).write(photoBytes);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return _path;
	}
	
	// 使用系统当前日期加以调整作为照片的名称
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";

	}

}
