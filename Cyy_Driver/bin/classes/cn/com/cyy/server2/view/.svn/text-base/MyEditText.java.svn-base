package cn.com.cyy.server2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class MyEditText extends EditText {
	public MyEditText(Context context) {
		super(context);
	}

	public MyEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}


	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		this.getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(event);
	}
}
