package uni.project.sd.Control.battleship;

import java.util.HashMap;
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
import uni.project.sd.boundary.FrontBoundary;
import uni.project.sd.boundary.battleship.BattleshipBoundary;
import uni.project.sd.comunications.ServerAddress;
import uni.project.sd.comunications.battleship.BattleshipActions;

public class BattleshipController implements FrontBoundary {
	private boolean oceanShared = false;
	private boolean haveToken = false;

	private static BattleshipController controller;

	public static final int d = 10;
	private MainClass myMain;

	private DummyFrontEntity myEntity;
	private BattleshipBoundary myBoundary;
	private int myPlayer;
	private Ocean ocean;

	private int shipCounter = 0;

	public static BattleshipController getInstance(MainClass main,
			int myPlayer, int playerNumber) {
		if (controller == null)
			controller = new BattleshipController(main, myPlayer, playerNumber);
		return controller;
	}

	private BattleshipController(MainClass main, int myPlayer, int playerNumber) {
		this.myMain = main;
		this.myPlayer = myPlayer;

		myEntity = DummyFrontEntity.getInstance();
		myEntity.addView(this);
		myBoundary = new BattleshipBoundary(this, playerNumber, d);

	}

	public void buttonClicked(int player, int x, int y) {
		myEntity.setPlayerTurn(false);
		ServerAddress serverAdd = ServerAddress.getInstance();
		OceanCoordinate myShot = new OceanCoordinate(x, y + d * serverAdd.getPlayerID(serverAdd.getServer(player)),0);
		HashMap<Ship, Integer> hit = ocean.checkShot(myShot, this.myPlayer);
		myBoundary.setValue(player, x, y, !hit.isEmpty());
		myMain.relaseToken(player, x, y);
	}

	public Ocean getOcean() {
		return ocean;
	}

	public void setOcean(Ocean ocean) {
		this.ocean = ocean;
	}

	public void addRandomCraft() {
		Ship[] craft = { new PatrolBoat(this.myPlayer),
				new PatrolBoat(this.myPlayer), new Destroyer(this.myPlayer),
				new Battleship(this.myPlayer) };
		int offset = this.myPlayer * d;
		boolean result = false;
		Random r = new Random();
		for (Ship c : craft) {
			result = false;
			do {
				int x = r.nextInt(d);
				int y = r.nextInt(d);
				int or = r.nextInt(2);
				LinkedList<OceanCoordinate> shipPos = new LinkedList<>();
				if (or == 0) {
					if (x + c.getLength() < d)
						for (int i = x; i < x + c.getLength(); i++) {
							shipPos.add(new OceanCoordinate(i, y + offset, 0));
						}
				} else {
					if (y + c.getLength() < d)
						for (int i = y + offset; i < y + offset + c.getLength(); i++) {
							shipPos.add(new OceanCoordinate(x, i, 0));
						}
				}
				if (this.ocean.deployShip(shipPos, c, this.myPlayer)) {
					myBoundary.addShip(c.getLength(), x, y, or);
					result = true;
				}
			} while (!result);
		}

		new BattleshipActions().sendOcean(this.ocean);
		this.oceanShared = true;
	}

	public boolean checkHit(int x, int y) {
		return true;
	}

	public void destroyPlayer(int k) {
		this.myBoundary.disablePlayer(k);
	}

	public void updateGrid(int ID, int x, int y, boolean result) {
		myBoundary.setValue(ID, x, y, result);
	}

	public void updateOcean(Ocean newOcean) {
		this.ocean.updateOcean(newOcean);
	}

	@Override
	public void addToLog(String Message) {
		System.out.println(Message);
	}

	@Override
	public void setButtonEnabled(boolean enabled) {
		this.haveToken = enabled;
		if (haveToken && !oceanShared) {
			// XXX l'oceano non � ancora stato condiviso (implementare evento
			// nel caso in cui si tratti di un token recuperato)
			addRandomCraft();
		} else {
			// TODO azione comune da svolgere quando � il proprio turno
		}
	}

	@Override
	public void disablePlayer(int k) {
		// TODO Richiamato quando un giocatore viene disabilitato

	}
}
