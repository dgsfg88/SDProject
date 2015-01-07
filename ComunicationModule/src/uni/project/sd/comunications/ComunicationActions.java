package uni.project.sd.comunications;

import uni.project.sd.comunications.entity.Message;

public class ComunicationActions {
	private Message m;
	public void cicleToken(Message token) {
		m = token;
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
	
	private void sendMessage(final int messageType) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Integer result = 0;
				while(result == 0) {
					OutcomingClient client = new OutcomingClient(ServerAddress.getInstance().getNextOnline(),messageType,m);

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
}
