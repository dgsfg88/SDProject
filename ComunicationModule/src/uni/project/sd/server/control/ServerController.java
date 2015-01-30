package uni.project.sd.server.control;

import javax.swing.DefaultListModel;

import uni.project.sd.server.boundary.ServerBoundary;
import uni.project.sd.server.entity.Player;

public class ServerController {

	private String myIP;
	private int portNumber;
	private int nextID = 0;
	private DefaultListModel<Player> playerOnline;
	private ServerBoundary myBoundary;
	
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

	public static void main(String[] args) {
		ServerController controller = new ServerController("127.0.0.1", 1099);
		
		controller.addPlayer("Player_1", "127.0.0.1", 1099);
		controller.addPlayer("Player_1", "127.0.0.1", 1099);
		controller.addPlayer("Player_2", "127.0.0.1", 1099);
		controller.addPlayer("Player_3", "127.0.0.1", 1099);
		
		controller.addPlayer("Player_1", "127.0.0.1", 1099);
	}
}
