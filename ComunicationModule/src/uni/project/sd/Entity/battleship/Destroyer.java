package uni.project.sd.Entity.battleship;

import java.util.HashMap;

public class Destroyer extends Ship {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -926275168485877117L;

	public Destroyer(int playerN) {
		super(playerN);
	}

	@Override
	public HashMap<Bomb, Integer> getMyAmmo() {
		return null;
	}

	@Override
	public int getLength() {
		return 3;
	}

	@Override
	public int getCannons() {
		return 1;
	}

}
