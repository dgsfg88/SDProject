package uni.project.sd.server.boundary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import uni.project.sd.server.control.ServerController;

public class GameStarterListener implements ActionListener {

	private ServerController myController;
	
	public GameStarterListener(ServerController controller) {
		this.myController = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.myController.startGamePressed();
	}

}
