package uni.project.sd.comunications.task;

import java.io.Serializable;

import saqib.rasul.Task;
import uni.project.sd.Entity.DummyFrontEntity;
import uni.project.sd.comunications.DeliverMessage;
import uni.project.sd.comunications.OutcomingClient;
import uni.project.sd.comunications.ServerAddress;
import uni.project.sd.comunications.entity.Message;

public class NotifyToken implements Serializable, DeliverMessage, Task<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2839149545156931567L;

	private Message m;
	public NotifyToken(Message tokenInfo) {
		this.m = tokenInfo;
	}
	
	@Override
	public Integer deliver() {
		ServerAddress book = ServerAddress.getInstance();
		//TODO fare in modo che il messaggio non vada in loop se il processo che lo ha inviato cade
		if(!m.getSender().equals(book.getMyAddress())){
			//TODO azione da intraprendere quando il token cambia
			DummyFrontEntity.getInstance().addMessage("Il token è in mano a "+m.getMessage());
			book.setTokenPosition(m.getMessage());
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					Integer result = 0;
					while(result == 0) {
						OutcomingClient client = new OutcomingClient(ServerAddress.getInstance().getNextOnline(),OutcomingClient.notifyToken,m);
						result = client.getResult();
						if(result == 0)
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Aggiungere controlli di terminazione
								e.printStackTrace();
							}
					}
				}
			}).start();
		}
		return 1;
	}

	@Override
	public void setMessage(Message tokenInfo) {
		this.m = tokenInfo;
	}

	@Override
	public Message getMessage() {
		return this.m;
	}

	@Override
	public Integer execute() {
		return deliver();
	}

	

}
