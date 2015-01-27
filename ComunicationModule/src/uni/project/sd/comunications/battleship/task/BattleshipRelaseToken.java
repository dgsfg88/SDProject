package uni.project.sd.comunications.battleship.task;

import uni.project.sd.Control.battleship.BattleshipController;
import uni.project.sd.comunications.ServerAddress;
import uni.project.sd.comunications.battleship.entity.BattleshipToken;
import uni.project.sd.comunications.entity.Message;
import uni.project.sd.comunications.task.NotifyToken;

public class BattleshipRelaseToken extends NotifyToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = -354895248081562980L;

	public BattleshipRelaseToken(Message tokenInfo) {
		super(tokenInfo);
	}

	@Override
	public Integer deliver() {
		BattleshipToken message = (BattleshipToken)this.m;
		ServerAddress address = ServerAddress.getInstance();
		
		if(message.getMessage().equals(address.getMyAddress())) {
			BattleshipController.getInstance(null, 0, 0).setEventList(message.getEventList());
		}
		
		return super.deliver();
	}
}
