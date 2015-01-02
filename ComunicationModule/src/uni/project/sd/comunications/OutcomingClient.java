package uni.project.sd.comunications;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import saqib.rasul.Compute;
import saqib.rasul.RmiStarter;
import saqib.rasul.Task;
/**
 * Classe che corrisponde ad un client RMI, serve ad inviare i messaggi in uscita 
 *  
 * @author Andrea
 *
 */
public class OutcomingClient extends RmiStarter{
	public final static int sendPing = 0;
	public final static int sendToken = 1;
	/**
	 * Risultato del ping, TODO da sostituire con lo stato
	 */
	private Integer result = null;
	private int taskType;
	/**
	 * Costruttore della classe, va inserito l'ID del server da richiamare
	 * 
	 * @param serverID Server con cui si desidera comunicare
	 */
	public OutcomingClient(String serverID) {
		super(RequestState.class);
		taskType = sendPing;
		doCustomRmiHandling(serverID);
	}
	public OutcomingClient(String serverID, int taskType) {
		super(RequestState.class);
		this.taskType = taskType;
		doCustomRmiHandling(serverID);
	}
	/**
	 * Invia una richiesta ad un nodo con ID specifico
	 */
	@Override
	public void doCustomRmiHandling(String serverID) {
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry();
	        Compute compute = (Compute)registry.lookup(Compute.SERVICE_NAME+serverID);
	        //Scelta del task da richiedere
	        Task task = null;
	        switch (taskType) {
			case sendPing:
				task = new RequestState();
				break;
			case sendToken:
				task = new takeToken();
				break;
			default:
				break;
			}
	        result = compute.executeTask(task);
	        
		} catch (RemoteException | NotBoundException e) {
			result = 0;
		}
	}
	
	public Integer getResult(){
		return result;
	}
}
