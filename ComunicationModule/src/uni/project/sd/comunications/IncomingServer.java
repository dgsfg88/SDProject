package uni.project.sd.comunications;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import saqib.rasul.Compute;
import saqib.rasul.RmiStarter;
/**
 * Server di ricezione dei messaggi, permette di riceve task ed eseguirli
 * @author Andrea
 *
 */
public class IncomingServer extends RmiStarter{
	/**
	 * Istanzia il server con l'ID specificato
	 * @param serverID			ID del server da istanziare
	 * @throws RemoteException	
	 */
	public IncomingServer(String serverID) throws RemoteException {
		super(Compute.class);
		doCustomRmiHandling(serverID);
	}
	/**
	 * Registra il nuovo server in modo che possa ricevere chiamate
	 */
	@Override
	public void doCustomRmiHandling(String serverID) {
		try {
			Compute engine = new ComputeEngine();
			Compute engineStub = (Compute)UnicastRemoteObject.exportObject(engine, 0);

            Registry registry = LocateRegistry.getRegistry(80);
            registry.rebind(Compute.SERVICE_NAME + serverID, engineStub);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}
