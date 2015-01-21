package uni.project.sd.Entity.battleship;

import java.io.Serializable;
import java.util.HashMap;

public abstract class Ship implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5554987231204019919L;
	protected int playerN;
	protected int length;
	protected int health;
	protected int cannons;
	protected HashMap<Bomb, Integer> myAmmo;
	
	public Ship(int playerN) {
		this.playerN = playerN;
		this.length = getLength();
		this.health = this.length;
		this.cannons = getCannons();
		this.myAmmo = getMyAmmo();
	}
	
	public Integer setSunk() {
		this.health = this.health - 1;
		return this.health;
	}

	public int getPlayer() {
		return this.playerN;
	}

	public int getPlayerN() {
		return playerN;
	}
	
	public int getHealth() {
		return health;
	}

	public abstract HashMap<Bomb, Integer> getMyAmmo();
	
	public abstract int getLength();

	public abstract int getCannons();
	
	

}
