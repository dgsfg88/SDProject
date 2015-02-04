package uni.project.sd.comunications.battleship.entity;

import java.util.ArrayList;

import uni.project.sd.Entity.battleship.Ocean;
import uni.project.sd.comunications.entity.Token;

public class BattleshipTokenContainer implements Token {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6795125025195690335L;
	
	private ArrayList<EventListItem> eventList;
	private Ocean ocean;
	
	public BattleshipTokenContainer(ArrayList<EventListItem> eventList, Ocean ocean) {
		this.eventList = eventList;
		this.ocean = ocean;
	}
	
	public ArrayList<EventListItem> getEventList() {
		return eventList;
	}
	public Ocean getOcean() {
		return ocean;
	}
	
}
