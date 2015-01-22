package uni.project.sd.comunications.battleship;

import uni.project.sd.Entity.battleship.Ocean;
import uni.project.sd.comunications.ComunicationActions;
import uni.project.sd.comunications.ServerAddress;
import uni.project.sd.comunications.battleship.entity.BattleshipMessage;
import uni.project.sd.comunications.battleship.entity.OceanMessage;

public class BattleshipActions extends ComunicationActions {
	
	public void sendHit(int player, int x, int y) {
		this.m = new BattleshipMessage();
		String s = ServerAddress.getInstance().getServer(player);
		this.m.setMessage("hit");
		this.m.setSender(ServerAddress.getInstance().getMyAddress());
		this.m.setReceiver(s);
		((BattleshipMessage)this.m).setX(x);
		((BattleshipMessage)this.m).setY(y);
		resendHit((BattleshipMessage) m);
	}

	public void resendHit(BattleshipMessage m) {
		this.m = m;
		setOutcomingClient(new BattleshipClient(BattleshipClient.sendHit, this.m));
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
		setOutcomingClient(new BattleshipClient(BattleshipClient.sendHitResult, this.m));
		sendMessage(BattleshipClient.sendHitResult);
	}
	
	public void sendOcean(Ocean myOcean){
		this.m = new OceanMessage();
		((OceanMessage)this.m).setOcean(myOcean);
		sendMessage(BattleshipClient.sendOcean);
	}
}
