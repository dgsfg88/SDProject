package uni.project.sd.event;

import java.util.Set;

import uni.project.sd.comunications.ServerAddress;

/**
 * Orologio vettoriale
 * @author ianni
 *
 */
public class EventCounter {

	private static EventCounter eventCounter;
	private EventTime myCounter;
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
		this.myCounter = new EventTime(book.serverNumber(), book.getMyAddress());

		for(int k = 0; k < book.serverNumber(); k++)
			this.myCounter.addNode(book.getServer(k));
	}
	/**
	 * Aggiunge un valore al proprio contatore
	 *  
	 */
	public EventTime getNextEvent() {
		synchronized (myCounter) {
			this.myCounter.incrementMyCounter();
			System.out.println("ho incrementato il mio contatore, ora è "+myCounter.getEventAtNode(myCounter.getYourID()));
			return myCounter;
		}
	}
	
	public EventTime getActualEvent(){
		synchronized (myCounter) {
			return myCounter;
		}
	}
	
	public boolean isNotActualEvent(EventTime event){
		synchronized (myCounter) {
			String node = myCounter.getYourID();
			if(event.getEventAtNode(node).equals(myCounter.getEventAtNode(node))){
				return false;
			} else {
				return true;
			}
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
	public boolean isNewEvent(EventTime event) throws EventNotSameException {
		boolean result = false;
		synchronized (myCounter) {
			Set<String> keys = myCounter.keySet();
			for(String k: keys) {
				Integer i = event.getEventAtNode(k);
				if(i == null)
					new EventNotSameException();
				else if(i > myCounter.getEventAtNode(k)){
					if(k.equals(event.getYourID())) 
						result = true;
					myCounter.updateNode(k, i);
				}
			}
		}
		return result;
	}
	
	
	@Override
	public String toString() {
		synchronized (myCounter) {
			return myCounter.toString();
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
