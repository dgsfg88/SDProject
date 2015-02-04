package uni.project.sd.boundary.battleship.listener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import uni.project.sd.Control.battleship.BattleshipController;

public class AutoPlayListener implements ItemListener {

	private BattleshipController controller;
	
	public AutoPlayListener(BattleshipController controller) {
		this.controller = controller;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		this.controller.setAutoplayState(e.getStateChange() == ItemEvent.SELECTED);
	}

}
