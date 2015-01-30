package uni.project.sd.Control.battleship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import uni.project.sd.MainClass;
import uni.project.sd.Entity.DummyFrontEntity;
import uni.project.sd.Entity.battleship.AircraftCarrier;
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
import uni.project.sd.comunications.battleship.entity.EventListItem;

public class BattleshipController implements FrontBoundary {
	private short oceanShared = 2;
	private boolean haveToken = false;

	private static BattleshipController controller;

	public static final int d = 10;
	private MainClass myMain;
	private Boolean gameOver = false;

	private DummyFrontEntity myEntity;
	private BattleshipBoundary myBoundary;
	private int myPlayer;
	private Ocean ocean;

	private ArrayList<EventListItem> eventList;
	private ArrayList<EventListItem> processedEvents;
	private ArrayList<Ship> ships;
	private Ship shipSelected;
	private int orientationSelected = 0;
	private int shipToPlace;
	private boolean oceanCompleted = false;
	private Integer[] shipsRemaining;

	private Object sendOceanLock = new Object();

	public synchronized static BattleshipController getInstance(MainClass main,
			int myPlayer, int playerNumber) {
		if (controller == null)
			controller = new BattleshipController(main, myPlayer, playerNumber);
		return controller;
	}

	private BattleshipController(MainClass main, int myPlayer, int playerNumber) {
		this.myMain = main;
		this.myPlayer = myPlayer;
		this.processedEvents = new ArrayList<>();
		this.ships = new ArrayList<Ship>(Arrays.asList(new PatrolBoat(
				this.myPlayer), new PatrolBoat(this.myPlayer), new PatrolBoat(
				this.myPlayer), new PatrolBoat(this.myPlayer), new Destroyer(
				this.myPlayer), new Destroyer(this.myPlayer), new Destroyer(
				this.myPlayer), new Battleship(this.myPlayer), new Battleship(
				this.myPlayer), new Battleship(this.myPlayer),
				new AircraftCarrier(this.myPlayer)));
		this.shipToPlace = this.ships.size();
		this.shipsRemaining = new Integer[playerNumber];
		for (int i = 0; i < playerNumber; i++) {
			this.shipsRemaining[i] = ships.size();
		}
		myEntity = DummyFrontEntity.getInstance();
		myBoundary = new BattleshipBoundary(this, playerNumber, d, this.ships);
		myBoundary.setPlayerButtonEnabled(false);
		myEntity.addView(this);
	}

	public void gameReady() {
		myBoundary.setPlayerButtonEnabled(true);
	}

	public void buttonClicked(int player, int x, int y) {
		if (player == 0) {
			placeShip(x, y);
		} else {
			player--;
			myEntity.setPlayerTurn(false);
			ServerAddress address = ServerAddress.getInstance();
			updateGrid(address.getServer(player), this.myPlayer, x, y);
			synchronized (eventList) {
				eventList.add(new EventListItem(address.getMyAddress(), address
						.getServer(player), x, y));
			}
			myMain.releaseToken(player, x, y);
		}
	}

	private void placeShip(int x, int y) {
		if (shipSelected != null) {
			if (addShip(x, y, orientationSelected, shipSelected)) {
				// TODO da rivedere
				this.ships.remove(shipSelected);
				this.myBoundary.disableShip(shipSelected);
				shipSelected = null;
				this.shipToPlace--;
				if (this.shipToPlace == 0) {
					synchronized (sendOceanLock) {
						oceanCompleted = true;
					}
					sendOcean();
				}
			}
		}
	}

	public Ocean getOcean() {
		return ocean;
	}

	public void setOcean(Ocean ocean) {
		this.ocean = ocean;
	}

	public void addShipsRandom() {
		boolean result = false;
		Random r = new Random();
		for (Ship c : this.ships) {
			result = false;
			do {
				int x = r.nextInt(d);
				int y = r.nextInt(d);
				int or = r.nextInt(2);
				result = addShip(x, y, or, c);
				if(result){
					this.myBoundary.disableShip(c);
				}
			} while (!result);
		}
		synchronized (sendOceanLock) {
			this.oceanCompleted = true;
		}
		
		

		sendOcean();
	}

	public boolean addShip(int x, int y, int orient, Ship ship) {
		int offset = this.myPlayer * d;
		boolean result = false;
		LinkedList<OceanCoordinate> shipPos = new LinkedList<>();
		if (orient == 0) {
			if (x + ship.getLength() < d)
				for (int i = x; i < x + ship.getLength(); i++) {
					shipPos.add(new OceanCoordinate(i, y + offset, 0));
				}
		} else {
			if (y + ship.getLength() < d)
				for (int i = y + offset; i < y + offset + ship.getLength(); i++) {
					shipPos.add(new OceanCoordinate(x, i, 0));
				}
		}
		OceanCoordinate first = new OceanCoordinate(x, y, 0);
		if (this.ocean.deployShip(shipPos, ship, this.myPlayer)) {
			ship.setOrientation(orient);
			ship.setFirstCoordinate(first);
			myBoundary.addShip(ship.getLength(), x, y, orient);
			result = true;
		}
		return result;
	}

