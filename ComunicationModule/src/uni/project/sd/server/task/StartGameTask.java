package uni.project.sd.server.task;

import saqib.rasul.Task;
import uni.project.sd.comunications.entity.Message;
import uni.project.sd.comunications.task.MessageBase;

public class StartGameTask extends MessageBase implements Task<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5378422556616697690L;

	public StartGameTask(Message message) {
		this.m = message;
	}

	@Override
	public Integer deliver() {
		// TODO Auto-generated method stub
		return 1;
	}

}
