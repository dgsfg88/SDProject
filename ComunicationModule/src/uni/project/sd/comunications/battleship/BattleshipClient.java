package uni.project.sd.comunications.battleship;

import saqib.rasul.Task;
import uni.project.sd.comunications.OutcomingClient;
import uni.project.sd.comunications.battleship.task.BattleshipRelaseToken;
import uni.project.sd.comunications.battleship.task.ExecuteHit;
import uni.project.sd.comunications.battleship.task.ExecuteHitResult;
import uni.project.sd.comunications.battleship.task.UpdateOcean;
import uni.project.sd.comunications.entity.Message;

public class BattleshipClient extends OutcomingClient {
	public static final int sendHit = 18;
	public static final int sendHitResult = 19;
	public static final int sendOcean = 22;
	
	public BattleshipClient() {
		super();
	}
	public BattleshipClient(int taskType) {
		super(taskType);
	}
	public BattleshipClient(int taskType, Message m) {
		super(taskType, m);
	}
	
	@Override
	public void doCustomRmiHandling(String serverID) {
		Task<Integer> task = null;
		switch (this.getTaskType()) {
		case sendHit:
			task = new ExecuteHit();
			((ExecuteHit) task).setMessage(this.getM());
			break;
		case sendHitResult:
			task = new ExecuteHitResult();
			((ExecuteHitResult)task).setMessage(getM());
			break;
		case sendOcean:
			task = new UpdateOcean(getM());
			break;
		case OutcomingClient.notifyToken:
			task = new BattleshipRelaseToken(getM());
			break;
		default:
			super.doCustomRmiHandling(serverID);
			break;
		}
		if(task!=null)
			doTask(serverID, task);
	}
}
