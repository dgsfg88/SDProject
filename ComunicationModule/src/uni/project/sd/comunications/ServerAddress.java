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
		serverOnline.put(server,status);
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
}
