package uni.project.sd.comunications.battleship.task;

import uni.project.sd.Control.BattleshipController;
import uni.project.sd.comunications.ServerAddress;
import uni.project.sd.comunications.battleship.BattleshipActions;
import uni.project.sd.comunications.battleship.entity.BattleshipMessage;
import uni.project.sd.comunications.task.MessageBase;
import uni.project.sd.event.EventCounter;
import uni.project.sd.event.EventCounter.EventNotSameException;

public class ExecuteHitResult extends MessageBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1777894874399388420L;

	@Override
	public Integer deliver() {
		BattleshipMessage m = (BattleshipMessage)this.getMessage();
		
		if(m.getReceiver().equals(ServerAddress.getInstance().getMyAddress()))
			new BattleshipActions().relaseToken();
		
		try {
			if(!m.getSender().equals(ServerAddress.getInstance().getMyAddress()) && EventCounter.getInstance(null).isNewEvent(m.getMyTime()) ){
				BattleshipController.getInstance(null, 0).updateGrid(ServerAddress.getInstance().getServerNID(m.getSender()),m.getX(),m.getY(),Boolean.parseBoolean(m.getMessage()));
				new BattleshipActions().resendHitResult(m);
			}
		} catch (EventNotSameException e) {
			e.printStackTrace();
		}
		return 1;
	}

}
