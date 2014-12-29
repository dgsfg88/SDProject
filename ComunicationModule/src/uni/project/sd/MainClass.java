package uni.project.sd;

import java.rmi.RemoteException;

import uni.project.sd.comunications.IncomingServer;
import uni.project.sd.comunications.OutcomingClient;
import uni.project.sd.comunications.ServerAddress;

public class MainClass {

	public static void main(String[] args) {
		if(args.length > 1) {
			try {
				new IncomingServer(args[0]);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}

		
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
							OutcomingClient client = new OutcomingClient(address.getServer(k));
							Integer result = client.getResult();
							System.out.println(address.getServer(k)+": "+result);
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
			
			sendPing.start();
		}
	}

}
