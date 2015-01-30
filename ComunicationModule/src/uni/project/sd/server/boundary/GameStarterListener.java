package uni.project.sd.server.boundary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import uni.project.sd.server.control.ServerController;

public class GameStarterListener implements ActionListener {

	private ServerController myController;
	private JButton button;
	
	public GameStarterListener(ServerController controller, JButton button) {
		this.myController = controller;
		this.button = button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.myController.startGamePressed();
		button.setEnabled(false);
	}

}
