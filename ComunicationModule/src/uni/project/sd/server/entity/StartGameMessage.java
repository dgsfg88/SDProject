package uni.project.sd.server.entity;

import java.util.List;
import java.util.ArrayList;

import uni.project.sd.comunications.entity.Message;

public class StartGameMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5374513161557928658L;
	private ArrayList<Player> players;
	private Integer[] shipNumber;
	
	public StartGameMessage(int shipTypes) {
		this.shipNumber = new Integer[shipTypes];
		this.players = new ArrayList<>();
	}
	
	public StartGameMessage(int shipTypes, List<Player> players) {
		this.shipNumber = new Integer[shipTypes];
		this.players = new ArrayList<>(players);
	}
	
	public void setShipNumber(int shipIndex, int number) {
		this.shipNumber[shipIndex] = number;
	}
	
	public void addPlayer(Player player) {
		this.players.add(player);
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public Integer[] getShipNumber() {
		return shipNumber;
	}
}
