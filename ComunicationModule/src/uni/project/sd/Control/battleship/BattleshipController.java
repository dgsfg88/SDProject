package uni.project.sd.Control.battleship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import uni.project.sd.MainClass;
import uni.project.sd.Control.battleship.runnable.AutoplayThread;
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
import uni.project.sd.comunications.battleship.entity.BattleshipTokenContainer;
import uni.project.sd.comunications.battleship.entity.EventListItem;
import uni.project.sd.comunications.entity.Token;

public class BattleshipController implements FrontBoundary {
	private short oceanShared = 2;
	private boolean haveToken = false;

	private static BattleshipController controller;

	private int d = 10;
	private MainClass myMain;
	private Boolean gameOver = false;
	private Boolean gameEnded = false;

	private DummyFrontEntity myEntity;
	private BattleshipBoundary myBoundary;
	private int myPlayer;
	private Ocean ocean;

	private ArrayList<EventListItem> eventList;
	private Object lockEventList = new Object();
	private ArrayList<EventListItem> processedEvents;
	private ArrayList<Ship> ships;
	private Ship shipSelected;
	private int orientationSelected = 0;
	private int shipToPlace;
	private boolean oceanCompleted = false;
	private Integer[] shipsRemaining;

	private int hitNotUsed;
	private boolean oneShotPerShip = false;

