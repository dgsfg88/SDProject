package uni.project.sd.server.control;

import uni.project.sd.comunications.OutcomingClient;
import uni.project.sd.comunications.ServerAddress;

public class BackgroudServerThread implements Runnable {

	private ServerAddress address;
	private ServerController controller;
	
	public BackgroudServerThread() {
		address = ServerAddress.getInstance();
		controller = ServerController.getInstance();
	}
	
	@Override
	public void run() {
		while(true) {
			int clientNumber = address.serverNumber();
			for(int k = 0; k < clientNumber; k++) {
				String name = address.getServer(k);
				OutcomingClient client = new OutcomingClient(
						OutcomingClient.sendPing);
				client.doCustomRmiHandling(name);
				if(client.getResult() == 0) {
					address.setServerStatus(name, false);
					controller.removePlayer(name, address.getLocation(name), address.getServerPort(name));
				} else {
					if(!address.getServerStatus(name)) {
						address.setServerStatus(address.getServer(k), true);
						controller.addPlayer(name, address.getLocation(name), address.getServerPort(name));
					}
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
