package uni.project.sd.comunications;

import java.io.Serializable;

import saqib.rasul.Task;

public class RequestState implements Task<Integer>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 721820586689272217L;

	@Override
	public Integer execute() {
		return 1;
	}

}
