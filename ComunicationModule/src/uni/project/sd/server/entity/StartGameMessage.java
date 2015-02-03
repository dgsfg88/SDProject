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
	private int d;
	private boolean oneShotPerShip;
	
	public StartGameMessage(int shipTypes) {
		this.shipNumber = new Integer[shipTypes];
		this.players = new ArrayList<>();
	}
	
	public StartGameMessage(int shipTypes, List<Player> players) {
		this.shipNumber = new Integer[shipTypes];
		this.players = new ArrayList<>(players);
	}
	
	public StartGameMessage(Integer[] shipNumber2, ArrayList<Player> players2) {
		this.shipNumber = shipNumber2;
		this.players = players2;
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

	public int getD() {
		return d;
	}

	public void setD(int d) {
		this.d = d;
	}

	public boolean isOneShotPerShip() {
		return oneShotPerShip;
	}

	public void setOneShotPerShip(boolean oneShotPerShip) {
		this.oneShotPerShip = oneShotPerShip;
	}
	
	
}
