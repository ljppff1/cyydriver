package cn.com.cyy.server2.ui;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.cyy.server2.R;
import cn.com.cyy.server2.model.NoteModel;

public class NoteActivity extends BaseActivity{
	private List<NoteModel> list =new ArrayList<NoteModel>();
	private TextView mTVActionBarRight;
	private MyAdapter adapter;
	private ListView mLVNoteRecord;
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_record);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		initView();
		
		adapter = new MyAdapter(NoteActivity.this);;
		mLVNoteRecord.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
	}
	 
	 
		

		private void initNewData() {
		       NoteModel nm =new NoteModel("新建标签", "点击输入正文");
		       list.add(nm);
		       handler.sendEmptyMessage(1);
	}

		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			switch (requestCode) {
			case 1:

				switch (resultCode) {
				case 2:
					int  position =	Integer.valueOf(data.getStringExtra("POSITION"));
					String theme =	data.getStringExtra("TITLE");
					String content =	data.getStringExtra("CONTENT");
					list.get(position).setTheme(theme);
					list.get(position).setContent(content);
					handler.sendEmptyMessage(1);
					break;

				default:
					break;
				}
				break;

			default:
				break;
			}
			super.onActivityResult(requestCode, resultCode, data);

		}
		private Handler handler =new Handler(){
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;

				default:
					break;
				}
				
			};
		};
		private TextView mTvActionBarCenter;
		private ImageView mIvActionBarLeft;
		private TextView mTVNoteFinish;


		private void initView() {
			mTVActionBarRight =(TextView)this.findViewById(R.id.mIvActionBarRight);
			mTVActionBarRight.setText("新建");
			mTVActionBarRight.setOnClickListener(listener);
			mTvActionBarCenter =(TextView)this.findViewById(R.id.mTvActionBarCenter);
			mTvActionBarCenter.setText("填写现场记录");
			mIvActionBarLeft =(ImageView)this.findViewById(R.id.mIvActionBarLeft);
			mIvActionBarLeft.setOnClickListener(listener);
			mTVNoteFinish =(TextView)this.findViewById(R.id.mTVNoteFinish);
			mTVNoteFinish.setOnClickListener(listener);
			mLVNoteRecord =(ListView)this.findViewById(R.id.mLVNoteRecord);
			mLVNoteRecord.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent =new Intent(NoteActivity.this, NoteDetailActivity.class);
					intent.putExtra("POSITION", position+"");
					intent.putExtra("THEME", list.get(position).getTheme());
					intent.putExtra("CONTENT", list.get(position).getContent());
					startActivityForResult(intent, 1);
					
				}
			});
	}

	OnClickListener listener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			    //添加
			case R.id.mIvActionBarRight:
				initNewData();
				break;
			case R.id.mIvActionBarLeft:
				finish();
				break;
				//完成
			case R.id.mTVNoteFinish:
				
				break;

			default:
				break;
			}
			
		}
	};


		public class MyAdapter extends BaseAdapter{
	        private Context context;
	        private LayoutInflater inflater;
			
			public MyAdapter(Context context) {
				this.context =context;
				inflater=LayoutInflater.from(context);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return list.size();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return list.get(position);
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
			GroupHolder groupHolder;
				if(convertView==null){
					groupHolder=new GroupHolder();
					convertView=inflater.inflate(R.layout.item_note, null);
					groupHolder.theme=(TextView)convertView.findViewById(R.id.mTVnoteTheme);
					groupHolder.content=(TextView)convertView.findViewById(R.id.mTVnoteContent);
					convertView.setTag(groupHolder);
				}else{
					groupHolder=(GroupHolder)convertView.getTag();
				}
				
				groupHolder.theme.setText(list.get(position).getTheme());
				groupHolder.content.setText(list.get(position).getContent());
				
				
				return convertView;
			}
		}

		public class GroupHolder {
           TextView theme,content;
		}

}
