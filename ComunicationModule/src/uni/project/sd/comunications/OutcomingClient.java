package uni.project.sd.comunications;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import saqib.rasul.Compute;
import saqib.rasul.RmiStarter;
import saqib.rasul.Task;
import uni.project.sd.comunications.entity.Message;
import uni.project.sd.comunications.task.NodeDown;
import uni.project.sd.comunications.task.NotifyToken;
import uni.project.sd.comunications.task.RequestState;
import uni.project.sd.comunications.task.RequestToken;
import uni.project.sd.comunications.task.takeToken;
/**
 * Classe che corrisponde ad un client RMI, serve ad inviare i messaggi in uscita 
 *  
 * @author Andrea
 *
 */
public class OutcomingClient extends RmiStarter{
	public final static int sendPing = 0;
	/**
	 * @deprecated use notifyToken to pass token
	 */
	@Deprecated
	public final static int sendToken = 1;
	public final static int notifyToken = 2;
	public final static int requestToken = 3;
	public final static int nodeDown = 4;
	/**
	 * Risultato del ping, TODO da sostituire con lo stato
	 */
	private Integer result = null;
	private int taskType;
	private Message m;
	/**
	 * Costruttore della classe, va inserito l'ID del server da richiamare
	 * 
	 * @param serverID Server con cui si desidera comunicare
	 */
	public OutcomingClient() {
		super(RequestState.class);
		taskType = sendPing;
	}
	public OutcomingClient(int taskType) {
		super(RequestState.class);
		this.taskType = taskType;
	}
	public OutcomingClient(int taskType, Message message) {
		super(RequestState.class);
		this.taskType = taskType;
		this.m = message;
	}
	/**
	 * Invia una richiesta ad un nodo con ID specifico
	 */
	@Override
	public void doCustomRmiHandling(String serverID) {
		
	        //Scelta del task da richiedere
	        Task<Integer> task = null;
	        switch (taskType) {
			case sendPing:
				task = new RequestState();
				break;
			case sendToken:
				task = new takeToken();
				break;
			case notifyToken:
				task = new NotifyToken(m);
				break;
			case requestToken:
				task = new RequestToken();
				((RequestToken)task).setMessage(m);
				break;
			case nodeDown:
				task = new NodeDown();
				((NodeDown)task).setMessage(m);
				break;
			default:
				result = 1;
				break;
			}

	        doTask(serverID, task);
	}
	
	protected void doTask(String serverID,Task<Integer> task) {
		Registry registry;
		try {
			String host = ServerAddress.getInstance().getLocation(serverID);
			int port = ServerAddress.getInstance().getServerPort(serverID);
			registry = LocateRegistry.getRegistry(host,port);
	        Compute compute = (Compute)registry.lookup(Compute.SERVICE_NAME+serverID);
	        if(task != null)
	        	result = compute.executeTask(task);
	        
		} catch (RemoteException | NotBoundException e) {
			result = 0;
			e.printStackTrace();
		}
	}
	
	protected int getTaskType() {
		return taskType;
	}
	protected Message getM() {
		return m;
	}
	public Integer getResult(){
		return result;
	}
	public void setMessageAndType(Message message, int messageType) {
		this.m = message;
		this.taskType = messageType;
	}
}
