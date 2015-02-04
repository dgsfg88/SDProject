package uni.project.sd.Control.battleship.runnable;

import java.util.Random;

import uni.project.sd.Control.battleship.BattleshipController;
import uni.project.sd.comunications.ServerAddress;
import uni.project.sd.comunications.battleship.entity.EventListItem;

public class AutoplayThread implements Runnable {

	private static final int minTime = 600;
	private static final int maxTime = 2000;
	
	private BattleshipController controller;
	private Random rand = new Random();
	private ServerAddress address;
	private int playerNumber;
	private int d;
	
	public AutoplayThread(int playerNumber, int d) {
		this.controller = BattleshipController.getInstance(null, 0, 0);
		this.address = ServerAddress.getInstance();
		this.playerNumber = playerNumber-1;
		this.d = d;
	}
	
	@Override
	public void run() {
		while(!controller.isGameEnded() && controller.canShot()) {
			int player = rand.nextInt(playerNumber) + 1;
			if(controller.getShipRemaining(player) > 0) {
				int x = rand.nextInt(d);
				int y = rand.nextInt(d);
				EventListItem item = new EventListItem(address.getMyAddress(), address.getServer(player-1), x, y);
				if(controller.isNewEvent(item))
					controller.buttonClicked(player, x, y);
			}
			
			try {
				Thread.sleep(rand.nextInt(maxTime-minTime)+minTime);
			} catch (InterruptedException e) {
	
			}
		}
	}

}
