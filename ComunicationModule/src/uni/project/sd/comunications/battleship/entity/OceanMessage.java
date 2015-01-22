package uni.project.sd.comunications.battleship.entity;

import uni.project.sd.Entity.battleship.Ocean;
import uni.project.sd.comunications.entity.Message;

public class OceanMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8091400117126593705L;
	private Ocean ocean;
	public Ocean getOcean() {
		return ocean;
	}
	public void setOcean(Ocean ocean) {
		this.ocean = ocean;
	}	
}
