package uni.project.sd.Control;

import java.util.LinkedList;
import java.util.Random;

import uni.project.sd.MainClass;
import uni.project.sd.Entity.DummyFrontEntity;
import uni.project.sd.Entity.battleship.Battleship;
import uni.project.sd.Entity.battleship.Destroyer;
import uni.project.sd.Entity.battleship.Ocean;
import uni.project.sd.Entity.battleship.OceanCoordinate;
import uni.project.sd.Entity.battleship.PatrolBoat;
import uni.project.sd.Entity.battleship.Ship;
import uni.project.sd.boundary.battleship.BattleshipBoundary;

public class BattleshipController {
	private static BattleshipController controller;

	public static final int d = 10;
	private MainClass myMain;

	private DummyFrontEntity myEntity;
	private BattleshipBoundary myBoundary;
	private int myPlayer;
	private Ocean ocean;

	private int shipCounter = 0;

	public static BattleshipController getInstance(MainClass main, int myPlayer) {
		if (controller == null)
			controller = new BattleshipController(main, myPlayer);
		return controller;
	}

	private BattleshipController(MainClass main, int myPlayer) {
		this.myMain = main;
		this.myPlayer = myPlayer;

		myBoundary = new BattleshipBoundary(this, myPlayer, d);

		myEntity = DummyFrontEntity.getInstance();

		addRandomCraft();
	}

	public void buttonClicked(int player, int row, int col) {
		myEntity.setPlayerTurn(false);
		myMain.relaseToken(player, row, col);
	}

	public Ocean getOcean() {
		return ocean;
	}

	public void setOcean(Ocean ocean) {
		this.ocean = ocean;
	}

	private void addRandomCraft() {
		Ship[] craft = {new PatrolBoat(this.myPlayer), new PatrolBoat(this.myPlayer), new Destroyer(this.myPlayer), new Battleship(this.myPlayer)};
		int offset = this.myPlayer * d;
		boolean result = false;
		Random r = new Random();
		for(Ship c: craft) {
			result = false;
			do {
				int x = r.nextInt(d);
				int y = r.nextInt(d);
				int or = r.nextInt(2);
				LinkedList<OceanCoordinate> shipPos = new LinkedList<>();
				if (or == 0){
					for (int i=x; i < x+c.getLength()-1; i++){
						shipPos.add(new OceanCoordinate(i, y+offset, 0));
					}
				} else {
					for (int i=y+offset; i < y+offset+c.getLength()-1; i++){
						shipPos.add(new OceanCoordinate(x, i, 0));
					}
				}
				if(this.ocean.deployShip(shipPos,c,this.myPlayer)){
					myBoundary.addShip(c.getLength(), x, y, or);
					result = true;
				}
			}while(!result);
		}
	}

	public boolean checkHit(int x, int y) {
		boolean result = myBoundary.hit(x, y);

		if (result) {
			shipCounter--;
			if (shipCounter == 0)
				System.exit(0);
		}

		return result;
	}

	public void destroyPlayer(int k) {
		this.myBoundary.disablePlayer(k);
	}

	public void updateGrid(int ID, int x, int y, boolean result) {
		myBoundary.setValue(ID, x, y, result);
	}
}
