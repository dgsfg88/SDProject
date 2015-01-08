package uni.project.sd.boundary.battleship;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import uni.project.sd.Control.BattleshipController;
import uni.project.sd.Entity.DummyFrontEntity;
import uni.project.sd.boundary.BattleshipJButtonActionListener;
import uni.project.sd.boundary.FrontBoundary;

public class BattleshipBoundary implements FrontBoundary{
	public static final int Horizontal = 0;
	public static final int Vertical = 1;
	
	public final static int row = 15;
	public final static int col = 15;
	private JFrame mainWindow;
	private ArrayList<ArrayList<JButton>> playersIcons;
	
	public BattleshipBoundary(BattleshipController controller, Integer playerNumber) {
		mainWindow = new JFrame("Battleship");
		mainWindow.getContentPane().setLayout(new GridLayout(0, 2,10,10));

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
		mainWindow.setVisible(true);
	}
	
	public boolean addShip(int length, int startX, int startY, int orient) {
		boolean returnValue = false;
		if((startX + length <= row && orient == Horizontal) || (startY + length <= col && orient == Vertical)) {
			if(length == 0) {
				try {
					synchronized (playersIcons) {
						returnValue = playersIcons.get(0).get(getCoordinate(startX, startY)).getBackground().equals(Color.BLUE);
					}
				} catch (IndexOutOfBoundsException e) {
					returnValue = false;
				}
			} else {
				switch (orient) {
				case Horizontal:
					startX++;
					break;
				case Vertical:
					startY++;
					break;
				default:
					returnValue = false;
				}
				returnValue = addShip(--length, startX, startY, orient);
			}
			
			if(returnValue)
				synchronized (playersIcons) {
					playersIcons.get(0).get(getCoordinate(startX, startY)).setBackground(Color.GRAY);
				}
		}
		return returnValue;
	}
	
	private int getCoordinate (int x, int y) {
		return x*col+y;
	}
	
	@Override
	public void addToLog(String Message) {
		System.out.println(Message);
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
