package uni.project.sd.comunications.battleship.entity;

import java.util.ArrayList;

import uni.project.sd.comunications.entity.Message;

public class BattleshipToken extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2779370743786969481L;

	private ArrayList<EventListItem> eventList;

	public BattleshipToken() {
		eventList = new ArrayList<>();
	}
	
	public ArrayList<EventListItem> getEventList() {
		return eventList;
	}
	
	public void setEventList(ArrayList<EventListItem> eventList) {
		this.eventList = eventList;
	}
}
