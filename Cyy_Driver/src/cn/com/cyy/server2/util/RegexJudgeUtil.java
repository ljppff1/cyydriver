package cn.com.cyy.server2.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ֣��������жϺϷ��ԵĹ�����
 * @author hurenji
 *
 */
public class RegexJudgeUtil {
	
	/**
	 * �жϵ绰�����Ƿ�Ϸ�
	 * @param phone �绰����
	 * @return	�Ƿ�Ϸ�
	 */
	public static boolean isMobileNo(String phone){
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(phone);
		return m.matches();
	}

}