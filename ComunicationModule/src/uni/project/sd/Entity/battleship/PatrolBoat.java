package uni.project.sd.Entity.battleship;

import java.util.HashMap;

public class PatrolBoat extends Ship {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1202658243010049536L;

	public PatrolBoat(int playerN) {
		super(playerN);
	}

	@Override
	public HashMap<Bomb, Integer> getMyAmmo() {
		return null;
	}

	@Override
	public int getLength() {
		return 2;
	}

	@Override
	public int getCannons() {
		return 1;
	}

}
