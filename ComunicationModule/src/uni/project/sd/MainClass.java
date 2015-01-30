package uni.project.sd;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import uni.project.sd.Control.battleship.BattleshipController;
import uni.project.sd.Entity.battleship.Ocean;
import uni.project.sd.comunications.IncomingServer;
import uni.project.sd.comunications.PingRoutine;
import uni.project.sd.comunications.ServerAddress;
import uni.project.sd.comunications.battleship.BattleshipActions;
import uni.project.sd.event.EventCounter;

/**
 * 
 * 
 * @author Andrea
 * 
 */
public class MainClass {
	/**
	 * Punto di avvio del programma
	 * 
	 * @param args
	 *            ID server come primo argomento, ID di tutti gli altri server
	 *            dopo
	 */
	private ServerAddress address;

	public static void main(String[] args) {
		if (args.length > 2) {
			try {
				// Creazione del server di ricezione
				try {
					LocateRegistry.createRegistry(1099);
				} catch (RemoteException e) {
					System.out.println("Port 1099 already in use");
				}
				// Creazione di una rubrica
				ServerAddress book = ServerAddress.getInstance();
				book.setMyAddress(args[0]);
				for (int k = 1; k < args.length; k += 2) {
					book.addServer(args[k], args[k + 1]);
				}

				new MainClass();
				new IncomingServer().doCustomRmiHandling(args[0]);
			} catch (RemoteException e1) {
				e1.printStackTrace();
				System.exit(0);
			}
		} else {
			ServerAddress address = ServerAddress.getInstance();
			address.setMyAddress(args[0]);
			address.addServer("Server", "localhost");
			EventCounter.getInstance(address);
			new BattleshipActions().registerToServer("localhost", 1099);
		}
	}

	public MainClass() {
		address = ServerAddress.getInstance();
		EventCounter.getNewInstance(address);
		BattleshipController controller = BattleshipController.getInstance(
				this, address.getPlayerID(address.getMyAddress()),
				address.serverNumber() + 1);
		controller.setOcean(new Ocean(Ocean.splitted, BattleshipController.d,
				address.serverNumber() + 1));
		
		Thread sendPing = new Thread(new PingRoutine());
		sendPing.start();
	}

	public void releaseToken(int player, int row, int col) {
		new BattleshipActions().sendHit(player, row, col);
		new BattleshipActions().relaseToken(BattleshipController.getInstance(null, 0, 0).getEventList());
	}
	
	public void releaseToken(){
		new BattleshipActions().relaseToken(BattleshipController.getInstance(null, 0, 0).getEventList());
	}

}
