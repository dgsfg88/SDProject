package uni.project.sd.comunications;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote {
	public boolean areYouOnline() throws RemoteException;
}
