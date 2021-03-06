package uni.project.sd.comunications.battleship.task;

import uni.project.sd.Control.battleship.BattleshipController;
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

		try {
			if(!m.getSender().equals(ServerAddress.getInstance().getMyAddress()) && EventCounter.getInstance(null).isNewEvent(m.getMyTime()) ){
				BattleshipController.getInstance(null,0, 0).updateGrid(m.getSender(),ServerAddress.getInstance().getPlayerID(m.getReceiver()),m.getX(),m.getY());
				new BattleshipActions().resendHitResult(m);
			}
			if(m.getReceiver().equals(ServerAddress.getInstance().getMyAddress()))
				new BattleshipActions().relaseToken();
		} catch (EventNotSameException e) {
			e.printStackTrace();
		}
		return 1;
	}

}
