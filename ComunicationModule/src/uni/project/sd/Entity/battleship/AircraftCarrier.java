package uni.project.sd.Entity.battleship;

import java.util.HashMap;

public class AircraftCarrier extends Ship {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5707460845034418765L;

	public AircraftCarrier(int playerN) {
		super(playerN);
	}

	@Override
	public HashMap<Bomb, Integer> getMyAmmo() {
		return null;
	}

	@Override
	public int getLength() {
		return 5;
	}

	@Override
	public int getCannons() {
		return 1;
	}

}
