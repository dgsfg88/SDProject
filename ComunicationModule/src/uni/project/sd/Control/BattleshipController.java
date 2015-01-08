package uni.project.sd.Control;

import java.util.Random;

import uni.project.sd.MainClass;
import uni.project.sd.Entity.DummyFrontEntity;
import uni.project.sd.boundary.battleship.BattleshipBoundary;

public class BattleshipController {
	private static BattleshipController controller;
	
	private MainClass myMain;

	private DummyFrontEntity myEntity;
	private BattleshipBoundary myBoundary;
	
	private int shipCounter = 0;
	
	public static BattleshipController getInstance(MainClass main ,int myPlayer) {
		if(controller == null)
			controller = new BattleshipController(main, myPlayer);
		return controller;
	}
	
	private BattleshipController(MainClass main ,int myPlayer) {
		this.myMain = main;
		
		myBoundary = new BattleshipBoundary(this, myPlayer);
		
		myEntity = DummyFrontEntity.getInstance();
		
		addRandomCraft();
	}
	
	public void buttonClicked(int player, int row, int col) {
		myEntity.setPlayerTurn(false);
		myMain.relaseToken(player,row,col);
	}

	
	private void addRandomCraft() {
		int[] craft = {2,2,3,4};
		
		boolean result = false;
		Random r = new Random();
		for(int c: craft) {
			shipCounter += c;
			do
				result = myBoundary.addShip(c, r.nextInt(BattleshipBoundary.row), r.nextInt(BattleshipBoundary.col), r.nextInt(2));
			while(!result);
		}
	}
	
	public boolean checkHit(int x, int y) {
		boolean result = myBoundary.hit(x, y);
		
		if(result) {
			shipCounter--;
			if(shipCounter == 0)
				System.exit(0);
		}
			
		return result;
	}

	public void destroyPlayer(int k) {
		this.myBoundary.disablePlayer(k);
	}

	public void updateGrid(int ID, int x, int y, boolean result) {
		myBoundary.setValue(ID,x,y,result);
	}
}
