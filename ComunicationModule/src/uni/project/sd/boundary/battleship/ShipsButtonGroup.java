package uni.project.sd.boundary.battleship;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;

public class ShipsButtonGroup extends ButtonGroup {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5444952262117096458L;

	@Override
	public void setSelected(ButtonModel m, boolean b) {
		if(b){
			super.setSelected(m, b);
		} else {
			clearSelection();
		}
	}

}
