package uni.project.sd.comunications.battleship.task;

import uni.project.sd.Control.battleship.BattleshipController;
import uni.project.sd.comunications.battleship.entity.OceanMessage;
import uni.project.sd.comunications.entity.Message;
import uni.project.sd.comunications.task.NotifyToken;

public class UpdateOcean extends NotifyToken {

	public UpdateOcean(Message tokenInfo) {
		super(tokenInfo);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3000815411021118435L;

	@Override
	public Integer deliver() {
		OceanMessage message = (OceanMessage) m;
		BattleshipController.getInstance(null, 0, 0).updateOcean(message.getOcean());
		return super.deliver();
	}

}
