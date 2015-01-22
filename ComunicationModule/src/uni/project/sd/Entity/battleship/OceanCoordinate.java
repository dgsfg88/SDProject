package uni.project.sd.Entity.battleship;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

	public Integer getX() {
		return coordinates[0];
	}

	public Integer getY() {
		return coordinates[1];
	}

	public Integer getZ() {
		return coordinates[2];
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(coordinates).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof OceanCoordinate))
			return false;
		if(obj == this)
			return true;
		
		OceanCoordinate oc = (OceanCoordinate)obj;
		return new EqualsBuilder()
			.append(coordinates, oc.coordinates).isEquals();
	}
}
