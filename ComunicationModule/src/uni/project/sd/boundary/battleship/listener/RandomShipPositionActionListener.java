package uni.project.sd.boundary.battleship.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import uni.project.sd.Control.battleship.BattleshipController;

public class RandomShipPositionActionListener implements ActionListener {
	
	@Override
	public void actionPerformed(ActionEvent e) {
		BattleshipController.getInstance(null, 0, 0).addShipsRandom();
	}

}
