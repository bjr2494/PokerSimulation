package manifest.poker;

public class Card {
	
	private String cardValue;
	private String cardSuit;
	
	public Card(int value, int suit) {
		switch (value) {
			case 2: this.cardValue = "2";  break;
			case 3: this.cardValue = "3";  break;
			case 4: this.cardValue = "4";  break;
			case 5: this.cardValue = "5";  break;
			case 6: this.cardValue = "6";  break;
			case 7: this.cardValue = "7";  break;
			case 8: this.cardValue = "8";  break;
			case 9: this.cardValue = "9";  break;
			case 10: this.cardValue = "T";  break;
			case 11: this.cardValue = "J";  break;
			case 12: this.cardValue = "Q";  break;
			case 13: this.cardValue = "K";  break;
			case 14: this.cardValue = "A"; break;
		}
		
		switch (suit) {
			case 1: this.cardSuit = "C"; break;
			case 2: this.cardSuit = "D"; break;
			case 3: this.cardSuit = "H"; break;
			case 4: this.cardSuit = "S"; break;
		}
	}
	
	public String getCardValue() {
		return cardValue;
	}

	public void setCardValue(String cardValue) {
		this.cardValue = cardValue;
	}
	
	public String getCardSuit() {
		return cardSuit;
	}
	
	public void setCardSuit(String cardSuit) {
		this.cardSuit = cardSuit;
	}

	@Override
	public String toString() {
		return cardValue + cardSuit;
	}
	
	
	
	
	
	
	
	
}
