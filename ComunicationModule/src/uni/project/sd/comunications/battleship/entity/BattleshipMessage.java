package uni.project.sd.comunications.battleship.entity;

import uni.project.sd.comunications.entity.Message;

public class BattleshipMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4027921306204030666L;
	private int x,y;
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

}
