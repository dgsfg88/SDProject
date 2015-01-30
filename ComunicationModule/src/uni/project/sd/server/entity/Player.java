package uni.project.sd.server.entity;

import java.io.Serializable;

public class Player implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5359091368498462240L;

	private int id;
	private int port;
	private String ip;
	private String name;
	
	
	
	public Player(int id) {
		super();
		this.id = id;
	}

	public Player(int id, int port, String ip, String name) {
		super();
		this.id = id;
		this.port = port;
		this.ip = ip;
		this.name = name;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getId() {
		return id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Player))
			return false;
		Player other = (Player) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.id+": "+this.name+"@"+this.ip+":"+this.port;
	}
}