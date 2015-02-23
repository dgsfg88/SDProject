package uni.project.sd.boundary.battleship.listener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

import uni.project.sd.Control.battleship.BattleshipController;

public class AutoPlayListener implements ItemListener {

	private BattleshipController controller;
	private JCheckBox myCheckBox;
	
	public AutoPlayListener(BattleshipController controller, JCheckBox checkBox) {
		this.controller = controller;
		this.myCheckBox = checkBox;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		this.controller.setAutoplayState(e.getStateChange() == ItemEvent.SELECTED);
		myCheckBox.setEnabled(e.getStateChange() != ItemEvent.SELECTED);
	}

}
