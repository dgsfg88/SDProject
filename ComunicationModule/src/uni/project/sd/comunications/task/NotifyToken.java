package uni.project.sd.comunications.task;

import uni.project.sd.Entity.DummyFrontEntity;
import uni.project.sd.comunications.ComunicationActions;
import uni.project.sd.comunications.ServerAddress;
import uni.project.sd.comunications.entity.Message;

public class NotifyToken extends MessageBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2839149545156931567L;

	public NotifyToken(Message tokenInfo) {
		this.m = tokenInfo;
	}
	
	@Override
	public Integer deliver() {
		ServerAddress book = ServerAddress.getInstance();
		//TODO fare in modo che il messaggio non vada in loop se il processo che lo ha inviato cade
		if(!m.getSender().equals(book.getMyAddress())){
			DummyFrontEntity.getInstance().addMessage("Il token è in mano a "+m.getMessage());
			new ComunicationActions().cicleToken(m);
			book.setTokenPosition(m.getMessage());
		}
		if(m.getMessage().equals(book.getMyAddress())) {
			DummyFrontEntity.getInstance().setPlayerTurn(true);
		}
		return 1;
	}


}
