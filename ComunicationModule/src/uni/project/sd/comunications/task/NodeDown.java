package uni.project.sd.comunications.task;

import uni.project.sd.Entity.DummyFrontEntity;
import uni.project.sd.comunications.ComunicationActions;
import uni.project.sd.comunications.ServerAddress;

public class NodeDown extends MessageBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3802498635866980549L;

	@Override
	public Integer deliver() {
		ServerAddress address = ServerAddress.getInstance();
		int lapCounter = address.getTokenLap();
		if (m.getMessageType() != -1) {
			int myID = address.getPlayerID(address.getMyAddress());
			int senderID = address.getPlayerID(m.getSender());
			int slpCounter = m.getMessageType();
			if ((senderID < myID && lapCounter < slpCounter)
					|| (senderID > myID && lapCounter == slpCounter)) {
				new ComunicationActions().cicleToken();
			}
			m.setMessageType(-1);
		}
		if (!m.getSender().equals(address.getMyAddress())
				&& address.getServerStatus(m.getMessage())) {
			address.setServerStatus(m.getMessage(), false);
			DummyFrontEntity.getInstance().destroyPlayer(
					address.getServerNID(m.getMessage()));
			new ComunicationActions().resendNodeDown(m);
			new ComunicationActions().requestToken();
		}
		System.out.println("Ho ricevuto il seguente morto " + m.getMessage());
		return lapCounter;
	}

}
