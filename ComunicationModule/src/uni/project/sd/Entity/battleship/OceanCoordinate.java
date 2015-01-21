package uni.project.sd.Entity.battleship;

import java.io.Serializable;

public class OceanCoordinate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2843159364720464510L;
	private Integer[] coordinates;

	public OceanCoordinate(int x, int y, int z) {
		coordinates = new Integer[3];
		coordinates[0] = x;
		coordinates[1] = y;
		coordinates[2] = z;
	}

	public int getX() {
		return coordinates[0];
	}

	public int getY() {
		return coordinates[1];
	}

	public int getZ() {
		return coordinates[2];
	}

	@Override
	public int hashCode() {
		return this.coordinates.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		try {
			OceanCoordinate oc = (OceanCoordinate) obj;
			if (oc.getX() == coordinates[0] && oc.getY() == coordinates[1]
					&& oc.getZ() == coordinates[2])
				result = true;
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
}
