package uni.project.sd.comunications.battleship.entity;

import java.io.Serializable;

public class EventListItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9033618485993617413L;

	private String breeder;
	private String receiver;
	private int x,y;
	
	public EventListItem(String breeder, String receiver, int x, int y) {
		super();
		this.breeder = breeder;
		this.receiver = receiver;
		this.x = x;
		this.y = y;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getBreeder() {
		return breeder;
	}

	public String getReceiver() {
		return receiver;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "EventListItem [breeder=" + breeder + ", receiver=" + receiver
				+ ", x=" + x + ", y=" + y + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((receiver == null) ? 0 : receiver.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof EventListItem))
			return false;
		EventListItem other = (EventListItem) obj;
		if (receiver == null) {
			if (other.receiver != null)
				return false;
		} else if (!receiver.equals(other.receiver))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	
		
	
}
