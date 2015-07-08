package cn.com.cyy.server2.model;

import java.io.Serializable;


/**
 * ������Ԯ����Ϣ��ģ����
 * 
 * @author hurenji
 */
public class HelpMsgModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String taskId; // �����id
	private String serverType; // ��������
	private String address; // ����������λ��
	private String longitude; // ����
	private String latitude; // γ��
	private String userName; // ��������
	private String userPhone; // �����绰
	private String userCarNum; // �����ĳ��ƺ���
	private String userCarType; // �����ĳ���
	private String insurer; // ���չ�˾
	private String other; // ����
	private long downTime = 20;	// ����ʱʱ��
	private int range;		// ����

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private boolean isTimeEnd;
	public HelpMsgModel(String taskId, String serverType, String address,
			String longitude, String latitude, String userName,
			String userPhone, String userCarNum, String userCarType,
			String insurer, String other, long downTime, int range,boolean isTimeEnd) {
		this.taskId = taskId;
		this.serverType = serverType;
		this.address = address;
		this.longitude = longitude;
		this.latitude = latitude;
		this.userName = userName;
		this.userPhone = userPhone;
		this.userCarNum = userCarNum;
		this.userCarType = userCarType;
		this.insurer = insurer;
		this.other = other;
		this.downTime = downTime;
		this.range = range;
		this.isTimeEnd =isTimeEnd;
	}
    public boolean getTimeEnd() {
		return isTimeEnd;
	}

	public void setTimeEnd(boolean isTimeEnd) {
		this.isTimeEnd = isTimeEnd;
	}

	public long getDownTime() {
		return downTime;
	}

	public void setDownTime(long downTime) {
		this.downTime = downTime;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public HelpMsgModel() {
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserCarNum() {
		return userCarNum;
	}

	public void setUserCarNum(String userCarNum) {
		this.userCarNum = userCarNum;
	}

	public String getUserCarType() {
		return userCarType;
	}

	public void setUserCarType(String userCarType) {
		this.userCarType = userCarType;
	}

	public String getInsurer() {
		return insurer;
	}

	public void setInsurer(String insurer) {
		this.insurer = insurer;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

}
