package uni.project.sd.comunications.task;

import uni.project.sd.Entity.DummyFrontEntity;
import uni.project.sd.comunications.ComunicationActions;
import uni.project.sd.comunications.ServerAddress;
import uni.project.sd.event.EventCounter;
import uni.project.sd.event.EventCounter.EventNotSameException;

public class NodeDown extends MessageBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3802498635866980549L;

	@Override
	public Integer deliver() {
		ServerAddress address = ServerAddress.getInstance();
		try {
			if(!m.getSender().equals(address.getMyAddress()) && EventCounter.getInstance(null).isNewEvent(m.getMyTime())){
				address.setServerStatus(m.getMessage(), false);
				DummyFrontEntity.getInstance().destroyPlayer(address.getServerNID(m.getMessage()));
				new ComunicationActions().resendNodeDown(m);
				new ComunicationActions().requestToken();
			}
		} catch (EventNotSameException e) {
			e.printStackTrace();
		}
		
		return 1;
	}

}
