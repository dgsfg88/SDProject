package uni.project.sd.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class EventTime implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2064332068519280306L;
	private HashMap<String, Integer> counter;
	private String me;
	
	public EventTime(int nodeNumber, String myID) {
		counter = new HashMap<String, Integer>(nodeNumber);
		counter.put(myID, 0);
		this.me = myID;
	}
	
	public void addNode(String node) {
		counter.put(node, 0);
	}
	
	public void updateNode(String node, int value) {
		counter.put(node, value);
	}
	
	public void incrementMyCounter() {
		updateNode(me, counter.get(me)+1);
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

	public Set<String> keySet() {
		return counter.keySet();
	}
	
}
