package manifest.poker;

import java.util.List;

public class Hand {

	private int id;
	private List<Card> cards;
	private List<Integer> frequencyList;
	private String greatestValue;
	//number-type list of the hand's card values
	private List<Integer> intCardValues;
	private List<String> stringCardSuits;
	private boolean hasPair;
	private String pairValue;
	private boolean hasTwoPairs;
	private List<String> pairNumberList;
	private String higherPairValue;
	private String lowerPairValue;
	private boolean hasThreeOfKind;
	private String threeOfKindNumber;
	private boolean hasStraight;
	private List<String> straightValues;
	private boolean hasFlush;
	private String flushSuit;
	private boolean hasFullHouse;
	private boolean hasFourOfKind;
	private String fourOfKindNumber;
	private boolean hasStraightFlush;
	private String straightFlushSuit;
	private int quality;
	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	
	public List<Card> getCards() {
		return cards;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public List<String> getPairNumberList() {
		return pairNumberList;
	}

	public void setPairNumberList(List<String> pairNumberList) {
		this.pairNumberList = pairNumberList;
	}

	public String getThreeOfKindNumber() {
		return threeOfKindNumber;
	}

	public void setThreeOfKindNumber(String threeOfKindNumber) {
		this.threeOfKindNumber = threeOfKindNumber;
	}

	public String getFourOfKindNumber() {
		return fourOfKindNumber;
	}

	public void setFourOfKindNumber(String fourOfKindNumber) {
		this.fourOfKindNumber = fourOfKindNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Integer> getIntCardValues() {
		return intCardValues;
	}

	public void setIntCardValues(List<Integer> intCardValues) {
		this.intCardValues = intCardValues;
	}

	public List<String> getStringCardSuits() {
		return stringCardSuits;
	}

	public void setStringCardSuits(List<String> stringCardSuits) {
		this.stringCardSuits = stringCardSuits;
	}

	public String getFlushSuit() {
		return flushSuit;
	}

	public void setFlushSuit(String flushSuit) {
		this.flushSuit = flushSuit;
	}

	public String getStraightFlushSuit() {
		return straightFlushSuit;
	}

	public void setStraightFlushSuit(String straightFlushSuit) {
		this.straightFlushSuit = straightFlushSuit;
	}

	public String getGreatestValue() {
		return greatestValue;
	}

	public void setGreatestValue(String greatestValue) {
		this.greatestValue = greatestValue;
	}

	public String getPairValue() {
		return pairValue;
	}

	public void setPairValue(String pairValue) {
		this.pairValue = pairValue;
	}

	public String getHigherPairValue() {
		return higherPairValue;
	}

	public void setHigherPairValue(String higherPairValue) {
		this.higherPairValue = higherPairValue;
	}

	public String getLowerPairValue() {
		return lowerPairValue;
	}

	public void setLowerPairValue(String lowerPairValue) {
		this.lowerPairValue = lowerPairValue;
	}

	public boolean isHasPair() {
		return hasPair;
	}

	public void setHasPair(boolean hasPair) {
		this.hasPair = hasPair;
	}

	public boolean isHasTwoPairs() {
		return hasTwoPairs;
	}

	public void setHasTwoPairs(boolean hasTwoPairs) {
		this.hasTwoPairs = hasTwoPairs;
	}

	public boolean isHasThreeOfKind() {
		return hasThreeOfKind;
	}

	public void setHasThreeOfKind(boolean hasThreeOfKind) {
		this.hasThreeOfKind = hasThreeOfKind;
	}

	public boolean isHasStraight() {
		return hasStraight;
	}

	public void setHasStraight(boolean hasStraight) {
		this.hasStraight = hasStraight;
	}

	public boolean isHasFlush() {
		return hasFlush;
	}

	public void setHasFlush(boolean hasFlush) {
		this.hasFlush = hasFlush;
	}

	public boolean isHasFullHouse() {
		return hasFullHouse;
	}

	public void setHasFullHouse(boolean hasFullHouse) {
		this.hasFullHouse = hasFullHouse;
	}

	public boolean isHasFourOfKind() {
		return hasFourOfKind;
	}

	public void setHasFourOfKind(boolean hasFourOfKind) {
		this.hasFourOfKind = hasFourOfKind;
	}

	public boolean isHasStraightFlush() {
		return hasStraightFlush;
	}

	public void setHasStraightFlush(boolean hasStraightFlush) {
		this.hasStraightFlush = hasStraightFlush;
	}

	public List<String> getStraightValues() {
		return straightValues;
	}

	public void setStraightValues(List<String> straightValues) {
		this.straightValues = straightValues;
	}

	public List<Integer> getFrequencyList() {
		return frequencyList;
	}

	public void setFrequencyList(List<Integer> frequencyList) {
		this.frequencyList = frequencyList;
	}
}
