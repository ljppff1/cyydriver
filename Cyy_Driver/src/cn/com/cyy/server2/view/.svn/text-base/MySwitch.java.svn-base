package cn.com.cyy.server2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.com.cyy.server2.R;

public class MySwitch extends LinearLayout {

	private ImageView maskImage; // 开关遮盖图片
	private boolean isOpen; // 开关当前状态
	private boolean isAninfinish = true; // 动画是否结束
	private float x; // 记录ACTION_DOWN时候的横坐标
	private boolean isChangeByTouch = false; // 是否在一次事件中已经切换过状态

	private OnSwitchChangeListener switchChangeListener; // 监控开关状态

	public interface OnSwitchChangeListener {
		void onSwitchChanged(boolean isOpen);
	}
	
	public MySwitch(Context context) {
		super(context);
		init();
	}

	public MySwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setBackgroundResource(R.drawable.on_off_bg);
		maskImage = new ImageView(getContext());
		maskImage.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		maskImage.setImageResource(R.drawable.switchs_mask);
		addView(maskImage);
	}

	public boolean getSwitchStatus() {
		return isOpen;
	}

	public void setSwitchStatus(boolean isOpen) {
		this.isOpen = isOpen;
		if (isOpen) {
			setGravity(Gravity.RIGHT);
		} else {
			setGravity(Gravity.RIGHT);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			if (event.getX() - x > 5 && !isOpen) { // 向右
				changeStatus();
			} else if (event.getX() - x < -5 && isOpen) { // 向左
				changeStatus();
			}
			break;
		case MotionEvent.ACTION_UP:
			if (Math.abs(event.getX() - x) <= 5) {
				changeStatus();
			}
			isChangeByTouch = false;
			break;
		case MotionEvent.ACTION_CANCEL: {
			isChangeByTouch = false;
			break;
		}

		}

		return true;
	}

	private void changeStatus() {
		if (isAninfinish && !isChangeByTouch) {
			isChangeByTouch = true;
			isOpen = !isOpen;
			isAninfinish = false;
			if (switchChangeListener != null) {
				switchChangeListener.onSwitchChanged(isOpen);
			}
			changeOpenStatusWithAnim(isOpen);
		}
	}

	private void changeOpenStatusWithAnim(boolean open) {
		if (open) {
			// 左到右
			Animation leftToRight = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE,
					getWidth() - maskImage.getWidth(),
					Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
					0);
			leftToRight.setDuration(300);
			leftToRight.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					maskImage.clearAnimation();
					setGravity(Gravity.RIGHT);
					isAninfinish = true;
				}
			});
			maskImage.startAnimation(leftToRight);
		} else {
			// 右到左
			Animation rightToLeft = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE,
					maskImage.getWidth() - getWidth(),
					Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
					0);
			rightToLeft.setDuration(300);
			rightToLeft.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					maskImage.clearAnimation();
					setGravity(Gravity.LEFT);
					isAninfinish = true;
				}
			});
			maskImage.startAnimation(rightToLeft);
		}
	}

	public OnSwitchChangeListener getSwitchChangeListener() {
		return switchChangeListener;
	}

	public void setOnSwitchChangeListener(
			OnSwitchChangeListener switchChangeListener) {
		this.switchChangeListener = switchChangeListener;
	}
}
