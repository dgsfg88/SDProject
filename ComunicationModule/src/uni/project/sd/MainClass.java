package uni.project.sd;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import uni.project.sd.Control.battleship.BattleshipController;
import uni.project.sd.Entity.battleship.Ocean;
import uni.project.sd.boundary.battleship.LoginBoundary;
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
	private static LoginBoundary login = null;

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

				Integer[] ships = {3,2,1,1};
				new MainClass(ships,false,10);
				new IncomingServer().doCustomRmiHandling(args[0]);
			} catch (RemoteException e1) {
				e1.printStackTrace();
				System.exit(0);
			}
		} else {
			login = new LoginBoundary();
		}
	}

	public MainClass(Integer[] shipNumber, boolean oneShotPerShip, int dimension) {
		if(login != null) {
			login.setVisible(false);
		}
		address = ServerAddress.getInstance();
		EventCounter.getNewInstance(address);
		BattleshipController controller = BattleshipController.getInstance(
				this, address.getPlayerID(address.getMyAddress()),
				address.serverNumber() + 1);
		controller.setOcean(new Ocean(Ocean.splitted, dimension,
				address.serverNumber() + 1));
		
		controller.setOneShotPerShip(oneShotPerShip);
		controller.setDimension(dimension);
		controller.startView();
		
		controller.setShipNumber(shipNumber);
		
		Thread sendPing = new Thread(new PingRoutine());
		sendPing.start();
	}
	
	public void sendRealTimeAction(int player, int row, int col) {
		new BattleshipActions().sendHit(player, row, col);
	}
	
	public void releaseToken(){
		new BattleshipActions().relaseToken(BattleshipController.getInstance(null, 0, 0).getEventList());
	}

}
