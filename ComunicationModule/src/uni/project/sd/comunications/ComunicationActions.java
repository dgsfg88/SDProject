package uni.project.sd.comunications;

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
				while(result == 0) {
					client.doCustomRmiHandling(ServerAddress.getInstance().getNextOnline());
					result = client.getResult();
					if(result == 0)
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
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
