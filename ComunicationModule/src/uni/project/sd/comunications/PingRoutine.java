package uni.project.sd.comunications;

import uni.project.sd.Control.battleship.BattleshipController;
import uni.project.sd.Entity.DummyFrontEntity;

public class PingRoutine implements Runnable {

	private final static int sleepTime = 1000;
	
	private ServerAddress address;
	
	@Override
	public void run() {
		address = ServerAddress.getInstance();
		
		waitPlayer();
		
		DummyFrontEntity.getInstance().addMessage(
				"All online, starting game");

		boolean imfirst = address.getPlayerID(address.getMyAddress()) == 0;
		if (imfirst) {
			new ComunicationActions().cicleToken();
		}
		
		imfirst = true;
		BattleshipController.getInstance(null, 0, 0).gameReady();
		while (imfirst) {
			try {
				String k = address.getNextOnline();

				Integer result = sendPingToNode(k);
				if (result == 0) {
					new ComunicationActions().nodeDown(k);
					address.setServerStatus(k, false);
					DummyFrontEntity.getInstance().addMessage(
							"Node " + k
									+ " is down, token position: "
									+ address.getTokenPosition());
					DummyFrontEntity.getInstance().destroyPlayer(
							address.getServerNID(k));
				}
			} catch (Exception e) {
				DummyFrontEntity.getInstance().addMessage(
						"I'm only online, I win");
				imfirst = false;
			}
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	
	private void waitPlayer () {
		boolean ready = false;
		while (!ready) {
			Integer result = 0;
			for (int k = 0; k < address.serverNumber(); k++) {
				result += sendPingToNode(address.getServer(k));
			}
			if (result == address.serverNumber())
				ready = true;
			else {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private int sendPingToNode(String node) {
		OutcomingClient client = new OutcomingClient(
				OutcomingClient.sendPing);
		client.doCustomRmiHandling(node);
		return client.getResult();
	}

}
