package uni.project.sd.comunications.task;

import uni.project.sd.comunications.ComunicationActions;
import uni.project.sd.comunications.ServerAddress;

public class RequestToken extends MessageBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5991091440213820932L;

	@Override
	public Integer deliver() {
		ComunicationActions ca = new ComunicationActions();
		ServerAddress servers = ServerAddress.getInstance();
		if(!servers.getTokenPosition().equals(servers.getMyAddress())) {
			if(m.getMessage().equals(servers.getMyAddress())){
				//Caso in cui il messaggio ha fatto tutto il giro e il server ha vinto
				m.setSender(servers.getMyAddress());
				m.setMessage(servers.getMyAddress());
				servers.setTokenPosition(servers.getMyAddress());
				ca.cicleToken(m);
			} else 
				if(servers.getServerStatus(m.getMessage())) {
					int me = Integer.parseInt(servers.getMyAddress());
					int tp = Integer.parseInt(servers.getTokenPosition());
					int d = Integer.parseInt(m.getMessage()) - tp;
					int dtemp = me - tp;
					if(((dtemp < d || d < 0) && dtemp > 0) ||( dtemp < 0 && d < 0 && dtemp < d)) {
						ca.requestToken();
					} else {
						ca.resubmitTokenRequest(m);
					}
				} else {
					ca.requestToken();
				}
		}
		return 1;
	}


}
