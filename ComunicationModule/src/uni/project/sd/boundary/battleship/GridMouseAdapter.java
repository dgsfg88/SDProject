package uni.project.sd.boundary.battleship;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GridMouseAdapter extends MouseAdapter {
	
	private int x,y;
	private BattleshipBoundary myBoundary;
	
	public GridMouseAdapter(int x, int y, BattleshipBoundary boundary) {
		this.x = x;
		this.y = y;
		
		this.myBoundary = boundary;
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		this.myBoundary.addHighlight(x,y);
		super.mouseEntered(e);
	}
	@Override
	public void mouseExited(MouseEvent e) {
		this.myBoundary.removeHighlight();
		super.mouseExited(e);
	}
	@Override
	public void mousePressed(MouseEvent e) {
		this.myBoundary.removeHighlight();
		super.mousePressed(e);
	}
}
