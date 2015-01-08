package uni.project.sd.boundary.battleship;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import uni.project.sd.Control.BattleshipController;

public class BattleshipJButtonActionListener implements ActionListener {
	
	private BattleshipController myController;
	private int myRow, myCol, myPlayer;
	
	public BattleshipJButtonActionListener(BattleshipController controller, int row, int col, int player) {
		this.myController = controller;
		this.myCol = col;
		this.myPlayer = player;
		this.myRow = row;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.myController.buttonClicked(myPlayer,myRow,myCol);
	}

}
