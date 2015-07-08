package cn.com.cyy.server2.finals;

/**
 * 服务起返回错误信息的编号错误
 * @author hurenji
 *
 */
public class ErrorCodeFinals {

	public static String throwError(String code){
		
		String codeNum = "";
		
		switch (code) {
		case "20040":
			codeNum = "获取验证码失败,请稍后重试";
			break;
		case "20140":
			codeNum = "注册失败,请稍后重试";
			break;
		}
		return codeNum;
		
	}
	
}
