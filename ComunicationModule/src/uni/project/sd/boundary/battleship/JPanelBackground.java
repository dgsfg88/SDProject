package uni.project.sd.boundary.battleship;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class JPanelBackground extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8230462268543000112L;
	private Image image = null;
	
	public JPanelBackground(String filename, LayoutManager layout) {
		super(layout);
		this.image = new ImageIcon(getClass().getResource(filename)).getImage();
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, image.getWidth(null), image.getHeight(null),
				null);
	}
}
