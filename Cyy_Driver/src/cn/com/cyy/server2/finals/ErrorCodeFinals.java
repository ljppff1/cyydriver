package cn.com.cyy.server2.finals;

/**
 * �����𷵻ش�����Ϣ�ı�Ŵ���
 * @author hurenji
 *
 */
public class ErrorCodeFinals {

	public static String throwError(String code){
		
		String codeNum = "";
		
		switch (code) {
		case "20040":
			codeNum = "��ȡ��֤��ʧ��,���Ժ�����";
			break;
		case "20140":
			codeNum = "ע��ʧ��,���Ժ�����";
			break;
		}
		return codeNum;
		
	}
	
}
