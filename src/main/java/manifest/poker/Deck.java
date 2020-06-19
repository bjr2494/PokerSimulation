package manifest.poker;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Deck {

	private List<Card> deckOfCards;
	
	public Deck() {
		deckOfCards = new ArrayList<>();
		
		for (int value = 2; value <= 14; value++) {
			for (int suit = 1; suit <= 4; suit++) {
				Card card = new Card(value, suit);
				deckOfCards.add(card);
			}
		}
	}
	
	public List<Card> getDeckOfCards() {
		return deckOfCards;
	}
}
