package uni.project.sd.comunications;

import java.util.HashMap;
import java.util.LinkedList;
/**
 * Rubrica indirizzi, � statica per poter funzionare in pi� thread
 * TODO aggiungere controlli thread-safe
 * @author Andrea
 *
 */
public class ServerAddress {
	private static ServerAddress addressBook = null;
	private String myAddress;
	private Integer nextServer = null;
	
	private Object lockServerOnline = new Object();
	
	private LinkedList<String> serverList;
	private HashMap<String, Boolean> serverOnline;
	/**
	 * Serve a richiedere un istanza della rubrica
	 * @return	istanza della rubrica
	 */
	public static ServerAddress getInstance () {
		if(addressBook == null) {
			addressBook = new ServerAddress();
		}
		return addressBook;
	}
	/**
	 * costruttore
	 * TODO forse si pu� fare anche privato
	 */
	public ServerAddress() {
		serverList = new LinkedList<String>();
		serverOnline = new HashMap<String, Boolean>();
	}
	/**
	 * Aggiunge un server alla rubrica
	 * @param server	Indirizzo del server
	 */
	public void addServer(String server) {
		serverList.add(server);
		serverOnline.put(server, true);
	}
	public void setServerStatus(String server, boolean status){
		synchronized (lockServerOnline) {
			serverOnline.put(server,status);
		}
	}
	/**
	 * ottiene un server dalla rubrica
	 * @param index	posizione del server
	 * @return		ID del server
	 */
	public String getServer(int index) {
		if(index < serverList.size()) {
			return serverList.get(index);
		} else return null;
	}
	/**
	 * Restituisce il numero di server in rubrica
	 * @return	Numero di server
	 */
	public int serverNumber () {
		return serverList.size();
	}
	public String getMyAddress() {
		return myAddress;
	}
	public void setMyAddress(String myAddress) {
		this.myAddress = myAddress;
	}
	
	public String getNextOnline() {
		if(nextServer == null)
			findNextServer();
		else {
			boolean online = false;
			synchronized (lockServerOnline) {
				online = serverOnline.get(getServer(nextServer));
			}
			if(!online)
				findNextServer();
		}
		return getServer(nextServer);
	}
	private void findNextServer() {
		synchronized (lockServerOnline) {
			int me = Integer.parseInt(myAddress);
			int k = 0;
			while(!serverOnline.get(serverList.get(k)))
				k++;
			nextServer = k;
			int d = Integer.parseInt(serverList.get(k)) - me;
			
			for(k++; k < serverList.size(); k++) {
				if(serverOnline.get(serverList.get(k))) {
					int dtemp = Integer.parseInt(serverList.get(k)) - me;
					if(((dtemp < d || d < 0) && dtemp > 0) ||( dtemp < 0 && d < 0 && dtemp < d)) {
						nextServer = k;
						d = dtemp;
					}
				}
			}
			
		}
	}
}
