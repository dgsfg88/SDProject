package uni.project.sd.Entity.battleship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Ocean {
	/**
	 * Splitted ocean
	 */
	public static final short splitted = 1;
	/**
	 * All in the same ocean
	 */
	public static final short shared = 2;
	// TODO team play (we are so crazy)
	/**
	 * Team battleship, team share ocean
	 */
	public static final short team = 3;
	private short mode;
	private int d;
	private int playersNumber;
	private HashMap<OceanCoordinate, Ship> positions;
	private ArrayList<OceanCoordinate> missed;
	private LinkedHashMap<Ship, OceanCoordinate> hit;

	public Ocean(short mode, int d, int playersNumber) {
		this.positions = new HashMap<>();
		this.hit = new LinkedHashMap<>();
		this.missed = new ArrayList<>();
		this.mode = mode;
		this.d = d;
		this.playersNumber = playersNumber;
	}

	public short checkShot(OceanCoordinate position) {
		Ship s;
		if ((s = this.positions.remove(position)) != null) {
			this.hit.put(s, position);
			return s.setSunk();
		} else {
			this.missed.add(position);
			return -1;
		}
	}

	public boolean deployShip(List<OceanCoordinate> coordinates, Ship ship,
			int playerN) {
		boolean result = false;
		switch (this.mode) {
		case shared:
			result = deployShipShared(coordinates, ship, playerN);
			break;
		case team:
			// TODO team deploy
			break;
		default:
			result = deployShipSplitted(coordinates, ship, playerN);
			break;
		}

		return result;
	}

	private boolean deployShipSplitted(List<OceanCoordinate> coordinates,
			Ship ship, int playerN) {
		OceanCoordinate oc = coordinates.remove(0);
		if (isSplittedPositionValid(oc, playerN)) {
			if (coordinates.size() == 0) {
				this.positions.put(oc, ship);
				return true;
			} else {
				if (deployShipSplitted(coordinates, ship, playerN)) {
					this.positions.put(oc, ship);
					return true;
				}
			}
		}
		return false;
	}

	// TODO ottimizzato per le due dimensioni, la terza si può aggiungere allo
	// stesso modo
	private boolean isSplittedPositionValid(OceanCoordinate oc, int playerN) {
		if (playerN < this.playersNumber) {
			int playerLimitInf = playerN * d; // (P*D) + (P*D+D-1)
			int playerLimitSup = playerLimitInf + d - 1;
			int wantedPosition = oc.getY() + oc.getX() * d;
			if (wantedPosition >= playerLimitInf
					&& wantedPosition <= playerLimitSup
					&& !this.positions.containsKey(oc)) {
				return true;
			}
		}
		return false;
	}

	private boolean deployShipShared(List<OceanCoordinate> coordinates,
			Ship ship, int playerN) {
		OceanCoordinate oc = coordinates.remove(0);
		if (coordinates.size() == 0) {
			if (isSharedPositionValid(oc, playerN)) {
				// TODO deploy shared ship!!
				return true;
			}
		} else {
			if (isSharedPositionValid(oc, playerN)) {
				if (deployShipShared(coordinates, ship, playerN)) {
					// TODO deploy shared ship!!!
					return true;
				}
			}
		}
		return false;
	}

	// TODO da rivedere per il fatto che possono starci più navi nello stesso
	// punto!!
	private boolean isSharedPositionValid(OceanCoordinate oc, int playerN) {
		if (oc.getX() < this.d && oc.getY() < this.d && oc.getZ() < this.d) {
			Ship s;
			if ((s = this.positions.get(oc)) != null)
				if (s.getPlayer() != playerN)
					return true;
				else
					return true;
		}
		return false;
	}
}
