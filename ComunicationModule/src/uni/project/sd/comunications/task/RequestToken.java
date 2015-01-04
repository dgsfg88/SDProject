package uni.project.sd.comunications.task;

import uni.project.sd.Entity.DummyFrontEntity;
import uni.project.sd.comunications.ComunicationActions;
import uni.project.sd.comunications.ServerAddress;

public class RequestToken extends MessageBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5991091440213820932L;

	@Override
	public Integer deliver() {
		if(m.getMessage().equals(ServerAddress.getInstance().getMyAddress())){
			//Caso in cui il messaggio ha fatto tutto il giro e ho vinto io
			DummyFrontEntity.getInstance().setPlayerTurn(true);
			ServerAddress.getInstance().getMyAddress();
			m.setSender(ServerAddress.getInstance().getMyAddress());
			new ComunicationActions().cicleToken(m);
		} else {
			
		}
		return 1;
	}


}
