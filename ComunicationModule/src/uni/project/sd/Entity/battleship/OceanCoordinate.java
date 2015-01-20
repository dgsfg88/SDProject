package uni.project.sd.Entity.battleship;

import java.io.Serializable;

public class OceanCoordinate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2843159364720464510L;
	private int x, y, z;

	public OceanCoordinate(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		try {
			OceanCoordinate oc = (OceanCoordinate) obj;
			if (oc.getX() == this.x && oc.getY() == this.y
					&& oc.getZ() == this.z)
				result = true;
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
}
