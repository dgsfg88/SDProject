import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import uni.project.sd.comunications.IncomingServer;


public class EntryPoint {

	public static void main(String[] args) {
		try {
			LocateRegistry.createRegistry(1099);
			new IncomingServer("1099");
		} catch (RemoteException e1) {
			e1.printStackTrace();
			System.exit(0);
		}
	}

}
