package uni.project.sd.server.boundary;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
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
	private JComboBox<Integer> gridDimensions;
	private JCheckBox checkBox;
	
	public ServerBoundary(ServerController controller) {
		this.mainView = new JFrame("Battleship Server");
		this.mainView.getContentPane().setLayout(new BorderLayout());
		
		JPanel tmpPanel = new JPanel(new GridLayout(1,4));
		
		shipsSelectors = new ArrayList<>();
		Integer[] items = {0,1,2,3,4,5,6,7,8,9};
		
		JPanel optionPanel = new JPanel(new GridLayout(0, 1));

		for(int i = 0; i < 4; i++) {
			JPanel cmbPanel = new JPanel(new BorderLayout());
			JLabel label = new JLabel("Ship " + (i+2));
			cmbPanel.add(label,BorderLayout.LINE_START);
			JComboBox<Integer> shipNumbers = new JComboBox<>(items);
			shipNumbers.setSelectedIndex(1);
			shipsSelectors.add(shipNumbers);
			cmbPanel.add(shipNumbers,BorderLayout.CENTER);
			tmpPanel.add(cmbPanel);
		}
		
		optionPanel.add(tmpPanel);
		tmpPanel = new JPanel();
		tmpPanel.add(new JLabel("One shot per ship: "));
		checkBox = new JCheckBox();
		
		Integer[] items2 = {10,11,12,13,14,15,16,17,18,19,20};
		gridDimensions = new JComboBox<>(items2);
		tmpPanel.add(checkBox);
		tmpPanel.add(new JLabel("Grid size: "));
		tmpPanel.add(gridDimensions);
		optionPanel.add(tmpPanel);
		mainView.getContentPane().add(optionPanel, BorderLayout.PAGE_START);
		
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
		startGame.addActionListener(new GameStarterListener(controller,startGame));
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
	
	public Integer[] getShipNumber() {
		Integer[] shipNumber = new Integer[this.shipsSelectors.size()];
		for(int i = 0; i < this.shipsSelectors.size(); i++) {
			shipNumber[i] = (Integer)this.shipsSelectors.get(i).getSelectedItem();
		}
		
		return shipNumber;
	}
	
	public boolean getShotType() {
		return checkBox.isSelected();
	}
	
	public Integer getGridSize() {
		return (Integer) this.gridDimensions.getSelectedItem();
	}
}
