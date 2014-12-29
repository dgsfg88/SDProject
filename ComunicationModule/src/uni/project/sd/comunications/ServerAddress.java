package uni.project.sd.comunications;

import java.util.LinkedList;
/**
 * Rubrica indirizzi, è statica per poter funzionare in più thread
 * TODO aggiungere controlli thread-safe
 * @author Andrea
 *
 */
public class ServerAddress {
	private static ServerAddress addressBook = null;
	private LinkedList<String> serverList;
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
	 * TODO forse si può fare anche privato
	 */
	public ServerAddress() {
		serverList = new LinkedList<String>();
	}
	/**
	 * Aggiunge un server alla rubrica
	 * @param server	Indirizzo del server
	 */
	public void addServer(String server) {
		serverList.add(server);
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
}
