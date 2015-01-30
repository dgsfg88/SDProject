package uni.project.sd.server.boundary;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

public class ServerBoundary {
	
	private JFrame mainView;
	private JLabel myIp, myPort;
	private JList<String> playersList;
	
	public ServerBoundary() {
		this.mainView = new JFrame("Battleship Server");
		this.mainView.getContentPane().setLayout(new BorderLayout());
		
		JPanel tmpPanel = new JPanel(new GridLayout(1,4));
		
		Integer[] items = {1,2,3,4,5,6,7,8,9};
		for(int i = 0; i < 4; i++) {
			JPanel cmbPanel = new JPanel(new BorderLayout());
			JLabel label = new JLabel("Ship " + i);
			cmbPanel.add(label,BorderLayout.LINE_START);
			JComboBox<Integer> shipNumbers = new JComboBox<>(items);
			cmbPanel.add(shipNumbers,BorderLayout.CENTER);
			tmpPanel.add(cmbPanel);
		}
		
		mainView.getContentPane().add(tmpPanel, BorderLayout.PAGE_START);
		
		String[] players = {"Player_1@127.0.0.1:1099","Player_2@127.0.0.1:1099","Player_3@127.0.0.1:1099","Player_4@127.0.0.1:1099"};
		playersList = new JList<>(players);
		mainView.getContentPane().add(playersList, BorderLayout.CENTER);
		
		tmpPanel = new JPanel(new BorderLayout());
		JPanel addressPanel = new JPanel(new GridLayout(1,2));
		myIp = new JLabel("Server IP: 127.0.0.1");
		addressPanel.add(myIp);
		myPort = new JLabel("Server port: 1099");
		addressPanel.add(myPort);
		tmpPanel.add(addressPanel,BorderLayout.CENTER);
		
		JButton startGame = new JButton("Start Game");
		tmpPanel.add(startGame,BorderLayout.LINE_END);
		
		mainView.getContentPane().add(tmpPanel, BorderLayout.PAGE_END);
		
		mainView.setSize(600, 250);
		mainView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainView.setVisible(true);
	}
}
