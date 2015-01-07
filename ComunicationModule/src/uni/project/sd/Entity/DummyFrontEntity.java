package uni.project.sd.Entity;

import java.util.LinkedList;

import uni.project.sd.boundary.FrontBoundary;

public class DummyFrontEntity {
	private static DummyFrontEntity entity = null;
	private LinkedList<FrontBoundary> fronts;
	private boolean isPlayerTurn = false;
	
	private Object lockPlayerTurn = new Object();
	private Object lockViewList = new Object();
	
	public synchronized static DummyFrontEntity getInstance() {
		if(entity == null)
			entity = new DummyFrontEntity();
		return entity;
	}

	public void addView(FrontBoundary dummyFront) {
		synchronized (lockViewList) {
			if(fronts == null)
				fronts =new LinkedList<>();
			fronts.add(dummyFront);
		}
		synchronized (lockPlayerTurn) {
			dummyFront.setButtonEnabled(isPlayerTurn);
		}
		dummyFront.addToLog("View initialized");
	}
	
	public void addMessage(String message) {
		synchronized (lockViewList) {
			if(fronts!=null)
				for(FrontBoundary df: fronts) {
					df.addToLog(message);
				}
		}
	}
	
	public void setPlayerTurn(boolean canPlay) {
		synchronized (lockPlayerTurn) {
			this.isPlayerTurn = canPlay;
		}
		synchronized (lockViewList) {
			for(FrontBoundary df: fronts) {
				df.setButtonEnabled(canPlay);
			}
		}
	}
}
