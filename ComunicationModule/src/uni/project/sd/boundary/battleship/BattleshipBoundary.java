package uni.project.sd.boundary.battleship;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import uni.project.sd.Control.battleship.BattleshipController;
import uni.project.sd.Entity.battleship.Ship;
import uni.project.sd.boundary.battleship.listener.AutoPlayListener;
import uni.project.sd.boundary.battleship.listener.BattleshipJButtonActionListener;
import uni.project.sd.boundary.battleship.listener.OrientationActionListener;
import uni.project.sd.boundary.battleship.listener.RandomShipPositionActionListener;
import uni.project.sd.boundary.battleship.listener.ShipActionListener;
import uni.project.sd.comunications.ServerAddress;

public class BattleshipBoundary {
	public static final int Horizontal = 0;
	public static final int Vertical = 1;

	public static final Color Blue = new Color(0, 127, 255, 0);
	public static final Color LightBlue = new Color(41, 189, 217, 150);
	public static final Color Red = new Color(255, 0, 0, 180);
	public static final Color Yellow = new Color(255, 255, 0, 180);
	public static final Color Orange = new Color(255, 165, 0, 180);
	public static final Color Green = new Color(0, 255, 0, 180);
	public static final Color Magenta = new Color(255, 0, 255, 180);
	public static final Color White = new Color(255, 255, 255, 180);
	public static final Color Fog = new Color(128, 128, 128, 180);
	private int row;
	private int col;
	private JFrame mainWindow;
	private ArrayList<ArrayList<JButton>> playersIcons;
	private JPanel shipsPanel;

	private ArrayList<Color> shipColors = new ArrayList<>();

	ArrayList<JComponent> playerControls;

	private HashMap<Ship, JRadioButton> shipRadioButtons;

	private int lengthSelected = 0;
	private int orientationSelected = 0;
	private LinkedList<Dimension> highlightPositions = new LinkedList<>();
	private LinkedList<Color> oldColors = new LinkedList<>();

	public BattleshipBoundary(BattleshipController controller,
			Integer playerNumber, int d, ArrayList<Ship> ships) {

		this.playerControls = new ArrayList<>();

		mainWindow = new JFrame("Battleship");
		mainWindow.getContentPane().setLayout(new BorderLayout());
		JPanel gamePanel = new JPanel(new GridLayout(0, 2, 10, 10));
		JPanel optionPanel = new JPanel(new GridLayout(0, 1));

		optionPanel.setSize(250, 250 + 250 * ((playerNumber - 1) / 2));

		ButtonGroup orietationGroup = new ButtonGroup();

		generateShipsPanel(ships);
		JScrollPane shipsScroller = new JScrollPane(shipsPanel);
		shipsScroller
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		optionPanel.add(shipsScroller);
		JPanel tempPanel = new JPanel(new GridLayout(0, 1));
		JPanel orietPanel = new JPanel(new GridLayout(6, 1));

		JRadioButton h = new JRadioButton("Horizontal", true);
		JRadioButton v = new JRadioButton("Vertical");
		v.addActionListener(new OrientationActionListener(
				BattleshipBoundary.Vertical));
		h.addActionListener(new OrientationActionListener(
				BattleshipBoundary.Horizontal));
		this.playerControls.add(v);
		this.playerControls.add(h);
		orietationGroup.add(v);
		orietationGroup.add(h);
		orietPanel.add(h);
		orietPanel.add(v);

		tempPanel.add(orietPanel);

		JCheckBox autoPlay = new JCheckBox("Auto play");
		autoPlay.addItemListener(new AutoPlayListener(controller));
		JButton setShipRandom = new JButton("Random positions");
		setShipRandom.addActionListener(new RandomShipPositionActionListener());
		this.playerControls.add(setShipRandom);
		tempPanel.add(autoPlay);
		tempPanel.add(setShipRandom);

		optionPanel.add(tempPanel);

		this.row = col = d;

		Color[] colors = { Orange, Green, Magenta, White };
		for (Color c : colors) {
			shipColors.add(c);
		}

		playersIcons = new ArrayList<ArrayList<JButton>>(playerNumber);

		JPanel playerPanel = null;
		JPanel playerHome = null;
		ArrayList<JButton> buttons = null;
		JButton singleButton = null;
		ServerAddress address = ServerAddress.getInstance();

		String background = "/texture/sea" + (new Random().nextInt(4) + 1)
				+ ".png";

		for (int k = 0; k < playerNumber; k++) {
			playerHome = new JPanel(new BorderLayout());
			if (k > 0)
				playerHome.add(new JLabel(address.getServer(k - 1) + "@"
						+ address.getLocation(address.getServer(k - 1))),
						BorderLayout.PAGE_START);
			else
				playerHome.add(new JLabel(address.getMyAddress()),
						BorderLayout.PAGE_START);
			GridLayout layout = new GridLayout(row, col);
			playerPanel = new JPanelBackground(background, layout);
			((GridLayout) playerPanel.getLayout()).setVgap(0);
			playerHome.add(playerPanel, BorderLayout.CENTER);
			buttons = new ArrayList<JButton>(col * row);

			for (int r = 0; r < row; r++) {
				for (int c = 0; c < col; c++) {
					singleButton = new JButtonBackground();
					singleButton.setSize(5, 5);
					singleButton.setOpaque(false);
					singleButton.setBackground(Blue);
					singleButton.setContentAreaFilled(false);
					singleButton
							.addActionListener(new BattleshipJButtonActionListener(
									controller, c, r, k));
					if (k == 0)
						singleButton.addMouseListener(new GridMouseAdapter(c,
								r, this));
					buttons.add(singleButton);
					playerPanel.add(singleButton);
				}
			}
			gamePanel.add(playerHome);
			playersIcons.add(buttons);
		}

		gamePanel.setSize(500, 250 + 250 * ((playerNumber - 1) / 2));

		mainWindow.add(optionPanel, BorderLayout.LINE_START);
		mainWindow.add(gamePanel, BorderLayout.CENTER);

		mainWindow.setResizable(false);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.pack();
		mainWindow.setSize(850, 400 + 400 * ((playerNumber - 1) / 2));

		mainWindow.setVisible(true);

	}

