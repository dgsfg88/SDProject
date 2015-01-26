package uni.project.sd.comunications.battleship.task;

import uni.project.sd.Control.battleship.BattleshipController;
import uni.project.sd.comunications.ServerAddress;
import uni.project.sd.comunications.battleship.BattleshipActions;
import uni.project.sd.comunications.battleship.entity.BattleshipMessage;
import uni.project.sd.comunications.task.MessageBase;
import uni.project.sd.event.EventCounter;

public class ExecuteHit extends MessageBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2200087886870480093L;

	@Override
	public Integer deliver() {
		BattleshipMessage m = (BattleshipMessage) this.getMessage();
		BattleshipActions actions = new BattleshipActions();
		if (m.getReceiver().equals(ServerAddress.getInstance().getMyAddress())) {
			// BattleshipController.getInstance(null, 0, 0)
			// .checkHit(m.getX(), m.getY());
			// XXX Rimuovere l'istruzione successiva perchÃ© adesso bisogna fare
			// il solo controllo in locale
			// actions.sendHitResult(m.getX(), m.getY(), result, m.getSender());
			m.setMessage(m.getSender());
		}
		try {
			String myName = ServerAddress.getInstance().getMyAddress();
			if (m.getSender().equals(myName)) {
				if (!m.getMessage().equals(myName)) {
					// TODO caso in cui il messaggio torna senza che il player a
					// cui è destinato lo veda, cioè è offline, avviare
					// un'azione adeguata

					// ServerAddress.getInstance().setServerStatus(m.getReceiver(),
					// false);
					// DummyFrontEntity.getInstance().destroyPlayer(
					// ServerAddress.getInstance().getServerNID(
					// m.getReceiver()));
					// actions.nodeDown(m.getReceiver());
					// actions.cicleToken();
				}
			} else if (EventCounter.getInstance(null).isNewEvent(m.getMyTime())) {
				// se si tratta di un nuovo evento, REAL TIME
				BattleshipController.getInstance(null, 0, 0).updateGrid(
						m.getReceiver(), ServerAddress.getInstance().getPlayerID(m.getSender()), m.getX(), m.getY());
				actions.resendHit(m);
			} else {
				// TODO ho già visto il messaggio, il mittente è offline,
				// avviare un'azione adeguata
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

}
