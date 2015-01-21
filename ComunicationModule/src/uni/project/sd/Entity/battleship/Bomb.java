package uni.project.sd.Entity.battleship;

import java.io.Serializable;
import java.util.LinkedList;

public interface Bomb extends Serializable {
	public abstract LinkedList<OceanCoordinate> getExplosionRange(OceanCoordinate center);
	
	public abstract int getRadius();
}
