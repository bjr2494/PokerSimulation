package manifest.poker;

import java.util.List;

public class Round {
	
	private List<Hand> handList;
	private int qualityCheck;

	public List<Hand> getHandList() {
		return handList;
	}
	public void setHandList(List<Hand> handList) {
		this.handList = handList;
	}
	public int getQualityCheck() {
		return qualityCheck;
	}
	public void setQualityCheck(int qualityCheck) {
		this.qualityCheck = qualityCheck;
	}

}
