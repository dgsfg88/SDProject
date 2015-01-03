package uni.project.sd.comunications.task;

import java.io.Serializable;

import saqib.rasul.Task;
/**
 * Task richiesta dello stato, restituisce sempre 1
 * @author Andrea
 *
 */
public class RequestState implements Task<Integer>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 721820586689272217L;
	/**
	 * Viene eseguito in remoto dal server
	 */
	@Override
	public Integer execute() {
		return 1;
	}

}
