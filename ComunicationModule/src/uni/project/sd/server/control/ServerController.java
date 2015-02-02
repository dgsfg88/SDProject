package uni.project.sd.server.control;

import java.util.ArrayList;

import javax.swing.DefaultListModel;

import uni.project.sd.server.boundary.ServerBoundary;
import uni.project.sd.server.entity.Player;
import uni.project.sd.server.entity.StartGameMessage;

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
	
	public boolean addPlayer(String name, String ip, int port) {
		synchronized (playerOnline) {
			Player p = new Player(nextID, port, ip, name);
			if(!playerOnline.contains(p)) {
				playerOnline.addElement(p);
				nextID++;
				
				return false;
			}
		}
		return true;
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
		myBoundary.setSelectorEnable(false);
		ArrayList<Player> players = new ArrayList<>(this.playerOnline.size());
		for(int i = 0; i < this.playerOnline.size(); i++){
			players.add(this.playerOnline.getElementAt(i));
		}
		StartGameMessage message = new StartGameMessage(myBoundary.getShipNumber(),players);
		
		for(int i = 0; i < this.playerOnline.size(); i++){
			ServerOutClass outClass = new ServerOutClass();
			outClass.setMessageAndType(message, 0);
			outClass.doCustomRmiHandling(this.playerOnline.getElementAt(i).getName());
		}
		
		System.exit(0);
	}

}
