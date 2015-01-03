package uni.project.sd.comunications;

import java.rmi.RemoteException;

import saqib.rasul.Compute;
import saqib.rasul.Task;

public class ComputeEngine
implements Compute {
/**
 * Esegue un task passato via RMI
 */
@Override
public <T> T executeTask(Task<T> t)
    throws RemoteException {
    System.out.println("got compute task: " + t);
    return t.execute();
}

public Integer deliverMessage(DeliverMessage dm) throws RemoteException {
	System.out.println("received message");
	return dm.deliver();
}
}