	public boolean checkHit(int x, int y) {
		return true;
	}

	public void destroyPlayer(int k) {
		this.myBoundary.disablePlayer(k);
	}

	public void updateGrid(String ID, int playerNumber, int x, int y) {
		ServerAddress serverAdd = ServerAddress.getInstance();
		OceanCoordinate myShot = new OceanCoordinate(x, y + d
				* serverAdd.getPlayerID(ID), 0);
		HashMap<Ship, Integer> hit = ocean.checkShot(myShot, playerNumber);

		this.processedEvents.add(new EventListItem("", ID, x, y));

		if (ID.equals(serverAdd.getMyAddress())) {
			myBoundary.setValue(0, x, y, !hit.isEmpty());
			Set<Ship> shipsHit = hit.keySet();
			for (Ship s : shipsHit) {
				if (s.getHealth() <= 0) {
					shipsRemaining[0]--;
					if (shipsRemaining[0] <= 0)
						synchronized (this.gameOver) {
							if (this.haveToken) {
								myMain.releaseToken();
							}
							this.gameOver = true;
							myBoundary.showAlert("Hai perso!");
						}
				}
			}
		} else {
			int enemyID = serverAdd.getServerNID(ID);
			myBoundary.setValue(enemyID + 1, x, y, !hit.isEmpty());
			Set<Ship> shipsHit = hit.keySet();
			for (Ship s : shipsHit) {
				if (s.getHealth() <= 0) {
					shipsRemaining[enemyID + 1]--;
					myBoundary.showEnemyShip(s.getLength(), s
							.getFirstCoordinate().getX(), s
							.getFirstCoordinate().getY(), s.getOrientation(),
							enemyID);
					if (shipsRemaining[enemyID + 1] <= 0) {
						disablePlayer(enemyID);
					}
				}
			}
		}
	}

	public void updateOcean(Ocean newOcean) {
		this.ocean.updateOcean(newOcean);
	}

	@Override
	public void addToLog(String Message) {
		System.out.println(Message);
	}

	@Override
	public synchronized void setButtonEnabled(boolean enabled) {
		synchronized (sendOceanLock) {
			this.haveToken = enabled;
		}
		if (haveToken && oceanShared > 0) {
			// l'oceano non � ancora stato condiviso (TODO implementare evento
			// nel caso in cui si tratti di un token recuperato)
			if (oceanShared == 2) {
				sendOcean();
			} else
				sendOcean();

		} else {
			synchronized (this.gameOver) {
				if (this.haveToken && this.gameOver) {
					myMain.releaseToken();
					myBoundary.setButtonEnabled(false);
				} else {
					myBoundary.setButtonEnabled(enabled);
				}
			}
			if (enabled)
				if (eventList == null)
					eventList = new ArrayList<>();
		}
	}

	private void sendOcean() {
		synchronized (sendOceanLock) {
			if (this.oceanShared == 2 && this.oceanCompleted)
				this.myBoundary.setPlayerButtonEnabled(false);
			if (haveToken && oceanCompleted) {
				new BattleshipActions().sendOcean(this.ocean);

				this.oceanShared--;
			}
		}
	}

	public void setEventList(ArrayList<EventListItem> eventList) {
		synchronized (eventList) {
			this.eventList = eventList;
			if (this.eventList != null) {
				ServerAddress address = ServerAddress.getInstance();
				for (int i = 0; i < this.eventList.size(); i++) {
					EventListItem item = eventList.get(i);
					if (item.getBreeder().equals(address.getMyAddress()))
						eventList.remove(i);
					else {
						if (!this.processedEvents.contains(item))
							updateGrid(item.getReceiver(),
									address.getPlayerID(item.getBreeder()),
									item.getX(), item.getY());
					}
				}
			}
		}
	}

	public ArrayList<EventListItem> getEventList() {
		synchronized (eventList) {
			return eventList;
		}
	}

	@Override
	public void disablePlayer(int k) {
		boolean result = true;
		myBoundary.disablePlayer(k);
		this.shipsRemaining[k + 1] = 0;
		for (int i = 1; i < this.shipsRemaining.length; i++) {
			if (!(this.shipsRemaining[i] <= 0)) {
				result = false;
			}
		}
		if (result) {
			myBoundary.showAlert("Hai Vinto!");
		}
	}

	public void setShipSelected(Ship myShip) {
		this.shipSelected = myShip;
		this.myBoundary.setLengthSelected(myShip.getLength());
	}

	public void setOrientationSelected(int orientation) {
		this.orientationSelected = orientation;
		this.myBoundary.setOrientationSelected(orientation);
	}
}