	private Boolean autoplay = false;

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
	}

	public void startView() {
		myBoundary = new BattleshipBoundary(this, shipsRemaining.length, d,
				this.ships);
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
			this.myBoundary.setColor(player, x, y, BattleshipBoundary.Fog);
			player--;
			ServerAddress address = ServerAddress.getInstance();
			synchronized (lockEventList) {
				eventList.add(new EventListItem(address.getMyAddress(), address
						.getServer(player), x, y));
			}
			myMain.sendRealTimeAction(player, x, y);
			this.hitNotUsed--;
			if (hitNotUsed <= 0) {
				showLastMoves();
				myEntity.setPlayerTurn(false);
				this.myMain.releaseToken();
			}
		}
	}

	private void showLastMoves() {
		synchronized (lockEventList) {
			int hitNumber = 1;
			if (oneShotPerShip)
				hitNumber = this.shipsRemaining[0];
			int i = this.eventList.size() - hitNumber + this.hitNotUsed;
			for (; i < this.eventList.size(); i++) {
				updateGrid(this.eventList.get(i).getReceiver(), this.myPlayer,
						this.eventList.get(i).getX(), this.eventList.get(i)
								.getY());
			}
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
				if (result) {
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
			if (x + ship.getLength() <= d)
				for (int i = x; i < x + ship.getLength(); i++) {
					shipPos.add(new OceanCoordinate(i, y + offset, 0));
				}
		} else {
			if (y + ship.getLength() <= d)
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
		EventListItem item;
		if(k > 0 )
			item = new EventListItem(ServerAddress.getInstance().getMyAddress(), ServerAddress.getInstance().getServer(k-1), -1, -1);
		else
			item = new EventListItem(ServerAddress.getInstance().getMyAddress(), ServerAddress.getInstance().getMyAddress(), -1, -1);
		synchronized (lockEventList) {
			eventList.add(item);
		}
		this.processedEvents.add(item);
		this.myBoundary.disablePlayer(k);
	}

	public void updateGrid(String ID, int playerNumber, int x, int y) {
		ServerAddress serverAdd = ServerAddress.getInstance();
		OceanCoordinate myShot = new OceanCoordinate(x, y + d
				* serverAdd.getPlayerID(ID), 0);
		HashMap<Ship, Integer> hit = ocean.checkShot(myShot, playerNumber);

		this.processedEvents.add(new EventListItem("", ID, x, y));

		synchronized (shipsRemaining) {
			if (ID.equals(serverAdd.getMyAddress())) {
				if (shipsRemaining[0] > 0) {
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
									myBoundary.disablePlayer(-1);
									myBoundary.showAlert("Hai perso!");
								}
						}
					}
					myBoundary.setShipRemaining(0, shipsRemaining[0]);
				}
			} else {
				int enemyID = serverAdd.getServerNID(ID);
				if (shipsRemaining[enemyID + 1] > 0) {
					myBoundary.setValue(enemyID + 1, x, y, !hit.isEmpty());
					Set<Ship> shipsHit = hit.keySet();
					for (Ship s : shipsHit) {
						if (s.getHealth() <= 0) {
							shipsRemaining[enemyID + 1]--;
							myBoundary.showEnemyShip(s.getLength(), s
									.getFirstCoordinate().getX(), s
									.getFirstCoordinate().getY(), s
									.getOrientation(), enemyID);
							if (shipsRemaining[enemyID + 1] <= 0) {
								disablePlayer(enemyID);
							}
						}
					}
					myBoundary.setShipRemaining(enemyID + 1, shipsRemaining[enemyID + 1]);
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
					synchronized (gameEnded) {
						if (!gameEnded)
							myMain.releaseToken();
					}
					myBoundary.setButtonEnabled(false);
				} else {
					if (enabled) {
						if (oneShotPerShip)
							this.hitNotUsed = this.shipsRemaining[0];
						else
							this.hitNotUsed = 1;
					}
					synchronized (autoplay) {
						myBoundary.setButtonEnabled(enabled);
						if (autoplay && enabled) {
							myBoundary.setButtonEnabled(false);
							if (!gameOver) {
								new Thread(new AutoplayThread(
										this.shipsRemaining.length, this.d))
										.start();
							}
						}
					}
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
		synchronized (lockEventList) {
			this.eventList = eventList;
			if (this.eventList != null) {
				ServerAddress address = ServerAddress.getInstance();
				for (int i = 0; i < this.eventList.size(); i++) {
					EventListItem item = eventList.get(i);
					if (item.getBreeder().equals(address.getMyAddress()))
						eventList.remove(i);
					else {
						if (!this.processedEvents.contains(item)) {
							if(item.getX() > -1 && item.getY() > -1)
							updateGrid(item.getReceiver(),
									address.getPlayerID(item.getBreeder()),
									item.getX(), item.getY());
							else
								if(item.getReceiver().equals(address.getMyAddress()))
									destroyPlayer(0);
								else
									destroyPlayer(address.getServerNID(item.getReceiver())+1);
						}
					}
				}
			}
		}
	}

	public ArrayList<EventListItem> getEventList() {
		return eventList;
	}

	@Override
	public void disablePlayer(int k) {
		boolean result = true;
		myBoundary.disablePlayer(k);
		synchronized (shipsRemaining) {
			this.shipsRemaining[k + 1] = 0;
			for (int i = 1; i < this.shipsRemaining.length; i++) {
				if (!(this.shipsRemaining[i] <= 0)) {
					result = false;
				}
			}
		}

		if (result) {
			synchronized (gameEnded) {
				if (!this.gameEnded) {
					if (!gameOver)
						myBoundary.showAlert("You win!");
					this.gameEnded = true;
				}
			}
		}
	}

	public void setShipSelected(Ship myShip) {
		this.shipSelected = myShip;
		if (myShip != null)
			this.myBoundary.setLengthSelected(myShip.getLength());
		else
			this.myBoundary.setLengthSelected(0);
	}

	public void setOrientationSelected(int orientation) {
		this.orientationSelected = orientation;
		this.myBoundary.setOrientationSelected(orientation);
	}

	public void setShipNumber(Integer[] shipNumber) {
		this.ships = new ArrayList<>(shipNumber.length);
		for (int i = 0; i < 4; i++) {
			switch (i) {
			case 0:
				for (int k = 0; k < shipNumber[i]; k++)
					ships.add(new PatrolBoat(myPlayer));
				break;
			case 1:
				for (int k = 0; k < shipNumber[i]; k++)
					ships.add(new Destroyer(myPlayer));
				break;
			case 2:
				for (int k = 0; k < shipNumber[i]; k++)
					ships.add(new Battleship(myPlayer));
				break;
			case 3:
				for (int k = 0; k < shipNumber[i]; k++)
					ships.add(new AircraftCarrier(myPlayer));
				break;
			}
		}

		for (int i = 0; i < this.shipsRemaining.length; i++) {
			this.shipsRemaining[i] = ships.size();
		}

		this.myBoundary.setShips(this.ships);
	}

	public void setOneShotPerShip(boolean oneShotPerShip) {
		this.oneShotPerShip = oneShotPerShip;
	}

	public void setDimension(int dimension) {
		this.d = dimension;
	}

	public void setAutoplayState(boolean state) {
		synchronized (autoplay) {
			this.autoplay = state;
		}
	}

	public boolean isGameEnded() {
		synchronized (gameEnded) {
			return gameEnded;
		}
	}

	public int getShipRemaining(int player) {
		synchronized (shipsRemaining) {
			return shipsRemaining[player];
		}
	}

	public boolean canShot() {
		return this.hitNotUsed > 0;
	}

	public boolean isNewEvent(EventListItem event) {
		if (this.processedEvents != null)
			return !this.processedEvents.contains(event);
		else
			return true;
	}

	@Override
	public Token getToken() {
		BattleshipTokenContainer tokenContainer = null;
		synchronized (sendOceanLock) {
			if(oceanShared == 1) {
				tokenContainer = new BattleshipTokenContainer(null, ocean);
			} else {
				if(oceanShared <= 0) {
					tokenContainer = new BattleshipTokenContainer(this.eventList, null);
				}
			}
		}
		
		return tokenContainer;
	}

	@Override
	public void setToken(Token token) {
		BattleshipTokenContainer container = (BattleshipTokenContainer) token;
		if(oceanShared == 1) {
			Ocean o = container.getOcean();
			if(o != null) {
				updateOcean(o);
			}
		} else {
			if(oceanShared <= 0) {
				setEventList(container.getEventList());
			}
		}
	}
}
