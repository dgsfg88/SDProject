package uni.project.sd.server.task;

import uni.project.sd.MainClass;

public class StartGameThread implements Runnable {

	private Integer[] shipNumber;
	private boolean oneShotPerShip;
	private int dimension;
	
	public StartGameThread(Integer[] shipNumber, boolean oneShotPerShip, int dimension) {
		this.shipNumber = shipNumber;
		this.dimension = dimension;
		this.oneShotPerShip = oneShotPerShip;
	}
	
	@Override
	public void run() {
		new MainClass(this.shipNumber,oneShotPerShip,dimension);
	}

}
