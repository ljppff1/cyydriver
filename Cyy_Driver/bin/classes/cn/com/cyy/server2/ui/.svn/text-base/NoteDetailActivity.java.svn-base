package cn.com.cyy.server2.ui;

import cn.com.cyy.server2.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NoteDetailActivity extends BaseActivity{
	private TextView mTVNoteMoretitle;
	private TextView mTVNoteMoreContent;
	private TextView mTVActionBarRight;
	private String position;
	private String theme;
	private String content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_note_record_more);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		initView();
		initData();
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);     

        imm.hideSoftInputFromWindow(mTVNoteMoretitle.getWindowToken(), 0);        
        imm.hideSoftInputFromWindow(mTVNoteMoreContent.getWindowToken(), 0);        


		
	}

	private void initData() {
	     position =   getIntent().getExtras().getString("POSITION");
	     theme =      getIntent().getExtras().getString("THEME");
	     content =    getIntent().getExtras().getString("CONTENT");
	    if(!theme.equals("新建标签")){
	    	mTVNoteMoretitle.setText(theme);
	    	mTVNoteMoreContent.setText(content);
	    }
		
	}

	

	OnClickListener listener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mIvActionBarRight:
				dealwith();
				break;
			case R.id.mIvActionBarLeft:
				finish();
				break;

			default:
				break;
			}
			
		}

	};
	private ImageView mIvActionBarLeft;
	private TextView mTvActionBarCenter;
	
	private void dealwith() {
		if(TextUtils.isEmpty(mTVNoteMoretitle.getText())){
			Toast.makeText(getApplicationContext(), "请填写主题", 0).show();
		}else if(TextUtils.isEmpty(mTVNoteMoreContent.getText())){
			Toast.makeText(getApplicationContext(), "请填写内容", 0).show();
		}else {
			Intent intent =new Intent(getApplicationContext(), NoteActivity.class);
			intent.putExtra("POSITION",position);
			intent.putExtra("TITLE", mTVNoteMoretitle.getText().toString());
			intent.putExtra("CONTENT", mTVNoteMoreContent.getText().toString());
			setResult(2, intent);
			finish();
		}
		
	}

	private void initView() {
		mTVNoteMoretitle =(TextView)this.findViewById(R.id.mTVNoteMoretitle);
		mTVNoteMoreContent =(TextView)this.findViewById(R.id.mTVNoteMoreContent);
		mTVActionBarRight =(TextView)this.findViewById(R.id.mIvActionBarRight);
		mTVActionBarRight.setOnClickListener(listener);
		mIvActionBarLeft =(ImageView)this.findViewById(R.id.mIvActionBarLeft);
		mIvActionBarLeft.setOnClickListener(listener);
		mTvActionBarCenter =(TextView)this.findViewById(R.id.mTvActionBarCenter);
		mTvActionBarCenter.setText("填写现场记录");
		
	}

}
