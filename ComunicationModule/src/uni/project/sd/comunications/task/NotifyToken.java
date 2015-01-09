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
			boolean newEvent = EventCounter.getInstance(null).isNewEvent(m.getMyTime());
			DummyFrontEntity.getInstance().addMessage("Il token è in mano a "+m.getMessage());
			book.setTokenPosition(m.getMessage());
			if(!m.getSender().equals(book.getMyAddress()) && newEvent ){
				new ComunicationActions().cicleToken(m);
			} else
				DummyFrontEntity.getInstance().addMessage("Fermo token: "+m.getMessage()+" "+newEvent );
		} catch (EventNotSameException e) {
			e.printStackTrace();
		}
		if(m.getMessage().equals(book.getMyAddress())) {
			DummyFrontEntity.getInstance().setPlayerTurn(true);
		}
		return 1;
	}

}
