package manifest.poker;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

public class Round {
	
	List<Hand> handList;
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
