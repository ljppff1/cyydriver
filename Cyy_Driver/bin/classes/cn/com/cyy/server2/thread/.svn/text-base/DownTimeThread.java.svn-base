package cn.com.cyy.server2.thread;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import cn.com.cyy.server2.R;
import cn.com.cyy.server2.adapter.HelpMsgAdapter.ViewHodler;
import cn.com.cyy.server2.model.HelpMsgModel;

/**
 * 任务倒计时的线程
 * 
 * @author hurenji
 */
public class DownTimeThread extends Thread {

	private static DownTimeThread timeThread = new DownTimeThread();
	private boolean isOpen = true;
	private static List<View> list;

	private static int time = 0;
	private static SharedPreferences userInfo;
	private static Context context;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			TextView textTimes = (TextView)msg.obj;
			textTimes.setText((Integer.parseInt(textTimes.getText().toString().trim())-1)+"");
			
			super.handleMessage(msg);
		}
	};

	private DownTimeThread() {
	}

	public static DownTimeThread newInstence(Context context) {
		return timeThread;
	}

	public static DownTimeThread newInstence(List<View> list, Context context) {
		DownTimeThread.list = list;
		return timeThread;
	}

	private void goTime() {
		while (isOpen) {
			for (int i = 0; i < list.size(); i++) {
				View view = list.get(i);
				TextView textTime = (TextView) view
						.findViewById(R.id.mTvTaskItemTime);
				Message msg = Message.obtain();
				msg.obj = textTime;
				handler.sendMessage(msg);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void run() {
		 goTime();
	}

	/**
	 * 设置线程状态,开启或关闭
	 * 
	 * @param isOpen
	 *            is true or false
	 */
	public void openStatus(boolean isOpen) {
		this.isOpen = isOpen;
	}

	/*
	 * public void close(){ if(!isOpen){ timeThread = null; } }
	 */

}
