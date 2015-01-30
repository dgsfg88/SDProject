package uni.project.sd.boundary.battleship;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import saqib.rasul.RmiStarter;
import uni.project.sd.comunications.IncomingServer;
import uni.project.sd.comunications.ServerAddress;
import uni.project.sd.comunications.battleship.BattleshipActions;
import uni.project.sd.event.EventCounter;

public class LoginActionListener implements ActionListener {
	private LoginBoundary loginBoundary;
	
	public LoginActionListener(LoginBoundary loginBoundary){
		this.loginBoundary = loginBoundary;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int port = 1099;
		boolean online = false;
		ServerAddress serverAddress = ServerAddress.getInstance();
		serverAddress.setMyAddress(this.loginBoundary.getUsername());
		serverAddress.addServer("Server", this.loginBoundary.getServer(), this.loginBoundary.getServerPort());
		EventCounter.getInstance(serverAddress);
		do{
			try {
				LocateRegistry.createRegistry(port);
			} catch (RemoteException error) {
				System.out.println("Warning: Port "+port+" already in use");
			}
			try {
				online = new IncomingServer().startServer(this.loginBoundary.getUsername(),port);
			} catch (RemoteException error) {
				error.printStackTrace();
				System.err.println("Error on starting server");
			}
			if(!online)
				port++;
		} while(!online);
		new BattleshipActions().registerToServer(RmiStarter.getIP(), 1099);
	}

}
