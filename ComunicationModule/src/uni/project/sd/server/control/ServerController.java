package uni.project.sd.server.control;

import javax.swing.DefaultListModel;

import uni.project.sd.server.boundary.ServerBoundary;
import uni.project.sd.server.entity.Player;

public class ServerController {

	private static ServerController controller = null;
	
	private String myIP;
	private int portNumber;
	private int nextID = 0;
	private DefaultListModel<Player> playerOnline;
	private ServerBoundary myBoundary;
	
	public static void setInstance(ServerController controller) {
		ServerController.controller = controller;
	}
	public static ServerController getInstance() {
		return controller;
	}
	
	public ServerController(String myIP, int portNumber) {
		super();
		this.myIP = myIP;
		this.portNumber = portNumber;
		this.playerOnline = new DefaultListModel<>();
		
		this.myBoundary = new ServerBoundary(this);
		this.myBoundary.setAddress(this.myIP, this.portNumber);
	}
	
	public void addPlayer(String name, String ip, int port) {
		synchronized (playerOnline) {
			Player p = new Player(nextID, port, ip, name);
			if(!playerOnline.contains(p)) {
				playerOnline.addElement(p);
				nextID++;
			}
		}
	}
	
	public void removePlayer(String name, String ip, int port) {
		synchronized (playerOnline) {
			Player p = new Player(nextID, port, ip, name);
			if(playerOnline.contains(p)){
				this.playerOnline.removeElement(p);
			}
		}
	}
	
	public DefaultListModel<Player> getPlayerList() {
		return playerOnline;
	}

	public void startGamePressed() {
		
	}

}
