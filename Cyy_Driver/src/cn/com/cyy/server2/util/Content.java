package cn.com.cyy.server2.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final  class Content {
	//������Ϣ�����ı���
	public static final String PUSH_MSG_TABLE ="msg";
	//��Ϣ���еĵ绰
	public static final String MSG_TELEPHONE ="M_phone";
	//��Ϣ���еĵ�ַ
	public static final String MSG_ADDRESS ="M_address";
	//��Ϣ���еľ���
	public static final String MSG_LONGITUDE ="M_longitude";
	//��Ϣ���е�γ��
	public static final String MSG_LATITUDE ="M_latitude";
	//��Ϣ���е�״̬
	public static final String MSG_STATUS ="M_status";
	
	public static final String HOME_URL = "http://192.168.0.132:8080"; // ���Կ�
/**
	public static final String SOCKET ="connect1.cyy928.com";
	public static final String HTTP ="http://c1.cyy928.com";
**/
//	public static final String SOCKET ="192.168.1.148";
	
	
	public static final String SOCKET ="192.168.1.148";
	public static final String HTTP ="http://192.168.1.148";

 //socket	192.168.1.148
	//115.29.245.128

	//�����ж��Ƿ�����
	public static boolean IS_CONNECT_NET =true;
	//�����ж��Ƿ��˳���¼
	public static boolean IS_CANCEL_LOGIN_NET =false;
	//�����ж��Ƿ�������
	public static boolean IS_OFF_LINE_NET =false;
	//������ʾ����ʱ��ʱ��
	public static int LONG_TIME =10*1000;
	
	
	
}
