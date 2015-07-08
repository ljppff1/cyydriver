package cn.com.cyy.server2.receiver;

import cn.com.cyy.server2.finals.BroadCastFinals;
import cn.com.cyy.server2.finals.SocketActionFinals;
import cn.com.cyy.server2.service.SocketNetService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

/** 
 * screen状态广播接收者 
 */  
public class ScreenReceiver extends BroadcastReceiver{  
    private AlarmManager alarms;
	private PendingIntent pendingIntent;

	@Override  
    public void onReceive(Context context, Intent intent) {  
        if(Intent.ACTION_SCREEN_ON.equals(intent.getAction())){     
        	 //onScreenOn(context,intent);    	
        	Log.i("MYMYMY", "ACTION_SCREEN_ON");
        }else if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())){  
        	Log.i("MYMYMY", "ACTION_SCREEN_OFF");
        	//onScreenOf(context,intent);

        }else if(Intent.ACTION_USER_PRESENT.equals(intent.getAction())){  
        	Log.i("MYMYMY", "ACTION_USER_PRESENT");

        }  
    }
     
    //屏幕点亮
	private void onScreenOn(Context context, Intent intent) {
		alarms.cancel(pendingIntent);
	}  
         
    //屏幕灭了
	private void onScreenOf(Context context, Intent intent) {
		//得到使用AlarmManager的权限
		 alarms=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		//AlarmManager要做的事情,打开NotificationService(这是个自定义Service稍后会补充)
		Intent broadIntent = new Intent();
		broadIntent.setAction(BroadCastFinals.BROAD_OPER_CONN);
		Bundle bundle = new Bundle();
		bundle.putString("action", SocketActionFinals.ACTION_SCREEN_OFF);
		bundle.putString("json", "0");
		broadIntent.putExtra("bundle", bundle);
		 pendingIntent=PendingIntent.getBroadcast(context, 0, broadIntent, 0);
	    //开始时间
	    long firstime=SystemClock.elapsedRealtime();
		//60秒一个周期，不停的发送广播
	    alarms.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, 5*1000, pendingIntent);
	    
	}  

}  
