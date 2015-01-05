package uni.project.sd.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import uni.project.sd.comunications.ServerAddress;

/**
 * Orologio vettoriale
 * @author ianni
 *
 */
public class EventCounter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1066848356844265592L;
	private static EventCounter eventCounter;
	private HashMap<String, Integer> counter;
	private String me;
	/**
	 * Restituisce l'oggetto istanziato
	 * 
	 * @param book	Indirizzi degli altri server, necessario solo alla prima richiesta, dopo può essere anche nulla
	 * @return		Contatore del nodo
	 */
	public static EventCounter getInstance(ServerAddress book) {
		if(eventCounter == null)
			eventCounter = new EventCounter(book);
		return eventCounter;
	}
	
	protected EventCounter(ServerAddress book) {
		this.me = book.getMyAddress();
		this.counter = new HashMap<String, Integer>(book.serverNumber());
		for(int k = 0; k < book.serverNumber(); k++)
			this.counter.put(book.getServer(k), 0);
	}
	/**
	 * Aggiunge un valore al proprio contatore
	 * 
	 */
	public void getNextEvent() {
		synchronized (counter) {
			this.counter.put(me, this.counter.get(me)+1);
		}
	}
	/**
	 * Fa il confronto fra se stesso e un altro contatore,
	 * se il valore di questo contatore è aumentato restituisce true
	 * 
	 * @param event						Contatore con cui fare il confronto
	 * @return							true se il contatore è aumentato, false altrimenti
	 * @throws EventNotSameException	viene dato errore in caso i due contatori non contegano gli stessi nodi
	 */
	public boolean isNewEvent(EventCounter event) throws EventNotSameException {
		boolean result = false;
		synchronized (counter) {
			Set<String> keys = counter.keySet();
			for(String k: keys) {
				Integer i = event.getEventAtNode(k);
				if(i == null)
					new EventNotSameException();
				else if(i > counter.get(k)){
					if(k.equals(event.getYourID())) 
						result = true;
					counter.put(k, i);
				}
			}
		}
		return result;
	}
	/**
	 * Restituisce il contatore per un nodo specifico
	 * 
	 * @param node	l'ID del nodo
	 * @return		Il contatore per quel nodo
	 */
	public Integer getEventAtNode(String node) {
		synchronized (counter) {
			return counter.get(node);
		}
	}
	/**
	 * Restituisce l'ID del nodo 
	 * @return ID del nodo
	 */
	public String getYourID() {
		return me;
	}
	
	@Override
	public String toString() {
		synchronized (counter) {
			return counter.toString();
		}
	}
	/**
	 * Exception causata dal fatto che due contatori non siano uguali
	 * @author ianni
	 *
	 */
	public class EventNotSameException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4575743480737187330L;
		
	}
}
