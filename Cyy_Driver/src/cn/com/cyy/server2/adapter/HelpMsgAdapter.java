package cn.com.cyy.server2.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.cyy.server2.R;
import cn.com.cyy.server2.model.HelpMsgModel;
import cn.com.cyy.server2.thread.DownTimeThread;
import cn.com.cyy.server2.timescale.CustomDigitalClock;
import cn.com.cyy.server2.ui.ConfirmOrdersActivity;
import cn.com.cyy.server2.util.CountRangeUtil;
import cn.com.cyy.server2.util.CountRangeUtil.GetRangeResult;
import cn.com.cyy.server2.util.RegexJudgeUtil;

/**
 * ������Ԯ����Ϣ���б�
 * 
 * @author hurenji
 */

public class HelpMsgAdapter extends BaseAdapter {

	private List<HelpMsgModel> list;
	private Context context;
	private ViewHodler hodler;
	// ����һ���û��㲥��Intent
	private Intent intent = null;
	private Bundle bundle = null;
	private final Map<Integer , Boolean> IsMobileMap = new HashMap<Integer , Boolean>();	
	private final Map<Object , Map<String , Object>> taskMap = new HashMap<Object , Map<String , Object>>() ;
	private long aa;
	private long bb;
	private Handler handler ;

	//private static List<View> viewList = new ArrayList<View>();
	 
	public HelpMsgAdapter(List<HelpMsgModel> list, Context context,Handler handler) {
		this.list = list;
		this.context = context;
		this.handler =handler;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final int p = position;
		ViewHodler holder = null;
/*		if (view == null) {
			view = View.inflate(context, R.layout.tesk_list_item, null);
			hodler = new ViewHodler();
			taskItem item=new taskItem();
			item.setHodler(hodler);
			item.view=view;
			hodler.mTvRange = (TextView) view
					.findViewById(R.id.mTvTaskItemRange);
			hodler.mTvType = (TextView) view
					.findViewById(R.id.mTvTaskItemServeType);
			hodler.mTvId = (TextView) view.findViewById(R.id.mTvTaskItemId);
			hodler.mTvAdds = (TextView) view
					.findViewById(R.id.mTvTaskItemAddress);
			hodler.mTvPhone = (TextView) view
					.findViewById(R.id.mTvTaskItemPhone);
			view.setTag(hodler);
			if(viewList.size()==0){
				DownTimeThread t = DownTimeThread.newInstence(viewList, context);
				//t.start();
			}
			viewList.add(view);
		} 
		else {
			hodler = (ViewHodler) view.getTag();
		}
*/		
		view = View.inflate(context, R.layout.tesk_list_item, null);
		hodler = new ViewHodler();
		hodler.mTvRange = (TextView) view
				.findViewById(R.id.mTvTaskItemRange);
		hodler.mTvType = (TextView) view
				.findViewById(R.id.mTvTaskItemServeType);
		hodler.mTvId = (TextView) view.findViewById(R.id.mTvTaskItemId);
		hodler.mTvAdds = (TextView) view
				.findViewById(R.id.mTvTaskItemAddress);
		hodler.mTvPhone = (TextView) view
				.findViewById(R.id.mTvTaskItemPhone);

		
		hodler.mRlRob = (RelativeLayout) view
				.findViewById(R.id.mRlTaskItemRob);
		hodler.mTvText1 =(TextView)view.findViewById(R.id.mTvText1);
		
		hodler.mTvTime = (CustomDigitalClock) view.findViewById(R.id.mTvTaskItemTime);
		 aa=	list.get(position).getDownTime();
		 bb =System.currentTimeMillis();
	

/*		if(!list.get(position).getTimeEnd()){
*/		if(aa>bb){
		//���ļ����¼�
		hodler.mRlRob.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(list.get(position).getDownTime()>System.currentTimeMillis()){
				Intent intent =new Intent(context.getApplicationContext(), ConfirmOrdersActivity.class);
				HelpMsgModel itemlist = list.get(position);
				Bundle bundle =new Bundle();
				bundle.putString("serverType", itemlist.getServerType());
				bundle.putString("carInfo", itemlist.getUserCarNum()+" "+itemlist.getUserCarType());
				bundle.putString("insurer", itemlist.getInsurer());
				bundle.putString("userPhone", itemlist.getUserPhone());
				bundle.putString("taskId", itemlist.getTaskId());
				intent.putExtras(bundle);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.getApplicationContext().startActivity(intent);
				}
			}
		});
		hodler.mRlRob.setBackgroundResource(R.drawable.shape_sgreenbro_sgreenbg_oval);
		hodler.mTvText1.setText("��");
		}else{
			hodler.mRlRob.setBackgroundResource(R.drawable.shape_sblackbro_sgreenbg_oval);
			hodler.mTvText1.setText("�ѵ�ʱ");
			Log.i("MYMYOO", position+" if  deletimeEnd ");
		}
		// ���ñ�ǩ����ֹ��λ
	//	hodler.mTvId.setTag(list.get(position).getTaskId());
        Log.i("HelpMsgAdapter",System.currentTimeMillis()+"");
		hodler.mTvTime.setEndTime(list.get(position).getDownTime());
		hodler.mTvTime.setClockListener(new CustomDigitalClock.ClockListener() { // register the clock's listener
			
			@Override
			public void timeEnd() {
				 deletimeEnd();
			}
			
			@SuppressLint("ResourceAsColor")
			private  void deletimeEnd() {
				list.get(position).setTimeEnd(true);
				hodler.mRlRob.setBackgroundColor(R.color.s_orange);
				hodler.mTvText1.setText("�ѵ�ʱ");
				//notifyDataSetChanged();
				Log.i("MYMYOO", position+" deletimeEnd ");
				handler.sendEmptyMessage(2);
				
			}

			@Override
			public void remainFiveMinutes() {
		      Log.i("MYMYOO", position+"remainFiveMinutes");
			}
		});

