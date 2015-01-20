package uni.project.sd.Entity.battleship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Ocean {
	public static final short splitted = 1;
	public static final short shared = 2;
	//TODO team play (we are so crazy)
	public static final short team = 3; 
	private short mode = splitted;
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
	
	public short checkShot(OceanCoordinate position){
		Ship s;
		if ((s = this.positions.remove(position)) != null){
			this.hit.put(s,position);
			return s.setSunk();
		} else {
			this.missed.add(position);
			return -1;
		}
	}
	
	public boolean deployShip(List<OceanCoordinate> coordinates, Ship ship, int playerN){
		boolean result = false;
		//TODO deploy della nave
		switch (this.mode) {
		case shared:
			result = true;
			break;
		case team:
			
			break;
		default:
			
			break;
		}
		
		return result;
	}
	
}
