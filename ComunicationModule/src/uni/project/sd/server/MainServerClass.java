package uni.project.sd.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import saqib.rasul.RmiStarter;
import uni.project.sd.comunications.IncomingServer;
import uni.project.sd.comunications.ServerAddress;
import uni.project.sd.server.control.BackgroudServerThread;
import uni.project.sd.server.control.ServerController;

public class MainServerClass {

	public static void main(String[] args) {
		String myIP = "";
		int port = -1;
		if(args.length > 1){
			for(int k = 0; k < args.length; k++){
				try {
					switch (args[k]) {
					case "-ip":
						k++;
						myIP = args[k];
						break;
					case "-p":
						k++;
						port = Integer.parseInt(args[k]);
						break;
					default:
						new Exception();
						break;
					}
				} catch (Exception e) {
					System.out.println("no valid input in " + args[k-1]);
				}
			}
		}
		if(myIP.equals(""))
			myIP = RmiStarter.getIP();
		if(port <= 0)
			port = 1099;
		boolean online = false;
		do{
			try {
				LocateRegistry.createRegistry(port);
			} catch (RemoteException e) {
				System.out.println("Warning: Port "+port+" already in use");
			}
			try {
				online = new IncomingServer().startServer("Server",port);
			} catch (RemoteException e) {
//				e.printStackTrace();
				System.err.println("Error on starting server");
			}
			if(!online)
				port++;
		} while(!online);
		
		ServerController.setInstance(new ServerController(myIP, port));
		ServerAddress address = ServerAddress.getInstance();
		address.setMyAddress("Server");
		
		new Thread(new BackgroudServerThread()).start();
	}

}
