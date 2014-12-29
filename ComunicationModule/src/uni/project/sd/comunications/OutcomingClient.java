package uni.project.sd.comunications;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import saqib.rasul.Compute;
import saqib.rasul.RmiStarter;
/**
 * Classe che corrisponde ad un client RMI, serve ad inviare i messaggi in uscita 
 *  
 * @author Andrea
 *
 */
public class OutcomingClient extends RmiStarter{
	/**
	 * Risultato del ping, TODO da sostituire con lo stato
	 */
	private Integer result = null;
	/**
	 * Costruttore della classe, va inserito l'ID del server da richiamare
	 * 
	 * @param serverID Server con cui si desidera comunicare
	 */
	public OutcomingClient(String serverID) {
		super(RequestState.class);
		doCustomRmiHandling(serverID);
	}

	@Override
	public void doCustomRmiHandling(String serverID) {
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry();
	        Compute compute = (Compute)registry.lookup(Compute.SERVICE_NAME+serverID);
	        RequestState task = new RequestState();
	        result = compute.executeTask(task);
	        
		} catch (RemoteException | NotBoundException e) {
			result = 0;
		}
	}
	
	public Integer getResult(){
		return result;
	}
}
