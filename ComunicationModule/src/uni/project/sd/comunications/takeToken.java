package uni.project.sd.comunications;

import java.io.Serializable;

import saqib.rasul.Task;
import uni.project.sd.Entity.DummyFrontEntity;

public class takeToken implements Task<Integer>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3077286184776314886L;

	@Override
	public Integer execute() {
		DummyFrontEntity.getInstance().setPlayerTurn(true);
		return 1;
	}

}
