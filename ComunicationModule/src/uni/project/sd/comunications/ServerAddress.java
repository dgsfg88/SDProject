package uni.project.sd.comunications;

import java.util.LinkedList;

public class ServerAddress {
	private static ServerAddress addressBook = null;
	private LinkedList<String> serverList;
	public static ServerAddress getInstance () {
		if(addressBook == null) {
			addressBook = new ServerAddress();
		}
		return addressBook;
	}
	
	public ServerAddress() {
		serverList = new LinkedList<String>();
	}
	
	public void addServer(String server) {
		serverList.add(server);
	}
	public String getServer(int index) {
		if(index < serverList.size()) {
			return serverList.get(index);
		} else return null;
	}
	public int serverNumber () {
		return serverList.size();
	}
}
