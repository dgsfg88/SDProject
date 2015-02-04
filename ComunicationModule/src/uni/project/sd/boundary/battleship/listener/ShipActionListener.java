package uni.project.sd.boundary.battleship.listener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import uni.project.sd.Control.battleship.BattleshipController;
import uni.project.sd.Entity.battleship.Ship;

public class ShipActionListener implements ItemListener {
	private Ship myShip;
	
	public ShipActionListener(Ship ship) {
		this.myShip = ship;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == ItemEvent.SELECTED)
			BattleshipController.getInstance(null, 0, 0).setShipSelected(this.myShip);
		else if(e.getStateChange() == ItemEvent.DESELECTED) {
			BattleshipController.getInstance(null, 0, 0).setShipSelected(null);
		}
	}
	
	

}
