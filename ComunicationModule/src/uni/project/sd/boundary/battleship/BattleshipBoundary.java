package uni.project.sd.boundary.battleship;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import uni.project.sd.Control.battleship.BattleshipController;
import uni.project.sd.Entity.DummyFrontEntity;
import uni.project.sd.Entity.battleship.Ship;
import uni.project.sd.boundary.FrontBoundary;
import uni.project.sd.comunications.ServerAddress;

public class BattleshipBoundary implements FrontBoundary{
	public static final int Horizontal = 0;
	public static final int Vertical = 1;
	
	private int row;
	private int col;
	private JFrame mainWindow;
	private ArrayList<ArrayList<JButton>> playersIcons;
	
	private ArrayList<Color> shipColors = new ArrayList<>();
	
	public BattleshipBoundary(BattleshipController controller, Integer playerNumber, int d) {
		mainWindow = new JFrame("Battleship");
		mainWindow.getContentPane().setLayout(new GridLayout(0, 2,10,10));
		this.row = col = d;

		Color[] colors = {Color.GRAY, Color.GREEN, Color.MAGENTA, Color.WHITE};
		for(Color c: colors) {
			shipColors.add(c);
		}
		
		playersIcons = new ArrayList<ArrayList<JButton>>(playerNumber);

		JPanel playerPanel = null;
		ArrayList<JButton> buttons = null;
		JButton singleButton = null;
		for(int k = 0; k < playerNumber; k++) {
			playerPanel = new JPanel(new GridLayout(row, col));
			buttons = new ArrayList<JButton>(col*row);
			
			for(int r = 0; r < row; r++){
				for(int c = 0; c < col; c++) {
					singleButton = new JButton();
					singleButton.setSize(5, 5);
					singleButton.setBackground(Color.BLUE);
					if(k == 0)
						singleButton.setEnabled(false);
					else
						singleButton.addActionListener(new BattleshipJButtonActionListener(controller, r, c, k-1));
					
					buttons.add(singleButton);
					playerPanel.add(singleButton);
				}
			}
			mainWindow.add(playerPanel);
			playersIcons.add(buttons);
		}
		
		DummyFrontEntity.getInstance().addView(this);
		
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.pack();
		mainWindow.setSize(500, 500);
		mainWindow.setVisible(true);
	}
	
	public void addShip(int length, int startX, int startY, int orient) {
		int x = startX;
		int y = startY;
		Color shipColor = shipColors.remove(0);
		if (orient == Horizontal){
			for (; x < startX + length; x++){
				JButton b = playersIcons.get(0).get(getCoordinate(x, y));
				b.setBackground(shipColor);
			}
		} else {
			for (; y < startY + length; y++){
				JButton b = playersIcons.get(0).get(getCoordinate(x, y));
				b.setBackground(shipColor);
			}
		}
	}
	
	private int getCoordinate (int x, int y) {
		return x*col+y;
	}
	
	@Override
	public void addToLog(String Message) {
		System.out.println(ServerAddress.getInstance().getMyAddress() + ": " +Message);
	}

	@Override
	public void setButtonEnabled(boolean enabled) {
		synchronized (playersIcons) {
			for(int k = 1; k < playersIcons.size(); k++) {
				for(JButton b: playersIcons.get(k))
					if(enabled) {
						if(b.getBackground().equals(Color.BLUE)) {
							b.setEnabled(enabled);
							b.setBackground(Color.CYAN);
						}
					} else {
						if(b.getBackground().equals(Color.CYAN)) {
							b.setBackground(Color.BLUE);
						}
						b.setEnabled(false);
					}
			}
		}
	}

	public boolean hit(int x, int y) {
		boolean result = false;
		synchronized (playersIcons) {
			JButton b = playersIcons.get(0).get(getCoordinate(x, y));
			
			result = b.getBackground().equals(Color.GRAY);
			if(result) 
				b.setBackground(Color.RED);
			else
				b.setBackground(Color.YELLOW);
		}
		return result;
	}

	@Override
	public void disablePlayer(int k) {
		synchronized (playersIcons) {
			for(JButton b: playersIcons.get(k+1)) {
				b.setBackground(Color.BLACK);
				b.setEnabled(false);
			}
		}
	}

	public void setValue(int iD, int x, int y, boolean result) {
		Color c = Color.YELLOW;
		synchronized (playersIcons) {
			if(result)
				c = Color.RED;
			playersIcons.get(iD+1).get(getCoordinate(x, y)).setBackground(c);
		}
	}
}