	public void addShip(int length, int startX, int startY, int orient) {
		int x = startX;
		int y = startY;

		String texturePath = "/texture/l" + length + "/";

		int c = 1;

		Color shipColor = shipColors.get(length - 2);
		if (orient == Horizontal) {
			for (; x < startX + length; x++, c++) {
				JButton b = playersIcons.get(0).get(getCoordinate(x, y));
				b.setBackground(shipColor);
				addIcon(b,
						getClass().getResource(texturePath + "p" + c + "h.png"));
			}
		} else {
			for (; y < startY + length; y++, c++) {
				JButton b = playersIcons.get(0).get(getCoordinate(x, y));
				b.setBackground(shipColor);
				addIcon(b,
						getClass().getResource(texturePath + "p" + c + ".png"));
			}
		}
	}

	private void addIcon(JButton button, URL iconPosition) {
		ImageIcon icon = new ImageIcon(iconPosition);
		icon = new ImageIcon(icon.getImage().getScaledInstance(
				button.getWidth(), button.getHeight(),
				java.awt.Image.SCALE_SMOOTH));
		button.setIcon(icon);
		button.setDisabledIcon(icon);
	}

	public void showEnemyShip(int length, int startX, int startY, int orient,
			int playerN) {
		int x = startX;
		int y = startY;

		playerN++;
		System.out.println("Il giocatore è il numero: " + playerN);

		String texturePath = "/texture/l" + length + "/";

		int c = 1;

		if (orient == Horizontal) {
			for (; x < startX + length; x++, c++) {
				JButton b = playersIcons.get(playerN).get(getCoordinate(x, y));
				addIcon(b,
						getClass().getResource(texturePath + "p" + c + "h.png"));
			}
		} else {
			for (; y < startY + length; y++, c++) {
				JButton b = playersIcons.get(playerN).get(getCoordinate(x, y));
				addIcon(b,
						getClass().getResource(texturePath + "p" + c + ".png"));
			}
		}
	}

	private int getCoordinate(int x, int y) {
		return y * col + x;
	}

	public void setPlayerButtonEnabled(boolean enabled) {
		synchronized (playersIcons) {
			enablePlayer(0, enabled);
		}
		for (JComponent component : this.playerControls)
			component.setEnabled(enabled);
	}

	public void setButtonEnabled(boolean enabled) {
		synchronized (playersIcons) {
			for (int k = 1; k < playersIcons.size(); k++) {
				enablePlayer(k, enabled);
			}
		}
	}

	private void enablePlayer(int player, boolean enabled) {
		for (JButton b : playersIcons.get(player))
			if (enabled) {
				if (b.getBackground().equals(Blue)) {
					b.setEnabled(enabled);
					b.setBackground(LightBlue);
				}
			} else {
				if (b.getBackground().equals(LightBlue)) {
					b.setBackground(Blue);
				}
				b.setEnabled(false);
			}
	}

