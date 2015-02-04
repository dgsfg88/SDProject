package uni.project.sd.boundary.battleship.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import saqib.rasul.RmiStarter;
import uni.project.sd.boundary.battleship.LoginBoundary;
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
		loginBoundary.setEnabled(false);
		int port = 1099;
		boolean online = false;
		ServerAddress serverAddress = ServerAddress.getInstance();
		serverAddress.setMyAddress(this.loginBoundary.getUsername());
		serverAddress.addServer("Server", this.loginBoundary.getServer(), this.loginBoundary.getServerPort());
		EventCounter.getInstance(serverAddress);
		do{
			try {
				LocateRegistry.createRegistry(port);
				online = new IncomingServer().startServer(this.loginBoundary.getUsername(),port);
			} catch (RemoteException error) {
				System.out.println("Warning: Port "+port+" already in use");
			}
			if(!online)
				port++;
		} while(!online);
		if(online) {
			new BattleshipActions().registerToServer(RmiStarter.getIP(), port);
		}  else
			loginBoundary.setEnabled(true);
	}

}
