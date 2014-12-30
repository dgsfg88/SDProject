package uni.project.sd.Control;

import uni.project.sd.MainClass;
import uni.project.sd.Entity.DummyFrontEntity;
import uni.project.sd.boundary.DummyFront;

public class DummyController {
	private DummyFrontEntity myEntity;
	private MainClass myMain;
	
	public DummyController(MainClass main) {
		myEntity = DummyFrontEntity.getInstance();
		new DummyFront(this);
		this.myMain = main;
	}

	public void tokenRelased() {
		myEntity.setPlayerTurn(false);
		myMain.relaseToken();
	}

	public void sendAnAction() {
		myMain.sendAction();
	}
	
	public void printMessage(String message) {
		this.myEntity.addMessage(message);
	}
	
	public void takeToken() {
		myEntity.setPlayerTurn(true);
	}
}
