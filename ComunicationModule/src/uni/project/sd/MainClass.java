package uni.project.sd;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import uni.project.sd.Control.DummyController;
import uni.project.sd.comunications.IncomingServer;
import uni.project.sd.comunications.OutcomingClient;
import uni.project.sd.comunications.ServerAddress;
/**
 * Classe di avvio, TODO va sostituita
 * @author Andrea
 *
 */
public class MainClass {
/**
 * Punto di avvio del programma
 * @param args	ID server come primo argomento, ID di tutti gli altri server dopo
 */
	private static DummyController viewController;
	private ServerAddress address;
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
			
			Thread sendPing = new Thread(new Runnable() {
				
				@Override
				public void run() {
					ServerAddress address = ServerAddress.getInstance();
					boolean ready = false;
					while(!ready) {
						Integer result = 0;
						for(int k = 0; k < address.serverNumber(); k++) {
							OutcomingClient client = new OutcomingClient(address.getServer(k));
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
					viewController.printMessage("All online, starting game");
					while(true) {
						for(int k = 0; k < address.serverNumber(); k++) {
							//Invio di un messaggio di ping ad ogni altro nodo
							OutcomingClient client = new OutcomingClient(address.getServer(k));
							Integer result = client.getResult();
							if(result == 0)
								address.setServerStatus(address.getServer(k), false);
							else
								address.setServerStatus(address.getServer(k), true);
							System.out.println(address.getServer(k)+": "+result);
							viewController.printMessage(address.getServer(k)+": "+result);
							try {
								viewController.printMessage("Me: " + address.getMyAddress() + " Next: " + address.getNextOnline());
							} catch (Exception e) {
								viewController.printMessage("I'm only online, I win");
							}
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
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
		viewController = new DummyController(this);
		
		boolean imfirst = true;
		
		address = ServerAddress.getInstance();
		Integer me = Integer.parseInt(address.getMyAddress());
		for(int k = 0; k < address.serverNumber(); k++) {
			if(Integer.parseInt(address.getServer(k)) < me) {
				imfirst = false;
				break;
			}
		}
		
		if(imfirst)
			viewController.takeToken();
	}
	
	public void relaseToken() {
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Integer result = 0;
				while(result == 0) {
					OutcomingClient client = new OutcomingClient(address.getNextOnline(),OutcomingClient.sendToken);
					result = client.getResult();
					viewController.printMessage("Token relased, result: "+result);
					if(result == 0)
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			}
		});
		t.start();
	}

	public void sendAction() {
	
	}
	
	

}
