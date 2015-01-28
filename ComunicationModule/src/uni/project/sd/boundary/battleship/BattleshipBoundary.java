package uni.project.sd.boundary.battleship;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uni.project.sd.Control.battleship.BattleshipController;
import uni.project.sd.comunications.ServerAddress;

public class BattleshipBoundary {
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

		Color[] colors = {Color.ORANGE, Color.GREEN, Color.MAGENTA, Color.WHITE};
		for(Color c: colors) {
			shipColors.add(c);
		}
		
		playersIcons = new ArrayList<ArrayList<JButton>>(playerNumber);

		JPanel playerPanel = null;
		JPanel playerHome = null;
		ArrayList<JButton> buttons = null;
		JButton singleButton = null;
		ServerAddress address = ServerAddress.getInstance();
		
		ImageIcon icon = new ImageIcon("texture/sea.png");
		icon = new ImageIcon(icon.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
		
		for(int k = 0; k < playerNumber; k++) {
			playerHome = new JPanel(new BorderLayout());
			if(k > 0)
				playerHome.add(new JLabel(address.getServer(k-1)+"@"+address.getLocation(address.getServer(k-1))),BorderLayout.PAGE_START);
			else
				playerHome.add(new JLabel(address.getMyAddress()),BorderLayout.PAGE_START);
			playerPanel = new JPanel(new GridLayout(row, col));
			playerHome.add(playerPanel,BorderLayout.CENTER);
			buttons = new ArrayList<JButton>(col*row);
			
			for(int r = 0; r < row; r++){
				for(int c = 0; c < col; c++) {
					singleButton = new JButton();
					singleButton.setSize(5, 5);
					singleButton.setIcon(icon);
					singleButton.setDisabledIcon(icon);
					singleButton.setBackground(Color.BLUE);
					if(k == 0)
						singleButton.setEnabled(false);
					else
						singleButton.addActionListener(new BattleshipJButtonActionListener(controller, c, r, k-1));
					
					buttons.add(singleButton);
					playerPanel.add(singleButton);
				}
			}
			mainWindow.add(playerHome);
			playersIcons.add(buttons);
		}
		
		//DummyFrontEntity.getInstance().addView(this);
		
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.pack();
		mainWindow.setSize(500, 250+250*((playerNumber-1)/2));
		mainWindow.setVisible(true);
	}
	
	public void addShip(int length, int startX, int startY, int orient) {
		int x = startX;
		int y = startY;
		
		String texturePath = "texture/l"+ length +"/";
		
		int c = 1;
		
		ImageIcon icon = null;
		
		
		Color shipColor = shipColors.remove(0);
		if (orient == Horizontal){
			for (; x < startX + length; x++){
				JButton b = playersIcons.get(0).get(getCoordinate(x, y));
				b.setBackground(shipColor);
				icon = new ImageIcon(texturePath + "p"+c+"h.png");
				c++;
				icon = new ImageIcon(icon.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
				b.setIcon(icon);
				b.setDisabledIcon(icon);
			}
		} else {
			for (; y < startY + length; y++){
				JButton b = playersIcons.get(0).get(getCoordinate(x, y));
				b.setBackground(shipColor);
				icon = new ImageIcon(texturePath + "p"+c+".png");
				c++;
				icon = new ImageIcon(icon.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
				b.setIcon(icon);
				b.setDisabledIcon(icon);
			}
		}
	}
	
	private int getCoordinate (int x, int y) {
		return y*col+x;
	}
	
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
			playersIcons.get(iD).get(getCoordinate(x, y)).setBackground(c);
		}
	}
}
