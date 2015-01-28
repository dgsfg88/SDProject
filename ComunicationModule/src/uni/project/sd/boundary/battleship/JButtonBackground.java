package uni.project.sd.boundary.battleship;

import java.awt.Graphics;

import javax.swing.JButton;

public class JButtonBackground extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7269217186816066123L;

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(this.getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());

		super.paintComponent(g);
	}

}
