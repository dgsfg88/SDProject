package uni.project.sd.comunications.battleship;

import java.util.ArrayList;

import uni.project.sd.Entity.battleship.Ocean;
import uni.project.sd.comunications.ComunicationActions;
import uni.project.sd.comunications.ServerAddress;
import uni.project.sd.comunications.battleship.entity.BattleshipMessage;
import uni.project.sd.comunications.battleship.entity.BattleshipToken;
import uni.project.sd.comunications.battleship.entity.EventListItem;
import uni.project.sd.comunications.battleship.entity.OceanMessage;

public class BattleshipActions extends ComunicationActions {
	
	public void sendHit(int player, int x, int y) {
		this.m = new BattleshipMessage();
		String s = ServerAddress.getInstance().getServer(player);
		this.m.setMessage(s);
		this.m.setSender(ServerAddress.getInstance().getMyAddress());
		this.m.setReceiver(s);
		((BattleshipMessage)this.m).setX(x);
		((BattleshipMessage)this.m).setY(y);
		resendHit((BattleshipMessage) m);
	}

	public void resendHit(BattleshipMessage m) {
		this.m = m;
		sendMessage(BattleshipClient.sendHit);
	}

	public void sendHitResult(int x, int y, boolean result,String receiver) {
		this.m = new BattleshipMessage();
		this.m.setMessage(Boolean.toString(result));
		this.m.setSender(ServerAddress.getInstance().getMyAddress());
		this.m.setReceiver(receiver);
		((BattleshipMessage)this.m).setX(x);
		((BattleshipMessage)this.m).setY(y);
		resendHitResult((BattleshipMessage)m);
	}

	public void resendHitResult(BattleshipMessage m) {
		this.m = m;
		sendMessage(BattleshipClient.sendHitResult);
	}
	
	public void sendOcean(Ocean myOcean){
		this.m = new OceanMessage();
		this.m.setMessage(ServerAddress.getInstance().getNextOnline());
		this.m.setSender(ServerAddress.getInstance().getMyAddress());
		this.m.setReceiver(this.m.getSender());
		((OceanMessage)this.m).setOcean(myOcean);
		sendMessage(BattleshipClient.sendOcean);
	}
	
	public void relaseToken(ArrayList<EventListItem> eventList) {
		this.m = new BattleshipToken();
		this.m.setMessage(ServerAddress.getInstance().getNextOnline());
		this.m.setSender(ServerAddress.getInstance().getMyAddress());
		this.m.setReceiver(this.m.getSender());
		((BattleshipToken)this.m).setEventList(eventList);
		setOutcomingClient(new BattleshipClient(BattleshipClient.notifyToken,this.m));
		sendMessage(BattleshipClient.notifyToken);
	}
	
	@Override
	protected void sendMessage(int messageType) {
		setOutcomingClient(new BattleshipClient(messageType, this.m));
		super.sendMessage(messageType);
	}
}
