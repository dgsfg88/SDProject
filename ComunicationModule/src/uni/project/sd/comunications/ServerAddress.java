package uni.project.sd.comunications;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
	private Object lockTokenPosition = new Object();
	
	private int tokenPosition;
	private LinkedList<String> serverList;
	private HashMap<String, Boolean> serverOnline;
	private HashMap<String, String> serverLocation;
	private HashMap<String, Integer> playerID;
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
	 * 
	 */
	protected ServerAddress() {
		serverList = new LinkedList<String>();
		serverOnline = new HashMap<String, Boolean>();
		serverLocation = new HashMap<String,String>();
	}
	/**
	 * Aggiunge un server alla rubrica
	 * @param server	Indirizzo del server
	 */
	public void addServer(String server, String location) {
		serverList.add(server);
		serverOnline.put(server, true);
		serverLocation.put(server, location);
	}
	public void setServerStatus(String server, boolean status){
		synchronized (lockServerOnline) {
			serverOnline.put(server,status);
		}
	}
	public boolean getServerStatus(String server) {
		synchronized (lockServerOnline) {
			return serverOnline.get(server);
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
	public int getServerNID(String ID) {
		return serverList.indexOf(ID);
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
	
	public void setTokenPosition(String tokenPosition) {
		synchronized (lockTokenPosition) {
			this.tokenPosition = serverList.indexOf(tokenPosition);
		}
	}
	public String getTokenPosition() {
		synchronized (lockTokenPosition) {
			if(tokenPosition >= 0)
				return serverList.get(tokenPosition);
			else
				return myAddress;
		}
	}
	
	public String getLocation(String server) {
		return serverLocation.get(server);
	}
	
	public int getPlayerID(String player){
		int id = -1;
		if(playerID == null) {
			
			int i = 1;
			LinkedList<String> pl = new LinkedList<>(this.serverList);
			pl.add(myAddress);
			String[] players = new String[pl.size()];
			pl.toArray(players);
			Arrays.sort(players);
			playerID = new HashMap<>(players.length);
			for(i = 0; i < players.length; i++) {
				playerID.put(players[i], i);
				System.out.println(players[i]+" -> "+i);
			}
		}
		id = playerID.get(player);
		return id;
	}
	    
    public String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
