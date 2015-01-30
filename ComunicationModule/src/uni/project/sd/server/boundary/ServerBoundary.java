package uni.project.sd.server.boundary;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import uni.project.sd.server.control.ServerController;
import uni.project.sd.server.entity.Player;

public class ServerBoundary {
	
	private JFrame mainView;
	private JLabel myIp, myPort;
	private ArrayList<JComboBox<Integer>> shipsSelectors;
	
	public ServerBoundary(ServerController controller) {
		this.mainView = new JFrame("Battleship Server");
		this.mainView.getContentPane().setLayout(new BorderLayout());
		
		JPanel tmpPanel = new JPanel(new GridLayout(1,4));
		
		shipsSelectors = new ArrayList<>();
		Integer[] items = {0,1,2,3,4,5,6,7,8,9};
		for(int i = 0; i < 4; i++) {
			JPanel cmbPanel = new JPanel(new BorderLayout());
			JLabel label = new JLabel("Ship " + i);
			cmbPanel.add(label,BorderLayout.LINE_START);
			JComboBox<Integer> shipNumbers = new JComboBox<>(items);
			shipNumbers.setSelectedIndex(1);
			shipsSelectors.add(shipNumbers);
			cmbPanel.add(shipNumbers,BorderLayout.CENTER);
			tmpPanel.add(cmbPanel);
		}
		
		mainView.getContentPane().add(tmpPanel, BorderLayout.PAGE_START);
		
		JList<Player> playersList = new JList<>(controller.getPlayerList());
		mainView.getContentPane().add(new JScrollPane(playersList), BorderLayout.CENTER);
		
		tmpPanel = new JPanel(new BorderLayout());
		JPanel addressPanel = new JPanel(new GridLayout(1,2));
		myIp = new JLabel("Server IP: 127.0.0.1");
		addressPanel.add(myIp);
		myPort = new JLabel("Server port: 1099");
		addressPanel.add(myPort);
		tmpPanel.add(addressPanel,BorderLayout.CENTER);
		
		JButton startGame = new JButton("Start Game");
		startGame.addActionListener(new GameStarterListener(controller));
		tmpPanel.add(startGame,BorderLayout.LINE_END);
		
		mainView.getContentPane().add(tmpPanel, BorderLayout.PAGE_END);
		
		mainView.setSize(600, 250);
		mainView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainView.setVisible(true);
	}
	
	public synchronized void setSelectorEnable(boolean enabled) {
		for(JComboBox<Integer> comboBox: this.shipsSelectors) {
			comboBox.setEnabled(enabled);
		}
	}
	
	public Integer getShipNumber(int shipIndex) {
		return (Integer)this.shipsSelectors.get(shipIndex).getSelectedItem();
	}
	
	public synchronized void setAddress(String ip, int port) {
		this.myIp.setText("Server IP: "+ip);
		this.myPort.setText("Server port: "+port);
	}
}
