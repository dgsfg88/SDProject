package uni.project.sd;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import uni.project.sd.Control.battleship.BattleshipController;
import uni.project.sd.Entity.DummyFrontEntity;
import uni.project.sd.Entity.battleship.Ocean;
import uni.project.sd.comunications.ComunicationActions;
import uni.project.sd.comunications.IncomingServer;
import uni.project.sd.comunications.OutcomingClient;
import uni.project.sd.comunications.ServerAddress;
import uni.project.sd.comunications.battleship.BattleshipActions;
import uni.project.sd.event.EventCounter;

/**
 * Classe di avvio, TODO aggiungere numerazione messaggi
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
	private ComunicationActions actions;

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
				EventCounter.getInstance(book);

				new MainClass();
				new IncomingServer(args[0]);
			} catch (RemoteException e1) {
				e1.printStackTrace();
				System.exit(0);
			}

			Thread sendPing = new Thread(new Runnable() {

				@Override
				public void run() {
					ServerAddress address = ServerAddress.getInstance();
					boolean ready = false;
					while (!ready) {
						Integer result = 0;
						for (int k = 0; k < address.serverNumber(); k++) {
							OutcomingClient client = new OutcomingClient(
									OutcomingClient.sendPing);
							client.doCustomRmiHandling(address.getServer(k));
							result += client.getResult();
						}
						if (result == address.serverNumber())
							ready = true;
						else {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					DummyFrontEntity.getInstance().addMessage(
							"All online, starting game");

					boolean imfirst = true;

					Integer me = Integer.parseInt(address.getMyAddress());
					for (int k = 0; k < address.serverNumber(); k++) {
						if (Integer.parseInt(address.getServer(k)) < me) {
							imfirst = false;
							break;
						}
					}

					if (imfirst) {
						new ComunicationActions().cicleToken();
						// DummyFrontEntity.getInstance().setPlayerTurn(imfirst);
					}
					imfirst = true;
					while (imfirst) {
						try {
							String k = address.getNextOnline();

							OutcomingClient client = new OutcomingClient(
									OutcomingClient.sendPing);
							client.doCustomRmiHandling(k);
							Integer result = client.getResult();
							if (result == 0) {
								// TODO Avvenuto crash di un nodo, avviare
								// azione di recovery
								new ComunicationActions().nodeDown(k);
								address.setServerStatus(k, false);
								DummyFrontEntity.getInstance().addMessage(
										"Node " + k
												+ " is down, token position: "
												+ address.getTokenPosition());
								DummyFrontEntity.getInstance().destroyPlayer(
										address.getServerNID(k));
							}
						} catch (Exception e) {
							DummyFrontEntity.getInstance().addMessage(
									"I'm only online, I win");
							imfirst = false;
						}
						try {
							// TODO aggiungere controlli di terminazione
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
			sendPing.start();
		}
	}

	public MainClass() {

		address = ServerAddress.getInstance();
		this.actions = new ComunicationActions();
		BattleshipController controller = BattleshipController.getInstance(
				this, address.getPlayerID(address.getMyAddress()),
				address.serverNumber() + 1);
		controller.setOcean(new Ocean(Ocean.splitted, BattleshipController.d,
				address.serverNumber() + 1));
	}

	public void relaseToken() {
		actions.relaseToken();
	}

	public void sendAction() {

	}

	public void relaseToken(int player, int row, int col) {
		new BattleshipActions().sendHit(player, row, col);
		// TODO Implementare un relase token
		
	}

}
