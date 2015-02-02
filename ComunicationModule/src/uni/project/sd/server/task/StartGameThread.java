package uni.project.sd.server.task;

import uni.project.sd.MainClass;

public class StartGameThread implements Runnable {

	private Integer[] shipNumber;
	
	public StartGameThread(Integer[] shipNumber) {
		this.shipNumber = shipNumber;
	}
	
	@Override
	public void run() {
		new MainClass(this.shipNumber);
	}

}
