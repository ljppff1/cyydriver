package cn.com.cyy.server2.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 郑则表达是判断合法性的工具类
 * @author hurenji
 *
 */
public class RegexJudgeUtil {
	
	/**
	 * 判断电话输入是否合法
	 * @param phone 电话号码
	 * @return	是否合法
	 */
	public static boolean isMobileNo(String phone){
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(phone);
		return m.matches();
	}

}
