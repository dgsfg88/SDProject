package uni.project.sd.Entity.battleship;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Ocean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8419489917076487169L;
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
	private HashMap<OceanCoordinate, HashMap<Integer, Ship>> positions;
	private ArrayList<OceanCoordinate> missed;
	private LinkedHashMap<Ship, LinkedList<OceanCoordinate>> hit;

	public Ocean(short mode, int d, int playersNumber) {
		this.positions = new HashMap<>();
		this.hit = new LinkedHashMap<>();
		this.missed = new ArrayList<>();
		this.mode = mode;
		this.d = d;
		this.playersNumber = playersNumber;
	}

	public void updateOcean(Ocean newOcean){
		synchronized (positions) {
			if (newOcean != null){
				Set<OceanCoordinate> myCoordinates = newOcean.positions.keySet();
				for (OceanCoordinate k: myCoordinates){
					HashMap<Integer, Ship> foreignCoordinate = newOcean.positions.get(k);
					if(foreignCoordinate != null){
						HashMap<Integer, Ship> localCoordinate = this.positions.get(k);
						if(localCoordinate == null){
							this.positions.put(k, foreignCoordinate);
						} else {
							//XXX ricordati che qui se fai il put di un null ti infila il tuo null su quello che invece è tuo
							this.positions.get(k).putAll(foreignCoordinate);
						}
					}
				}
			}	
		}
	}
	/**
	 * This method check if the shot of a player, identified with his number,
	 * hit or missed one or more (shared mode) enemy.
	 * 
	 * @param position
	 *            the coordinates of the shot
	 * @param playerN
	 *            number of the player
	 * @return return an HashMap empty if there were no hit or a set of Ships
	 *         with their sunk result.
	 */
	public HashMap<Ship, Integer> checkShot(OceanCoordinate position,
			int playerN) {
		synchronized (positions) {
			HashMap<Ship, Integer> result = new HashMap<>();
			HashMap<Integer, Ship> ships = this.positions.get(position);
			if (ships != null) {
				Set<Integer> playersShips = this.positions.get(position).keySet();
				playersShips.remove(playerN);
				for (Integer i : playersShips) {
					Ship s = this.positions.get(position).remove(i);
					if (this.hit.get(s) == null) {
						this.hit.put(s, new LinkedList<OceanCoordinate>());
					}
					this.hit.get(s).add(position);
					result.put(s, s.setSunk());
				}
			}
			if (result.isEmpty()) {
				this.missed.add(position);
			}
			return result;
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

	public List<OceanCoordinate> getMissed(int from) {
		if (from < 0 || from >= missed.size() - 1) {
			return null;
		}
		return missed.subList(from, missed.size() - 1);
	}

	public LinkedHashMap<Ship, LinkedList<OceanCoordinate>> getHit(int from) {
		return hit;
	}

	private boolean deployShipSplitted(List<OceanCoordinate> coordinates,
			Ship ship, int playerN) {
		synchronized (positions) {
		try {
			HashMap<Integer, Ship> ships;
			OceanCoordinate oc = coordinates.remove(0);
			if (isSplittedPositionValid(oc, playerN)) {
				if (coordinates.size() == 0) {
					ships = new HashMap<>();
					ships.put(playerN, ship);
					this.positions.put(oc, ships);
					return true;
				} else {
					if (deployShipSplitted(coordinates, ship, playerN)) {
						ships = new HashMap<>();
						ships.put(playerN, ship);
						this.positions.put(oc, ships);
						return true;
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {

		}
		return false;
		}
	}

	// TODO ottimizzato per le due dimensioni, la terza si pu� aggiungere allo
	// stesso modo
	private boolean isSplittedPositionValid(OceanCoordinate oc, int playerN) {
		if (playerN < this.playersNumber) {
			int playerLimitInf = playerN * d * d; // (P*D) + (P*D+D-1)
			int playerLimitSup = playerLimitInf + d * d - 1;
			int wantedPosition = oc.getX() + oc.getY() * d;
			synchronized (positions) {
			if (wantedPosition >= playerLimitInf
					&& wantedPosition <= playerLimitSup
					&& !this.positions.containsKey(oc)) {
				return true;
			}
			}
		}
		return false;
	}

	private boolean deployShipShared(List<OceanCoordinate> coordinates,
			Ship ship, int playerN) {
		synchronized (positions) {
		OceanCoordinate oc = coordinates.remove(0);
		if (coordinates.size() == 0) {
			if (isSharedPositionValid(oc, playerN)) {
				if (this.positions.get(oc) == null) {
					this.positions.put(oc, new HashMap<Integer, Ship>());
				}
				this.positions.get(oc).put(playerN,ship);
				return true;
			}
		} else {
			if (isSharedPositionValid(oc, playerN)) {
				if (deployShipShared(coordinates, ship, playerN)) {
					if (this.positions.get(oc) == null) {
						this.positions.put(oc, new HashMap<Integer, Ship>());
					}
					this.positions.get(oc).put(playerN, ship);
					return true;
				}
			}
		}
		return false;
		}
	}

	private boolean isSharedPositionValid(OceanCoordinate oc, int playerN) {
		if (oc.getX() < this.d && oc.getY() < this.d && oc.getZ() < this.d) {
			HashMap<Integer, Ship> ships;
			synchronized (positions) {
				ships = this.positions.get(oc);
			}
			if (ships != null) {
				return !ships.containsKey(playerN);
			}
			return true;
		}
		return false;
	}
}
