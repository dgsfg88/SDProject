package uni.project.sd.comunications.task;

import uni.project.sd.Entity.DummyFrontEntity;
import uni.project.sd.comunications.ComunicationActions;
import uni.project.sd.comunications.ServerAddress;
import uni.project.sd.comunications.entity.Message;
import uni.project.sd.event.EventCounter;
import uni.project.sd.event.EventCounter.EventNotSameException;

public class NotifyToken extends MessageBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2839149545156931567L;

	public NotifyToken(Message tokenInfo) {
		setMessage(tokenInfo);
	}
	
	@Override
	public Integer deliver() {
		ServerAddress book = ServerAddress.getInstance();
		try {
			if(!m.getSender().equals(book.getMyAddress()) && EventCounter.getInstance(null).isNewEvent(m.getMyTime())){
				DummyFrontEntity.getInstance().addMessage("Il token è in mano a "+m.getMessage());
				new ComunicationActions().cicleToken(m);
				book.setTokenPosition(m.getMessage());
			}
		} catch (EventNotSameException e) {
			e.printStackTrace();
		}
		if(m.getMessage().equals(book.getMyAddress())) {
			DummyFrontEntity.getInstance().setPlayerTurn(true);
		}
		return 1;
	}


}
