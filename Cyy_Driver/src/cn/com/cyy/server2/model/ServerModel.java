package cn.com.cyy.server2.model;

/**
 * 注册时选择服务的类
 * @author hurenji
 *
 */
public class ServerModel {

	private String id;
	private String servierName;
	public ServerModel() {
	}
	public ServerModel(String id, String servierName) {
		this.id = id;
		this.servierName = servierName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getServierName() {
		return servierName;
	}
	public void setServierName(String servierName) {
		this.servierName = servierName;
	}
	
	
	
	
	
}
