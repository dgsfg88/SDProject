package uni.project.sd.server.control;

import uni.project.sd.comunications.OutcomingClient;
import uni.project.sd.server.task.StartGameTask;

public class ServerOutClass extends OutcomingClient {
	
	@Override
	public void doCustomRmiHandling(String serverID) {
		doTask(serverID, new StartGameTask(this.getM()));
	}
}
