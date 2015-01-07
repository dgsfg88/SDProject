package uni.project.sd.comunications.task;

import java.io.Serializable;

import saqib.rasul.Task;
import uni.project.sd.comunications.DeliverMessage;
import uni.project.sd.comunications.entity.Message;
import uni.project.sd.event.EventCounter;

public abstract class MessageBase implements DeliverMessage, Serializable,
		Task<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3975819091656395171L;

	protected Message m;
	
	@Override
	public void setMessage(Message m) {
		this.m = m;
		this.m.setMyTime(EventCounter.getInstance(null).getNextEvent());
	}

	@Override
	public Message getMessage() {
		return this.m;
	}

	@Override
	public abstract Integer deliver();

	@Override
	public Integer execute() {
		return deliver();
	}
}
