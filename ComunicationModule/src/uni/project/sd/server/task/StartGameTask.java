package uni.project.sd.server.task;

import java.util.ArrayList;

import saqib.rasul.Task;
import uni.project.sd.comunications.ServerAddress;
import uni.project.sd.comunications.entity.Message;
import uni.project.sd.comunications.task.MessageBase;
import uni.project.sd.server.entity.Player;
import uni.project.sd.server.entity.StartGameMessage;

public class StartGameTask extends MessageBase implements Task<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5378422556616697690L;

	public StartGameTask(Message message) {
		this.m = message;
	}

	@Override
	public Integer deliver() {
		String myName = ServerAddress.getInstance().getMyAddress();
		ServerAddress book = ServerAddress.getNewInstance();
		StartGameMessage message = (StartGameMessage)this.m;
		
		ArrayList<Player> players = message.getPlayers();
		for(Player p: players){
			if(p.getName().equals(myName))
				book.setMyAddress(myName);
			else
				book.addServer(p.getName(), p.getIp(), p.getPort());
		}
		
		new Thread(new StartGameThread(message.getShipNumber())).start();;
		
		return 1;
	}

}
