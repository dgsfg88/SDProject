package uni.project.sd;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import uni.project.sd.Control.BattleshipController;
import uni.project.sd.Entity.DummyFrontEntity;
import uni.project.sd.comunications.ComunicationActions;
import uni.project.sd.comunications.IncomingServer;
import uni.project.sd.comunications.OutcomingClient;
import uni.project.sd.comunications.ServerAddress;
import uni.project.sd.comunications.battleship.BattleshipActions;
import uni.project.sd.event.EventCounter;
/**
 * Classe di avvio, TODO aggiungere numerazione messaggi
 * @author Andrea
 *
 */
public class MainClass {
/**
 * Punto di avvio del programma
 * @param args	ID server come primo argomento, ID di tutti gli altri server dopo
 */
	private ServerAddress address;
	private ComunicationActions actions;
	public static void main(String[] args) {
		if(args.length > 1) {
			try {
				//Creazione del server di ricezione
				LocateRegistry.createRegistry(Integer.parseInt(args[0]));
				new IncomingServer(args[0]);
			} catch (RemoteException e1) {
				e1.printStackTrace();
				System.exit(0);
			}

			//Creazione di una rubrica
			ServerAddress book = ServerAddress.getInstance();
			book.setMyAddress(args[0]);
			for(int k = 1; k < args.length; k++) {
				book.addServer(args[k]);
			}
			EventCounter.getInstance(book);
			Thread sendPing = new Thread(new Runnable() {
				
				@Override
				public void run() {
					ServerAddress address = ServerAddress.getInstance();
					boolean ready = false;
					while(!ready) {
						Integer result = 0;
						for(int k = 0; k < address.serverNumber(); k++) {
							OutcomingClient client = new OutcomingClient(OutcomingClient.sendPing);
							client.doCustomRmiHandling(address.getServer(k));
							result += client.getResult();
						}
						if(result == address.serverNumber())
							ready = true;
						else {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					DummyFrontEntity.getInstance().addMessage("All online, starting game");
					
					boolean imfirst = true;
					
					Integer me = Integer.parseInt(address.getMyAddress());
					for(int k = 0; k < address.serverNumber(); k++) {
						if(Integer.parseInt(address.getServer(k)) < me) {
							imfirst = false;
							break;
						}
					}
					
					if(imfirst) {
						new ComunicationActions().cicleToken();
						DummyFrontEntity.getInstance().setPlayerTurn(imfirst);
					}
					while(true) {
						for(int k = 0; k < address.serverNumber(); k++) {
							//Invio di un messaggio di ping ad ogni altro nodo
							if(address.getServerStatus(address.getServer(k))) {
								OutcomingClient client = new OutcomingClient(OutcomingClient.sendPing);
								client.doCustomRmiHandling(address.getServer(k));
								Integer result = client.getResult();
								if(result == 0) {
									//TODO Avvenuto crash di un nodo, avviare azione di recovery
									
									address.setServerStatus(address.getServer(k), false);
									BattleshipController.getInstance(null, 0).destroyPlayer(k);
									try {
										if(address.getServer(k).equals(address.getTokenPosition())) {
											DummyFrontEntity.getInstance().addMessage("\n\nTOKEN PERSO\n\n");
											new ComunicationActions().requestToken();
										}}
										catch (IndexOutOfBoundsException e){
											//TODO Non sono riuscito a capire cosa causa l'exception, ma il processo che riceverà il token inizia a farla
									}
								}
								try {
									DummyFrontEntity.getInstance().addMessage("Me: " + address.getMyAddress() + " Next: " + address.getNextOnline());
								} catch (Exception e) {
									DummyFrontEntity.getInstance().addMessage("I'm only online, I win");
								}
							}
						}
						try {
							//TODO aggiungere controlli di terminazione
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
			new MainClass();
			sendPing.start();
		}
	}
	

	public MainClass() {

		address = ServerAddress.getInstance();
		BattleshipController.getInstance(this, address.serverNumber() +1);
		this.actions = new ComunicationActions();
		
	}
	
	public void relaseToken() {
		actions.relaseToken();
	}

	public void sendAction() {
	
	}


	public void relaseToken(int player, int row, int col) {
		new BattleshipActions().sendHit(player, row, col);
		relaseToken();
	}
	

}