	public void disablePlayer(int k) {
		synchronized (playersIcons) {
			for (JButton b : playersIcons.get(k + 1)) {
				b.setBackground(Fog);
				b.setDisabledIcon(null);
				b.setEnabled(false);
			}
		}
	}

	public void setValue(int iD, int x, int y, boolean result) {
		Color c = Yellow;

		if (result)
			c = Red;
		setColor(iD, x, y, c);

	}

	public void setColor(int iD, int x, int y, Color c) {
		synchronized (playersIcons) {
				playersIcons.get(iD).get(getCoordinate(x, y)).setBackground(c);
				playersIcons.get(iD).get(getCoordinate(x, y)).setEnabled(false);
			
		}
	}

	public void disableShip(Ship s) {
		this.lengthSelected = 0;
		this.shipRadioButtons.get(s).setSelected(false);
		this.shipRadioButtons.get(s).setEnabled(false);
	}

	public void showAlert(String message, int i) {
		JOptionPane.showMessageDialog(null, message);
	}

	public void showAlert(String message) {
		JOptionPane.showMessageDialog(null, message);
	}

	public void setLengthSelected(int lengthSelected) {
		this.lengthSelected = lengthSelected;
	}

	public void setOrientationSelected(int orientationSelected) {
		this.orientationSelected = orientationSelected;
	}

	public void addHighlight(int x, int y) {
		if (lengthSelected > 0) {
			synchronized (playersIcons) {
				try {
					for (int i = 0; i < this.lengthSelected; i++) {
						if (x >= this.col)
							break;
						this.oldColors.add(this.playersIcons.get(0)
								.get(getCoordinate(x, y)).getBackground());
						this.highlightPositions.add(new Dimension(x, y));
						if (this.oldColors.getLast().equals(LightBlue))
							this.playersIcons.get(0).get(getCoordinate(x, y))
									.setBackground(Green);
						else
							this.playersIcons.get(0).get(getCoordinate(x, y))
									.setBackground(Red);
						if (this.orientationSelected == Horizontal)
							x++;
						else
							y++;
					}
				} catch (IndexOutOfBoundsException e) {

				}
			}
		}
	}

	public void removeHighlight() {
		if (!highlightPositions.isEmpty()) {
			synchronized (playersIcons) {
				int i = highlightPositions.size();
				for (; i > 0; i--) {
					Dimension position = highlightPositions.removeFirst();
					Color oldColor = this.oldColors.removeFirst();
					this.playersIcons
							.get(0)
							.get(getCoordinate(position.width, position.height))
							.setBackground(oldColor);
				}
			}
		}
	}

	private void generateShipsPanel(ArrayList<Ship> ships) {
		ButtonGroup shipGroup = new ShipsButtonGroup();

		if (!(this.shipRadioButtons == null)) {
			Set<Ship> shipI = this.shipRadioButtons.keySet();
			for (Ship s : shipI) {
				JRadioButton component = this.shipRadioButtons.get(s);
				this.shipsPanel.remove(component);
				this.playerControls.remove(component);
			}
			this.shipRadioButtons.clear();
		} else {
			shipsPanel = new JPanel(new GridLayout(0, 1));
			this.shipRadioButtons = new HashMap<>(ships.size());
		}

		int width = this.shipsPanel.getVisibleRect().width;
		if (width == 0)
			width = 100;
		int heigth = width / 5;

		for (Ship s : ships) {
			int w = width * s.getLength() / 5;
			ImageIcon icon = new ImageIcon(getClass().getResource(
					"/texture/l" + s.getLength() + "/all.png"));
			icon = new ImageIcon(icon.getImage().getScaledInstance(w, heigth,
					java.awt.Image.SCALE_SMOOTH));
			ImageIcon iconSelected = new ImageIcon(getClass().getResource(
					"/texture/l" + s.getLength() + "/allSelected.png"));
			iconSelected = new ImageIcon(iconSelected.getImage()
					.getScaledInstance(w, heigth, java.awt.Image.SCALE_SMOOTH));
			JRadioButton ss = new JRadioButton();
			ss.setIcon(icon);
			ss.setSelectedIcon(iconSelected);
			ss.addItemListener(new ShipActionListener(s));
			this.shipRadioButtons.put(s, ss);
			this.playerControls.add(ss);
			shipGroup.add(ss);
			shipsPanel.add(ss);
		}

		this.shipsPanel.setVisible(false);
		this.shipsPanel.repaint(0);
		this.shipsPanel.setVisible(true);
	}

	public void setShips(ArrayList<Ship> ships) {
		generateShipsPanel(ships);
	}

}
