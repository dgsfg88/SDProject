package uni.project.sd.Entity.battleship;

import java.io.Serializable;

public class OceanCoordinate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2843159364720464510L;
	private Integer X, Y, Z;

	public OceanCoordinate(int x, int y, int z) {
		this.X = x;
		this.Y = y;
		this.Z = z;
	}

	public Integer getX() {
		return X;
	}

	public Integer getY() {
		return Y;
	}

	public Integer getZ() {
		return Z;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((X == null) ? 0 : X.hashCode());
		result = prime * result + ((Y == null) ? 0 : Y.hashCode());
		result = prime * result + ((Z == null) ? 0 : Z.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof OceanCoordinate))
			return false;
		OceanCoordinate other = (OceanCoordinate) obj;
		if (X == null) {
			if (other.X != null)
				return false;
		} else if (!X.equals(other.X))
			return false;
		if (Y == null) {
			if (other.Y != null)
				return false;
		} else if (!Y.equals(other.Y))
			return false;
		if (Z == null) {
			if (other.Z != null)
				return false;
		} else if (!Z.equals(other.Z))
			return false;
		return true;
	}

	
}
