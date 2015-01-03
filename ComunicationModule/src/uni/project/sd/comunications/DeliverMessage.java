package uni.project.sd.comunications;

import uni.project.sd.comunications.entity.Message;

public interface DeliverMessage {
	public void setMessage(Message m);
	public Message getMessage();
	public Integer deliver();
}
