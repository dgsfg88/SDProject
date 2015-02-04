package uni.project.sd.boundary;

import uni.project.sd.comunications.entity.Token;

public interface FrontBoundary {
	public void addToLog(String Message);
	public void setButtonEnabled(boolean enabled);
	void disablePlayer(int k);
	public Token getToken();
	public void setToken(Token token);
}
