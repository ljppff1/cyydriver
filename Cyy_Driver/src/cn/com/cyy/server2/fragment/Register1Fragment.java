package cn.com.cyy.server2.fragment;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.com.cyy.server2.R;
import cn.com.cyy.server2.finals.HandlerFinals;
import cn.com.cyy.server2.finals.SharedprefenceFinals;
import cn.com.cyy.server2.net.SetMapToServer;
import cn.com.cyy.server2.ui.RegisterActivity;
import cn.com.cyy.server2.util.RegexJudgeUtil;

/**
 * 注册的第一步
 * 
 * @author hurenji
 * 
 */
public class Register1Fragment extends Fragment implements OnClickListener {

	// 定义主布局文件的控件
	private EditText mEtRegisterPhone; // 手机号码
	private Button mBtRegisterGetCode; // 获取验证码的按钮
	private EditText mEtRegisterCode; // 输入验证码
	private EditText mEtRegisterPwd; // 输入密码
	private Button mBtRegisterNext; // 下一步的按钮

	// 定义一个倒计时的线程
	private CountDownTimer timer;
	// 是否点击了获取验证码
	private boolean isClickCode = false;

	// 定义注册第二步的Fragment
	private Register2Fragment register2Fragment;

	// 获取手机验证码的Handler
	private Handler getCodeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String result = (String) msg.obj;
			switch (msg.what) {
			case HandlerFinals.JSON_INFO:
				Toast.makeText(getActivity(), "请输入验证码" + result,
						Toast.LENGTH_SHORT).show();
				break;
			case HandlerFinals.JSON_IO_ERROR:
				Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT)
						.show();
				break;
			}

		}

	};

	// 使用静态代码块实例化Fragment
	public static Register1Fragment newInstance(String label) {
		Register1Fragment fragment = new Register1Fragment();
		Bundle args = new Bundle();
		args.putString("label", label);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		View viwe = inflater.inflate(R.layout.fragment_register1, null);
		// 初始化控件
		mEtRegisterPhone = (EditText) viwe.findViewById(R.id.mEtRegisterPhone);
		mBtRegisterGetCode = (Button) viwe
				.findViewById(R.id.mBtRegisterGetCode);
		mEtRegisterCode = (EditText) viwe.findViewById(R.id.mEtRegisterCode);
		mEtRegisterPwd = (EditText) viwe.findViewById(R.id.mEtRegisterPwd);
		mBtRegisterNext = (Button) viwe.findViewById(R.id.mBtRegisterNext);

		// 初始化Sharedprefences中的注册的第一步
		mEtRegisterPhone.setText(SharedprefenceFinals.register_shared.getString("phone", ""));
		mEtRegisterCode.setText(SharedprefenceFinals.register_shared.getString("phone_code", ""));
		mEtRegisterPwd.setText(SharedprefenceFinals.register_shared.getString("password", ""));
		isInputOk();
		
		// 实例化注册第二步的Fragment
		register2Fragment = Register2Fragment.newInstance("register2");

		// 初始化下一步控件不能被点击
		mBtRegisterNext.setClickable(false);

		// 设置监听
		mBtRegisterGetCode.setOnClickListener(this);
		mBtRegisterNext.setOnClickListener(this);

		return viwe;
	}

	@Override
	public void onActivityCreated( Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// 电话输入监听
		mEtRegisterPhone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				isInputOk();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		// 验证码输入监听
		mEtRegisterCode.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				isInputOk();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		// 密码输入监听
		mEtRegisterPwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				isInputOk();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

	}

	// 所有控件点击监听的公共方法
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mBtRegisterGetCode: // 获取验证码
			String phone = mEtRegisterPhone.getText().toString().trim();
			if (!phone.equals("") || phone.length() == 11) {
				boolean isMobileNo = RegexJudgeUtil.isMobileNo(phone);
				if (isMobileNo) {
					// 设置点击验证码为true
					isClickCode = true;
					// isInputOk();
					// 设置为不可点击状态,并设置背景颜色为灰色
					mBtRegisterGetCode.setClickable(false);
					mBtRegisterGetCode
							.setBackgroundResource(android.R.color.darker_gray);
					// 开启倒计时的线程
					timer = new CountDownTimer(6000, 1000) {
						@Override
						public void onTick(long millisUntilFinished) {
							mBtRegisterGetCode
									.setText((millisUntilFinished / 1000)
											+ "s后获取");
						}

						@Override
						public void onFinish() {
							mBtRegisterGetCode
									.setBackgroundResource(R.drawable.select_dorangebg_to_sorangebg);
							mBtRegisterGetCode.setClickable(true);
							CharSequence charSequence = getResources()
									.getString(R.string.get_code);
							mBtRegisterGetCode.setText(charSequence);
						}
					}.start();

					// 验证手机号码，获取验证码
					SetMapToServer.getCodeMap(getActivity(), getCodeHandler,
							phone);

				} else {
					Toast.makeText(getActivity(), "请输入正确的手机号码",
							Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(getActivity(), "请输入11位手机号码", Toast.LENGTH_SHORT)
						.show();
			}
			break;

		case R.id.mBtRegisterNext: // 下一步
			// 获取第一步所填的内容
			String phone_get = mEtRegisterPhone.getText().toString().trim();
			String phone_code = mEtRegisterCode.getText().toString().trim();
			String password = mEtRegisterPwd.getText().toString().trim();

			Editor editor = SharedprefenceFinals.register_shared.edit();
			editor.putString("phone", phone_get);
			editor.putString("phone_code", phone_code);
			editor.putString("password", password);
			editor.commit();

			// 跳转到注册的第二步
			((RegisterActivity) getActivity()).replaceFragment(
					register2Fragment, "register2");
			if (timer != null) {
				timer.cancel();
				mBtRegisterGetCode
						.setBackgroundResource(R.drawable.select_dorangebg_to_sorangebg);
				mBtRegisterGetCode.setClickable(true);
				CharSequence charSequence = getResources().getString(
						R.string.get_code);
				mBtRegisterGetCode.setText(charSequence);
			}
			break;
		}
	}

	// 判断是否已经输入完成或符合要求
	private void isInputOk() {
		if (!mEtRegisterPhone.getText().toString().trim().equals("")
				&& mEtRegisterPhone.getText().toString().length() == 11
				&& !mEtRegisterCode.getText().toString().trim().equals("")
				&& mEtRegisterCode.getText().toString().trim().length() == 4
				&& !mEtRegisterPwd.getText().toString().trim().equals("")
				&& mEtRegisterPwd.getText().toString().trim().length() >= 6) {

			mBtRegisterNext
					.setBackgroundResource(R.drawable.select_dorangebg_to_sorangebg);
			mBtRegisterNext.setClickable(true);

		} else {
			mBtRegisterNext.setBackgroundResource(android.R.color.darker_gray);
			mBtRegisterNext.setClickable(false);
		}
	}

}