/*		// �������
		if (!list.get(position).getTaskId().equals("")) {
			if (hodler.mTvId.getTag().equals(list.get(position).getTaskId())) {
			
			}
		}
*/		// ������λ��
		hodler.mTvAdds.setText(list.get(position).getAddress());
		// ��ȡ��ϵ�˺͵绰
		String name = "";
		if(list.get(position).getUserName().equals("")){
			name = "-";
		}else{
			name = list.get(position).getUserName().substring(0, 1)+"Ůʿ";
		}
		String phone = "";
		boolean isMobile = RegexJudgeUtil.isMobileNo(list.get(position).getUserPhone());
		IsMobileMap.put(p, isMobile);
		if(isMobile){
			phone = "***" + list.get(position).getUserPhone().substring(list.get(position).getUserPhone().length()-4, list.get(position).getUserPhone().length());
		}else{
			phone = "����ϵ�绰";
		}
		hodler.mTvPhone.setText("��ϵ����("+name+phone+")");
		
		// �������绰
		hodler.mTvPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("p-->" + p);
				if(IsMobileMap.get(p)){
					// ����绰
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:" + list.get(p).getUserPhone()));
					context.startActivity(intent);
				}else{
					// ��ʾ
					Toast.makeText(context, "�Է�δ�ṩ��ϵ�绰"+p, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		// ȡ����ť���
		/*
		 * hodler.mIvCancle.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { System.out.println("p-->" +
		 * p); // ʹ�ù㲥��Ҫ�㲥��item��Index����ȥ intent = new
		 * Intent("cancel_msgs_item"); bundle = new Bundle();
		 * bundle.putInt("position", p); intent.putExtra("bundle", bundle);
		 * context.sendBroadcast(intent); } });
		 */
		
		return view;
	}

	public class ViewHodler {
		private TextView mTvRange;
		private TextView mTvType;
		private TextView mTvId;
		private TextView mTvAdds;
		private TextView mTvPhone;
		private TextView mTvText1;
		private RelativeLayout mRlRob;
		private CustomDigitalClock mTvTime;
	}

	
	class taskItem{
		public ViewHodler hodler;
		public View view;
		public void setHodler(ViewHodler arg_hodler){
			hodler=arg_hodler;
		}
	}


}




