package uni.project.sd.comunications;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import saqib.rasul.Compute;
import saqib.rasul.RmiStarter;

public class IncomingServer extends RmiStarter{
	public IncomingServer(String serverID) throws RemoteException {
		super(Compute.class);
		doCustomRmiHandling(serverID);
	}

	@Override
	public void doCustomRmiHandling(String serverID) {
		try {
			Compute engine = new ComputeEngine();
			Compute engineStub = (Compute)UnicastRemoteObject.exportObject(engine, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(Compute.SERVICE_NAME + serverID, engineStub);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
