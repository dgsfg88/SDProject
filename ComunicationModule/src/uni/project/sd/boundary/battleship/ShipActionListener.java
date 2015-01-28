package uni.project.sd.boundary.battleship;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import uni.project.sd.Control.battleship.BattleshipController;
import uni.project.sd.Entity.battleship.Ship;

public class ShipActionListener implements ActionListener {
	private Ship myShip;
	
	public ShipActionListener(Ship ship) {
		this.myShip = ship;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		BattleshipController.getInstance(null, 0, 0).setShipSelected(this.myShip);
	}

}
