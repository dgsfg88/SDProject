package uni.project.sd.server.task;

import uni.project.sd.comunications.ServerAddress;
import uni.project.sd.comunications.task.MessageBase;
import uni.project.sd.server.control.ServerController;
import uni.project.sd.server.entity.RegisterMessage;

public class RegisterPlayerTask extends MessageBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1722917838326074607L;

	@Override
	public Integer deliver() {
		RegisterMessage message = (RegisterMessage)this.m;
		
		ServerAddress.getInstance().addServer(message.getMessage(), message.getIp(), message.getPort());
		ServerController.getInstance().addPlayer(message.getName(), message.getIp(), message.getPort());
		
		return 1;
	}

}
