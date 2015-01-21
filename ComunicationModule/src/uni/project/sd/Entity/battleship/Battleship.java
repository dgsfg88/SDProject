package uni.project.sd.Entity.battleship;

import java.util.HashMap;

public class Battleship extends Ship {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1446254693437636365L;

	public Battleship(int playerN) {
		super(playerN);
	}

	@Override
	public HashMap<Bomb, Integer> getMyAmmo() {
		return null;
	}

	@Override
	public int getLength() {
		return 4;
	}

	@Override
	public int getCannons() {
		return 1;
	}

}
