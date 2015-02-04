package uni.project.sd.Entity;

import java.util.LinkedList;

import uni.project.sd.boundary.FrontBoundary;
import uni.project.sd.comunications.ServerAddress;
import uni.project.sd.comunications.entity.Token;

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
			if(canPlay){
				ServerAddress.getInstance().incrementTokenLap();
			}
		}
		synchronized (lockViewList) {
			for(FrontBoundary df: fronts) {
				df.setButtonEnabled(canPlay);
			}
		}
	}

	public void destroyPlayer(int serverNID) {
		synchronized (lockViewList) {
			for(FrontBoundary df: fronts) {
				df.disablePlayer(serverNID);
			}
		}
	}

	public Token getToken() {
		synchronized (lockViewList) {
			return fronts.getFirst().getToken();
		}
	}

	public void setToken(Token token) {
		synchronized (lockViewList) {
			for(FrontBoundary frontBoundary: fronts){
				frontBoundary.setToken(token);
			}
		}
	}
}
