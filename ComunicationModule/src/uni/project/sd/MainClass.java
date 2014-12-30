package uni.project.sd;

import java.rmi.RemoteException;

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
	public static void main(String[] args) {
		if(args.length > 1) {
			try {
				//Creazione del server di ricezione
				new IncomingServer(args[0]);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}

			//Creazione di una rubrica
			ServerAddress book = ServerAddress.getInstance();
			for(int k = 1; k < args.length; k++) {
				book.addServer(args[k]);
			}
			
			Thread sendPing = new Thread(new Runnable() {
				
				@Override
				public void run() {
					ServerAddress address = ServerAddress.getInstance();
					while(true) {
						for(int k = 0; k < address.serverNumber(); k++) {
							//Invio di un messaggio di ping ad ogni altro nodo
							OutcomingClient client = new OutcomingClient(address.getServer(k));
							Integer result = client.getResult();
							System.out.println(address.getServer(k)+": "+result);
							viewController.printMessage(address.getServer(k)+": "+result);
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
	}
	
public void relaseToken() {
	// TODO Auto-generated method stub
	
}

public void sendAction() {
	// TODO Auto-generated method stub
	
}
	
	

}
