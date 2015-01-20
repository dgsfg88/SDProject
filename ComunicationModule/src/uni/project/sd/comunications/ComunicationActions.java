package uni.project.sd.comunications;

import uni.project.sd.Entity.DummyFrontEntity;
import uni.project.sd.comunications.entity.Message;

public class ComunicationActions {
	protected Message m;
	private OutcomingClient client = null;
	
	public void cicleToken() {
		m = new Message();
		m.setSender(ServerAddress.getInstance().getMyAddress());
		m.setMessage(ServerAddress.getInstance().getMyAddress());
		m.setReceiver(ServerAddress.getInstance().getMyAddress());
		sendMessage(OutcomingClient.notifyToken);
	}
	
	public void nodeDown(String node) {
		m = new Message();
		m.setSender(ServerAddress.getInstance().getMyAddress());
		m.setReceiver(ServerAddress.getInstance().getMyAddress());
		m.setMessage(node);
		sendMessage(OutcomingClient.nodeDown);
		try {
			if(node.equals(ServerAddress.getInstance().getTokenPosition())) {
				DummyFrontEntity.getInstance().addMessage("\n\nTOKEN PERSO\n\n");
				requestToken();
			}
		} catch (Exception e){
			//TODO Non sono riuscito a capire cosa causa l'exception, ma il processo che riceverà il token inizia a farla
			DummyFrontEntity.getInstance().addMessage("Possibile token perso");
			requestToken();
		}
	}
	public void resendNodeDown(Message m) {
		this.m = m;
		sendMessage(OutcomingClient.nodeDown);
	}
	
	public void requestToken() {
		m = new Message();
		m.setSender(ServerAddress.getInstance().getMyAddress());
		m.setMessage(ServerAddress.getInstance().getMyAddress());
		sendMessage(OutcomingClient.requestToken);
	}
	
	public void resubmitTokenRequest(Message m) {
		this.m = m;
		sendMessage(OutcomingClient.requestToken);
	}
	
	protected void setOutcomingClient(OutcomingClient client) {
		this.client = client;
	}
	
	protected OutcomingClient getClient() {
		return client;
	}
	
	protected void sendMessage(final int messageType) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Integer result = 0;
				if(client == null)
					client = new OutcomingClient(messageType,m);
				else {
					client.setMessageAndType(m,messageType);
				}
				while(result == 0) {
					try{
						String next = ServerAddress.getInstance().getNextOnline();
						client.doCustomRmiHandling(next);
						result = client.getResult();
						if(result == 0) {
							nodeDown(next);
							ServerAddress.getInstance().setServerStatus(next, false);
						}
					} catch (IndexOutOfBoundsException e){
						//TODO routine di terminazione
						System.out.println("Ho vinto!");
						result = 1;
					}
						
				}
			}
		}).start();
	}

	public void cicleToken(Message token) {
		this.m = token;
		sendMessage(OutcomingClient.notifyToken);
	}

	public void relaseToken() {
		m = new Message();
		m.setSender(ServerAddress.getInstance().getMyAddress());
		m.setMessage(ServerAddress.getInstance().getNextOnline());
		m.setReceiver(ServerAddress.getInstance().getMyAddress());
		sendMessage(OutcomingClient.notifyToken);
	}
}
