package uni.project.sd.comunications.battleship.task;

import uni.project.sd.Control.BattleshipController;
import uni.project.sd.Entity.DummyFrontEntity;
import uni.project.sd.comunications.ServerAddress;
import uni.project.sd.comunications.battleship.BattleshipActions;
import uni.project.sd.comunications.battleship.entity.BattleshipMessage;
import uni.project.sd.comunications.task.MessageBase;
import uni.project.sd.event.EventCounter;
import uni.project.sd.event.EventCounter.EventNotSameException;

public class ExecuteHit extends MessageBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2200087886870480093L;

	@Override
	public Integer deliver() {
		BattleshipMessage m = (BattleshipMessage)this.getMessage();
		BattleshipActions actions = new BattleshipActions();
		if(m.getReceiver().equals(ServerAddress.getInstance().getMyAddress())){
			boolean result = BattleshipController.getInstance(null, 0).checkHit(m.getX(), m.getY());
			actions.sendHitResult(m.getX(),m.getY(),result,m.getSender());
		} else {
			try {
				if(m.getSender().equals(ServerAddress.getInstance().getMyAddress())) {
					ServerAddress.getInstance().setServerStatus(m.getReceiver(), false);
					DummyFrontEntity.getInstance().destroyPlayer(ServerAddress.getInstance().getServerNID(m.getReceiver()));
					actions.nodeDown(m.getReceiver());
					actions.cicleToken();
				}
				else if(EventCounter.getInstance(null).isNewEvent(m.getMyTime()))
					actions.resendHit(m);
			} catch (EventNotSameException e) {
				e.printStackTrace();
			}
		}
		
		return 1;
	}

}
