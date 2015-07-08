package cn.com.cyy.server2.util;

import java.io.IOException;

import cn.com.cyy.server2.R;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;

public class SmallUtil {

	private SmallUtil() {
	};

	private static SmallUtil smallUtil = new SmallUtil();
	public static MediaPlayer mp;

	public static SmallUtil newInstance() {
		return smallUtil;
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			boolean isOk = (boolean) msg.obj;
			if (isOk) {
				if (mp.isPlaying() == false) {
					mp.pause();
				}
			}
			super.handleMessage(msg);
		}
	};

	// 接收到任务进行振动提醒
	public void vibraAlert(Context context) {
		final Vibrator v = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);

		mp = MediaPlayer.create(context, R.raw.lingsheng);
		mp.start();

		// 开启振动模式，振动600毫秒，停400毫秒.循环振动10次
		new Thread(new Runnable() {
			@Override
			public void run() {
				long[] pattern = new long[] { 600, 1000 };
				v.vibrate(pattern, 0);
				int i = 10;
				while (i > 0) {
					try {
						Thread.sleep(1000);
						i--;
						if (i == 1) {
							v.cancel();
							break;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				Message msg = Message.obtain();
				msg.obj = true;
				handler.sendMessage(msg);
			}
		}).start();
	}

}
