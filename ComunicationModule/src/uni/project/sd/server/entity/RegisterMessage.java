package uni.project.sd.server.entity;

import uni.project.sd.comunications.entity.Message;

public class RegisterMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3167984332644308197L;

	private String ip;
	private int port;
	private String name;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
