package manifest.poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Poker {

	public static void main(String[] args) {
		playRound();
	}

	private static void playRound() {
		Round round = new Round();
		round.setHandList(new ArrayList<>());
		Deck currentDeck = new Deck();

		// fetching list of cards from deck object
		List<Card> deckCards = currentDeck.getDeckOfCards();

		// shuffling the cards
		Collections.shuffle(deckCards);

		// set that will hold the cards for the round (five for each player)
		Set<Card> tenCards = new HashSet<>();

		Random rand = new Random();

		for (int i = 0; i < deckCards.size(); i++) {
			int randomIndex = rand.nextInt(deckCards.size());
			Card randomCard = deckCards.get(randomIndex);
			// addition will of course be rejected if it's a duplicate
			tenCards.add(randomCard);
			if (tenCards.size() == 10) {
				break;
			}
		}

		// transferring the contents of the set to this new list so as to easily divide
		// the ten cards between the two hands
		List<Card> roundCards = new ArrayList<>();
		for (Card c : tenCards) {
			roundCards.add(c);
		}

		/*
		 * List<Card> blackList = new ArrayList<>(); Card blackCardOne = new Card(6, 1);
		 * blackList.add(blackCardOne); Card blackCardTwo = new Card(6, 2);
		 * blackList.add(blackCardTwo); Card blackCardThree = new Card(3, 4);
		 * blackList.add(blackCardThree); Card blackCardFour = new Card(3, 1);
		 * blackList.add(blackCardFour); Card blackCardFive = new Card(2, 2);
		 * blackList.add(blackCardFive);
		 * 
		 * List<Card> whiteList = new ArrayList<>(); Card whiteCardOne = new Card(7, 3);
		 * whiteList.add(whiteCardOne); Card whiteCardTwo = new Card(3, 4);
		 * whiteList.add(whiteCardTwo); Card whiteCardThree = new Card(5, 2);
		 * whiteList.add(whiteCardThree); Card whiteCardFour = new Card(4, 3);
		 * whiteList.add(whiteCardFour); Card whiteCardFive = new Card(9, 2);
		 * whiteList.add(whiteCardFive);
		 */

		List<Card> blackList = new ArrayList<>();
		List<Card> whiteList = new ArrayList<>();

		// dealing the cards one by one to the two players
		blackList.add(roundCards.get(0));
		blackList.add(roundCards.get(2));
		blackList.add(roundCards.get(4));
		blackList.add(roundCards.get(6));
		blackList.add(roundCards.get(8));

		whiteList.add(roundCards.get(1));
		whiteList.add(roundCards.get(3));
		whiteList.add(roundCards.get(5));
		whiteList.add(roundCards.get(7));
		whiteList.add(roundCards.get(9));

		// making the two hands
		Hand handBlack = new Hand();
		handBlack.setId(1);
		Hand handWhite = new Hand();
		handWhite.setId(2);

		// default is that no special category is at 1; the value is increased according
		// to the level that it reaches
		handBlack.setQuality(1);
		handWhite.setQuality(1);

		handBlack.setCards(blackList);
		handWhite.setCards(whiteList);

		round.getHandList().add(handBlack);
		round.getHandList().add(handWhite);

		int qualityCheck = determineHandQuality(round);

		if (qualityCheck > 0) {
			System.out.println(fetchResult(handBlack));
		}

		if (qualityCheck < 0) {
			System.out.println(fetchResult(handWhite));
		}

		if (qualityCheck == 0) {
			System.out.println(fetchResult(handBlack, handWhite));
		}
	}

	private static int determineHandQuality(Round round) {

		for (Hand givenHand : round.getHandList()) {
			// list of integers that will be sorted
			List<Integer> intCardValues = integerListCardValues();

			// list of strings for the suit of each card
			List<String> stringCardSuits = stringListCardSuits();

			for (Card c : givenHand.getCards()) {
				// System.out.println(c);
				forNumberComparison(c);
				intCardValues.add(Integer.parseInt(c.getCardValue()));
				stringCardSuits.add(c.getCardSuit());
				backToNormal(c);
			}

			printHand(givenHand);
			Collections.sort(intCardValues);
			givenHand.setIntCardValues(intCardValues);
			givenHand.setStringCardSuits(stringCardSuits);
			givenHand.setPairNumberList(new ArrayList<>());

			givenHand.setGreatestValue(Integer.toString(intCardValues.get(4)));

			Map<Integer, Integer> cardValueAndFrequencyMap = new HashMap<>();

			for (int cv : givenHand.getIntCardValues()) {
				int frequency = Collections.frequency(givenHand.getIntCardValues(), cv);
				switch (frequency) {
				case 2:
					if (!cardValueAndFrequencyMap.containsKey(cv)) {
						cardValueAndFrequencyMap.put(cv, frequency);
						if (givenHand.isHasPair() == false) {
							givenHand.setHasPair(true);
							givenHand.setQuality(2);
							givenHand.getPairNumberList().add(Integer.toString(cv));

							break;
						}
						if (givenHand.isHasPair() == true) {
							givenHand.setHasTwoPairs(true);
							givenHand.setQuality(3);
							givenHand.getPairNumberList().add(Integer.toString(cv));
						}
					}
					if (cardValueAndFrequencyMap.containsValue(3)) {
						givenHand.setHasFullHouse(true);
						givenHand.setQuality(7);
						givenHand.getPairNumberList().add(Integer.toString(cv));
					}

					break;
				case 3:
					if (!cardValueAndFrequencyMap.containsKey(cv)) {
						cardValueAndFrequencyMap.put(cv, frequency);
						if (cardValueAndFrequencyMap.containsValue(2)) {
							givenHand.setHasThreeOfKind(true);
							givenHand.setHasFullHouse(true);
							givenHand.setQuality(7);
							givenHand.setThreeOfKindNumber(Integer.toString(cv));
						}
						if (!cardValueAndFrequencyMap.containsValue(2)) {
							givenHand.setHasThreeOfKind(true);
							givenHand.setQuality(4);
							givenHand.setThreeOfKindNumber(Integer.toString(cv));
						}
					}
					break;
				case 4:
					givenHand.setHasFourOfKind(true);
					givenHand.setQuality(8);
					givenHand.setFourOfKindNumber(Integer.toString(cv));
					break;
				}
			}

			if (givenHand.getIntCardValues().get(4) - givenHand.getIntCardValues().get(3) == 1
					&& givenHand.getIntCardValues().get(3) - givenHand.getIntCardValues().get(2) == 1
					&& givenHand.getIntCardValues().get(2) - givenHand.getIntCardValues().get(1) == 1
					&& givenHand.getIntCardValues().get(1) - givenHand.getIntCardValues().get(0) == 1) {
				givenHand.setHasStraight(true);
				givenHand.setQuality(5);
			}

			for (String cs : givenHand.getStringCardSuits()) {
				int suitFrequency = Collections.frequency(givenHand.getStringCardSuits(), cs);
				switch (suitFrequency) {
				case 5:
					if (givenHand.isHasStraight() == true) {
						givenHand.setHasStraightFlush(true);
						givenHand.setStraightFlushSuit(cs);
						givenHand.setQuality(9);

					} else {
						givenHand.setHasFlush(true);
						givenHand.setQuality(6);
						givenHand.setFlushSuit(cs);
					}
				}
			}
		}
		return round.getHandList().get(0).getQuality() - round.getHandList().get(1).getQuality();
	}

	private static List<String> stringListCardSuits() {
		List<String> cardSuits = new ArrayList<>();
		return cardSuits;
	}

	private static List<Integer> integerListCardValues() {
		List<Integer> cardValues = new ArrayList<>();
		return cardValues;
	}

	private static void forNumberComparison(Card c) {
		switch (c.getCardValue()) {
		case "T":
			c.setCardValue("10");
			break;
		case "J":
			c.setCardValue("11");
			break;
		case "Q":
			c.setCardValue("12");
			break;
		case "K":
			c.setCardValue("13");
			break;
		case "A":
			c.setCardValue("14");
			break;
		}
	}

	private static void backToNormal(Card c) {
		switch (c.getCardValue()) {
		case "10":
			c.setCardValue("T");
			break;
		case "11":
			c.setCardValue("J");
			break;
		case "12":
			c.setCardValue("Q");
			break;
		case "13":
			c.setCardValue("K");
			break;
		case "14":
			c.setCardValue("A");
			break;
		}
	}

	private static void printHand(Hand givenHand) {
		String playerName = "";
		if (givenHand.getId() == 1) {
			playerName = "Black: ";
		}

		else if (givenHand.getId() == 2) {
			playerName = "White: ";
		}
		System.out.println(playerName + givenHand.getCards().get(0) + " " + givenHand.getCards().get(1) + " "
				+ givenHand.getCards().get(2) + " " + givenHand.getCards().get(3) + " " + givenHand.getCards().get(4));
		System.out.println();
	}

	private static String fetchResult(Hand givenHand) {
		String winner = "";
		if (givenHand.getId() == 1) {
			winner = "Black wins - ";
		} else if (givenHand.getId() == 2) {
			winner = "White wins - ";
		}
		String result = "";

		switch (givenHand.getQuality()) {
		case 2:
			switchToNormalCardValues(givenHand);
			result = winner + " Pair of " + givenHand.getPairNumberList().get(0);
			break;
		case 3:
			switchToNormalCardValues(givenHand);
			result = winner + " Two Pairs, of " + givenHand.getPairNumberList().get(0) + " and "
					+ givenHand.getPairNumberList().get(1);
			break;
		case 4:
			switchToNormalCardValues(givenHand);
			result = winner + " Three of a Kind, on " + givenHand.getThreeOfKindNumber();
			break;
		case 5:

			makeListForValuesInStraightOrStraightFlush(givenHand);

			switchToNormalCardValues(givenHand);
			result = winner + " Straight, " + givenHand.getStraightValues().get(0) + " through "
					+ givenHand.getStraightValues().get(4);
			break;
		case 6:
			switchToNormalCardValues(givenHand);
			result = winner + " Flush, the suit being " + givenHand.getFlushSuit();
			break;
		case 7:
			switchToNormalCardValues(givenHand);
			result = winner + " Full House: " + givenHand.getThreeOfKindNumber() + " over "
					+ givenHand.getPairNumberList().get(0);
			break;
		case 8:
			switchToNormalCardValues(givenHand);
			result = winner + " Four of a Kind, on " + givenHand.getFourOfKindNumber();
			break;
		case 9:
			makeListForValuesInStraightOrStraightFlush(givenHand);
			switchToNormalCardValues(givenHand);
			result = winner + " Straight Flush, " + givenHand.getStraightValues().get(0) + " through "
					+ givenHand.getStraightValues().get(4) + ", the suit being " + givenHand.getStraightFlushSuit();
			break;
		}
		return result;
	}

	private static void makeListForValuesInStraightOrStraightFlush(Hand givenHand) {

		givenHand.setStraightValues(new ArrayList<>());
		for (int i : givenHand.getIntCardValues()) {
			String intValue = Integer.toString(i);
			givenHand.getStraightValues().add(intValue);
		}
	}

	private static String fetchResult(Hand handBlack, Hand handWhite) {
		String result = "";

		int handSum = handBlack.getQuality() + handWhite.getQuality();
		switch (handSum) {
		// high card vs high card
		case 2:

			result = overlyLongChainOfIfStatements(handBlack, handWhite, result);

			break;
		// pair vs pair
		case 4:

			int blackPairValue = Integer.parseInt(handBlack.getPairNumberList().get(0));
			int whitePairValue = Integer.parseInt(handWhite.getPairNumberList().get(0));

			handBlack.setPairValue(handBlack.getPairNumberList().get(0));
			handWhite.setPairValue(handWhite.getPairNumberList().get(0));

			switchToNormalCardValues(handBlack);
			switchToNormalCardValues(handWhite);

			if (blackPairValue > whitePairValue) {
				result = "Black wins - Pair of " + handBlack.getPairValue() + " over White's Pair of "
						+ handWhite.getPairValue();
			}

			if (blackPairValue < whitePairValue) {
				result = "White wins - Pair of " + handWhite.getPairValue() + " over Black's Pair of "
						+ handBlack.getPairValue();
			}

			List<Integer> handBlackListWithoutPair = new ArrayList<>();
			List<Integer> handWhiteListWithoutPair = new ArrayList<>();
			if (blackPairValue == whitePairValue) {
				for (int i : handBlack.getIntCardValues()) {
					if (i != blackPairValue) {
						handBlackListWithoutPair.add(i);
					}
				}
				for (int j : handWhite.getIntCardValues()) {
					if (j != whitePairValue) {
						handWhiteListWithoutPair.add(j);
					}
				}
				// Collections.sort(handBlack.getIntCardValues());
				// Collections.sort(handWhite.getIntCardValues());

				if (handBlackListWithoutPair.get(2) > handWhiteListWithoutPair.get(2)) {
					handBlack.setGreatestValue(Integer.toString(handBlackListWithoutPair.get(2)));
					switchToNormalCardValues(handBlack);
					result = "Black wins - High Card of " + handBlack.getGreatestValue() + " after a pair of "
							+ handBlack.getPairValue() + " from each player";
				}
				if (handBlackListWithoutPair.get(2) < handWhiteListWithoutPair.get(2)) {
					handWhite.setGreatestValue(Integer.toString(handWhiteListWithoutPair.get(2)));
					switchToNormalCardValues(handWhite);
					result = "White wins - High Card of " + handWhite.getGreatestValue() + " after a pair of "
							+ handWhite.getPairValue() + " from each player";
				}
				if (handBlackListWithoutPair.get(2) == handWhiteListWithoutPair.get(2)) {
					if (handBlackListWithoutPair.get(1) > handWhiteListWithoutPair.get(1)) {
						handBlack.setGreatestValue(Integer.toString(handBlackListWithoutPair.get(1)));
						switchToNormalCardValues(handBlack);
						result = "Black wins - Second Highest Card of " + handBlack.getGreatestValue()
								+ " after a pair of " + handBlack.getPairValue() + " from each player";
					}
					if (handBlackListWithoutPair.get(1) < handWhiteListWithoutPair.get(1)) {
						handWhite.setGreatestValue(Integer.toString(handWhiteListWithoutPair.get(1)));
						switchToNormalCardValues(handWhite);
						result = "White wins - Second Highest Card of " + handWhite.getGreatestValue()
								+ " after a pair of " + handWhite.getPairValue() + " from each player";
					}
					if (handBlackListWithoutPair.get(1) == handWhiteListWithoutPair.get(1)) {
						if (handBlackListWithoutPair.get(0) > handBlackListWithoutPair.get(0)) {
							handBlack.setGreatestValue(Integer.toString(handBlackListWithoutPair.get(0)));
							switchToNormalCardValues(handBlack);
							result = "Black wins - Third Highest Card of " + handBlack.getGreatestValue()
									+ " after a pair of " + handBlack.getPairValue() + " from each player";
						}
						if (handBlackListWithoutPair.get(0) < handBlackListWithoutPair.get(0)) {
							handWhite.setGreatestValue(Integer.toString(handWhiteListWithoutPair.get(0)));
							switchToNormalCardValues(handWhite);
							result = "White wins - Third Highest Card of " + handWhite.getGreatestValue()
									+ " after a pair of " + handWhite.getPairValue() + " from each player";
						}
						if (handBlackListWithoutPair.get(0) == handBlackListWithoutPair.get(0)) {
							result = "Tie: the pairs, and the additional cards, are equal to one another";
						}
					}
				}
			}
			break;
		// two pairs vs two pairs
		case 6:
			int blackPairValueOne = Integer.parseInt(handBlack.getPairNumberList().get(0));
			int blackPairValueTwo = Integer.parseInt(handBlack.getPairNumberList().get(1));

			int higherPairBlack;
			int lowerPairBlack;

			if (blackPairValueOne > blackPairValueTwo) {
				handBlack.setHigherPairValue(Integer.toString(blackPairValueOne));
				handBlack.setLowerPairValue(Integer.toString(blackPairValueTwo));

				higherPairBlack = blackPairValueOne;
				lowerPairBlack = blackPairValueTwo;
			}

			else {
				handBlack.setHigherPairValue(Integer.toString(blackPairValueTwo));
				handBlack.setLowerPairValue(Integer.toString(blackPairValueOne));

				higherPairBlack = blackPairValueTwo;
				lowerPairBlack = blackPairValueOne;
			}

			switchToNormalCardValues(handBlack);

			int whitePairValueOne = Integer.parseInt(handWhite.getPairNumberList().get(0));
			int whitePairValueTwo = Integer.parseInt(handWhite.getPairNumberList().get(1));

			int higherPairWhite;
			int lowerPairWhite;

			if (whitePairValueOne > whitePairValueTwo) {
				handWhite.setHigherPairValue(Integer.toString(whitePairValueOne));
				handWhite.setLowerPairValue(Integer.toString(whitePairValueTwo));

				higherPairWhite = whitePairValueOne;
				lowerPairWhite = whitePairValueTwo;
			}

			else {
				handWhite.setHigherPairValue(Integer.toString(whitePairValueTwo));
				handWhite.setLowerPairValue(Integer.toString(whitePairValueOne));

				higherPairWhite = whitePairValueTwo;
				lowerPairWhite = whitePairValueOne;
			}

			switchToNormalCardValues(handWhite);

			if (higherPairBlack > higherPairWhite) {
				result = "Black wins - of its two pairs, its higher one of " + handBlack.getHigherPairValue()
						+ " beats white's higher pair of " + handWhite.getHigherPairValue();
			}

			if (higherPairBlack < higherPairWhite) {
				result = "White wins - of its two pairs, its higher one of " + handWhite.getHigherPairValue()
						+ " beats black's higher pair of " + handBlack.getHigherPairValue();
			}

			if (higherPairBlack == higherPairWhite) {
				if (lowerPairBlack > lowerPairWhite) {
					result = "Black wins - its higher pair equals that of white, but its lower one of "
							+ handBlack.getLowerPairValue() + " beats white's lower pair of "
							+ handWhite.getLowerPairValue();
				}

				if (lowerPairBlack < lowerPairWhite) {
					result = "White wins - its higher pair equals that of black, but its lower one of "
							+ handWhite.getLowerPairValue() + " beats black's lower pair of "
							+ handBlack.getLowerPairValue();
				}

				List<Integer> handBlackListWithoutTwoPairs = new ArrayList<>();
				List<Integer> handWhiteListWithoutTwoPairs = new ArrayList<>();
				if (lowerPairBlack == lowerPairWhite) {
					for (int i : handBlack.getIntCardValues()) {
						if (i != higherPairBlack || i != lowerPairBlack) {
							handBlackListWithoutTwoPairs.add(i);
						}
					}
					for (int j : handWhite.getIntCardValues()) {
						if (j != higherPairWhite || j != lowerPairWhite) {
							handWhiteListWithoutTwoPairs.add(j);
						}
					}

					if (handBlackListWithoutTwoPairs.get(0) > handWhiteListWithoutTwoPairs.get(0)) {
						handBlack.setGreatestValue(Integer.toString(handBlackListWithoutTwoPairs.get(0)));
						switchToNormalCardValues(handBlack);
						handWhite.setGreatestValue(Integer.toString(handWhiteListWithoutTwoPairs.get(0)));
						switchToNormalCardValues(handWhite);
						result = "Black wins - its two pairs happen to match those of white, but its lone other card, "
								+ handBlack.getGreatestValue() + ", beats that of white, which is "
								+ handWhite.getGreatestValue();
					}

					if (handBlackListWithoutTwoPairs.get(0) < handWhiteListWithoutTwoPairs.get(0)) {
						handBlack.setGreatestValue(Integer.toString(handBlackListWithoutTwoPairs.get(0)));
						switchToNormalCardValues(handBlack);
						handWhite.setGreatestValue(Integer.toString(handWhiteListWithoutTwoPairs.get(0)));
						switchToNormalCardValues(handWhite);
						result = "White wins - its two pairs happen to match those of black, but its lone other card, "
								+ handWhite.getGreatestValue() + ", beats that of black, which is "
								+ handBlack.getGreatestValue();
					}

					if (handBlackListWithoutTwoPairs.get(0) == handWhiteListWithoutTwoPairs.get(0)) {
						result = "Tie: both sets of pairs, and the additional cards, are equal to one another";
					}
				}
			}
			break;
		// 3 of kind vs 3 of kind
		case 8:
			int threeKindBlack = Integer.parseInt(handBlack.getThreeOfKindNumber());
			int threeKindWhite = Integer.parseInt(handWhite.getThreeOfKindNumber());

			switchToNormalCardValues(handBlack);
			switchToNormalCardValues(handWhite);

			if (threeKindBlack > threeKindWhite) {
				result = "Black wins - its three of a kind value, " + handBlack.getThreeOfKindNumber()
						+ ", is greater than that of white, which is " + handWhite.getThreeOfKindNumber();
			}

			if (threeKindBlack < threeKindWhite) {
				result = "White wins - its three of a kind value, " + handWhite.getThreeOfKindNumber()
						+ ", is greater than that of black, which is " + handBlack.getThreeOfKindNumber();
			}

			if (threeKindBlack == threeKindWhite) {
				result = "Tie: the threes of a kind are equal";
			}
			break;
		// straight vs straight
		case 10:
			int highestValueStraightBlack = handBlack.getIntCardValues().get(4);
			handBlack.setGreatestValue(Integer.toString(highestValueStraightBlack));
			switchToNormalCardValues(handBlack);

			int highestValueStraightWhite = handWhite.getIntCardValues().get(4);
			handWhite.setGreatestValue(Integer.toString(highestValueStraightWhite));
			switchToNormalCardValues(handWhite);

			if (highestValueStraightBlack > highestValueStraightWhite) {
				result = "Black wins - although each has a straight, black's highest value, "
						+ handBlack.getGreatestValue() + ", is higher than that of white, which is "
						+ handWhite.getGreatestValue();
			}

			if (highestValueStraightBlack < highestValueStraightWhite) {
				result = "White wins - although each has a straight, white's highest value, "
						+ handWhite.getGreatestValue() + ", is higher than that of black, which is "
						+ handBlack.getGreatestValue();
			}

			if (highestValueStraightBlack == highestValueStraightWhite) {
				result = "Tie: the straights are equal.";
			}
			break;
		// flush vs flush
		case 12:
			String degree = "";
			if (handBlack.getIntCardValues().get(4) > handWhite.getIntCardValues().get(4)) {
				handBlack.setGreatestValue(Integer.toString(handBlack.getIntCardValues().get(4)));
				switchToNormalCardValues(handBlack);
				handWhite.setGreatestValue(Integer.toString(handWhite.getIntCardValues().get(4)));
				switchToNormalCardValues(handWhite);
				degree = "highest";
				result = "Black wins - although each has a flush, its " + degree + " card, "
						+ handBlack.getGreatestValue() + ", is higher than that of white, which is "
						+ handWhite.getGreatestValue();
			}

			if (handBlack.getIntCardValues().get(4) < handWhite.getIntCardValues().get(4)) {
				handBlack.setGreatestValue(Integer.toString(handBlack.getIntCardValues().get(4)));
				switchToNormalCardValues(handBlack);
				handWhite.setGreatestValue(Integer.toString(handWhite.getIntCardValues().get(4)));
				switchToNormalCardValues(handWhite);
				degree = "highest";
				result = "White wins - although each has a flush, its " + degree + " card, "
						+ handWhite.getGreatestValue() + ", is higher than that of white, which is "
						+ handBlack.getGreatestValue();
			}

			if (handBlack.getIntCardValues().get(4) == handWhite.getIntCardValues().get(4)) {
				if (handBlack.getIntCardValues().get(3) > handWhite.getIntCardValues().get(3)) {
					handBlack.setGreatestValue(Integer.toString(handBlack.getIntCardValues().get(3)));
					switchToNormalCardValues(handBlack);
					handWhite.setGreatestValue(Integer.toString(handWhite.getIntCardValues().get(3)));
					switchToNormalCardValues(handWhite);
					degree = "second highest";
					result = "Black wins - although each has a flush, its " + degree + " card, "
							+ handBlack.getGreatestValue() + ", is higher than that of white, which is "
							+ handWhite.getGreatestValue();
				}
				if (handBlack.getIntCardValues().get(3) < handWhite.getIntCardValues().get(3)) {
					handBlack.setGreatestValue(Integer.toString(handBlack.getIntCardValues().get(3)));
					switchToNormalCardValues(handBlack);
					handWhite.setGreatestValue(Integer.toString(handWhite.getIntCardValues().get(3)));
					switchToNormalCardValues(handWhite);
					degree = "second highest";
					result = "White wins - although each has a flush, its " + degree + " card, "
							+ handWhite.getGreatestValue() + ", is higher than that of white, which is "
							+ handBlack.getGreatestValue();
				}
				if (handBlack.getIntCardValues().get(3) == handWhite.getIntCardValues().get(3)) {
					if (handBlack.getIntCardValues().get(2) > handWhite.getIntCardValues().get(2)) {
						handBlack.setGreatestValue(Integer.toString(handBlack.getIntCardValues().get(2)));
						switchToNormalCardValues(handBlack);
						handWhite.setGreatestValue(Integer.toString(handWhite.getIntCardValues().get(2)));
						switchToNormalCardValues(handWhite);
						degree = "third highest";
						result = "Black wins - although each has a flush, its " + degree + " card, "
								+ handBlack.getGreatestValue() + ", is higher than that of white, which is "
								+ handWhite.getGreatestValue();
					}

					if (handBlack.getIntCardValues().get(2) < handWhite.getIntCardValues().get(2)) {
						handBlack.setGreatestValue(Integer.toString(handBlack.getIntCardValues().get(2)));
						switchToNormalCardValues(handBlack);
						handWhite.setGreatestValue(Integer.toString(handWhite.getIntCardValues().get(2)));
						switchToNormalCardValues(handWhite);
						degree = "third highest";
						result = "White wins - although each has a flush, its " + degree + " card, "
								+ handWhite.getGreatestValue() + ", is higher than that of white, which is "
								+ handBlack.getGreatestValue();
					}

					if (handBlack.getIntCardValues().get(2) == handWhite.getIntCardValues().get(2)) {
						if (handBlack.getIntCardValues().get(1) > handWhite.getIntCardValues().get(1)) {
							handBlack.setGreatestValue(Integer.toString(handBlack.getIntCardValues().get(1)));
							switchToNormalCardValues(handBlack);
							handWhite.setGreatestValue(Integer.toString(handWhite.getIntCardValues().get(1)));
							switchToNormalCardValues(handWhite);
							degree = "fourth highest";
							result = "Black wins - although each has a flush, its " + degree + " card, "
									+ handBlack.getGreatestValue() + ", is higher than that of white, which is "
									+ handWhite.getGreatestValue();
						}

						if (handBlack.getIntCardValues().get(1) < handWhite.getIntCardValues().get(1)) {
							handBlack.setGreatestValue(Integer.toString(handBlack.getIntCardValues().get(1)));
							switchToNormalCardValues(handBlack);
							handWhite.setGreatestValue(Integer.toString(handWhite.getIntCardValues().get(1)));
							switchToNormalCardValues(handWhite);
							degree = "fourth highest";
							result = "White wins - although each has a flush, its " + degree + " card, "
									+ handWhite.getGreatestValue() + ", is higher than that of white, which is "
									+ handBlack.getGreatestValue();
						}
						if (handBlack.getIntCardValues().get(1) == handWhite.getIntCardValues().get(1)) {
							if (handBlack.getIntCardValues().get(0) > handWhite.getIntCardValues().get(0)) {
								handBlack.setGreatestValue(Integer.toString(handBlack.getIntCardValues().get(0)));
								switchToNormalCardValues(handBlack);
								handWhite.setGreatestValue(Integer.toString(handWhite.getIntCardValues().get(0)));
								switchToNormalCardValues(handWhite);
								degree = "fifth highest";
								result = "Black wins - although each has a flush, its " + degree + " card, "
										+ handBlack.getGreatestValue() + ", is higher than that of white, which is "
										+ handWhite.getGreatestValue();
							}
							if (handBlack.getIntCardValues().get(0) < handWhite.getIntCardValues().get(0)) {
								handBlack.setGreatestValue(Integer.toString(handBlack.getIntCardValues().get(0)));
								switchToNormalCardValues(handBlack);
								handWhite.setGreatestValue(Integer.toString(handWhite.getIntCardValues().get(0)));
								switchToNormalCardValues(handWhite);
								degree = "fifth highest";
								result = "White wins - although each has a flush, its " + degree + " card, "
										+ handWhite.getGreatestValue() + ", is higher than that of white, which is "
										+ handBlack.getGreatestValue();
							}
							if (handBlack.getIntCardValues().get(0) == handWhite.getIntCardValues().get(0)) {
								result = "Tie: the flushes are equal";
							}
						}
					}
				}
			}
			break;

		// full house vs full house
		case 14:
			int threeKindFullHouseBlack = Integer.parseInt(handBlack.getThreeOfKindNumber());
			handBlack.setGreatestValue(handBlack.getThreeOfKindNumber());
			switchToNormalCardValues(handBlack);

			int threeKindFullHouseWhite = Integer.parseInt(handWhite.getThreeOfKindNumber());
			handWhite.setGreatestValue(handWhite.getThreeOfKindNumber());
			switchToNormalCardValues(handWhite);

			if (threeKindFullHouseBlack > threeKindFullHouseWhite) {
				result = "Black wins - each has a full house, but the value of black's three of a kind, "
						+ handBlack.getGreatestValue() + ", is greater than that of white, which is "
						+ handWhite.getGreatestValue();
			}

			if (threeKindFullHouseBlack < threeKindFullHouseWhite) {
				result = "White wins - each has a full house, but the value of white's three of a kind, "
						+ handWhite.getGreatestValue() + ", is greater than that of black, which is "
						+ handBlack.getGreatestValue();
			}

			if (threeKindFullHouseBlack == threeKindFullHouseWhite) {
				result = "Tie: the threes of a kind in each full house are equal";
			}
			break;

		// four of kind vs four of kind
		case 16:
			int fourKindBlack = Integer.parseInt(handBlack.getFourOfKindNumber());
			handBlack.setGreatestValue(handBlack.getFourOfKindNumber());
			switchToNormalCardValues(handBlack);

			int fourKindWhite = Integer.parseInt(handWhite.getFourOfKindNumber());
			handWhite.setGreatestValue(handWhite.getFourOfKindNumber());
			switchToNormalCardValues(handWhite);

			if (fourKindBlack > fourKindWhite) {
				result = "Black wins - the value of its four of a kind, " + handBlack.getGreatestValue()
						+ ", is greater than that of white, which is " + handWhite.getGreatestValue();
			}

			if (fourKindBlack < fourKindWhite) {
				result = "White wins - the value of its four of a kind, " + handWhite.getGreatestValue()
						+ ", is greater than that of black, which is " + handBlack.getGreatestValue();
			}

			if (fourKindBlack == fourKindWhite) {
				result = "Tie: the fours of a kind are equal.";
			}
			break;
		// straight flush vs straight flush
		case 18:
			int highestValueStraightFlushBlack = handBlack.getIntCardValues().get(4);
			handBlack.setGreatestValue(Integer.toString(highestValueStraightFlushBlack));
			switchToNormalCardValues(handBlack);

			int highestValueStraightFlushWhite = handWhite.getIntCardValues().get(4);
			handWhite.setGreatestValue(Integer.toString(highestValueStraightFlushWhite));
			switchToNormalCardValues(handWhite);

			if (highestValueStraightFlushBlack > highestValueStraightFlushWhite) {
				result = "Black wins - its straight flush's higher value, " + handBlack.getGreatestValue()
						+ ", is greater than that of white, which is " + handWhite.getGreatestValue();
			}

			if (highestValueStraightFlushBlack < highestValueStraightFlushWhite) {
				result = "White wins - its straight flush's higher value, " + handWhite.getGreatestValue()
						+ ", is greater than that of black, which is " + handBlack.getGreatestValue();
			}

			if (highestValueStraightFlushBlack == highestValueStraightFlushWhite) {
				result = "Tie: the straight flushes are equal.";
			}

			break;
		}
		return result;
	}

	private static String overlyLongChainOfIfStatements(Hand handBlack, Hand handWhite, String result) {
		String degree = "";
		if (handBlack.getIntCardValues().get(4) > handWhite.getIntCardValues().get(4)) {
			handBlack.setGreatestValue(Integer.toString(handBlack.getIntCardValues().get(4)));
			switchToNormalCardValues(handBlack);
			handWhite.setGreatestValue(Integer.toString(handWhite.getIntCardValues().get(4)));
			switchToNormalCardValues(handWhite);
			degree = "highest";
			result = "Black wins - its " + degree + " card, " + handBlack.getGreatestValue()
					+ ", is greater than that of white, which is " + handWhite.getGreatestValue();
		}

		if (handBlack.getIntCardValues().get(4) < handWhite.getIntCardValues().get(4)) {
			handBlack.setGreatestValue(Integer.toString(handBlack.getIntCardValues().get(4)));
			switchToNormalCardValues(handBlack);
			handWhite.setGreatestValue(Integer.toString(handWhite.getIntCardValues().get(4)));
			switchToNormalCardValues(handWhite);
			degree = "highest";
			result = "White wins - its " + degree + " card, " + handWhite.getGreatestValue()
					+ ", is greater than that of black, which is " + handBlack.getGreatestValue();
		}

		if (handBlack.getIntCardValues().get(4) == handWhite.getIntCardValues().get(4)) {
			if (handBlack.getIntCardValues().get(3) > handWhite.getIntCardValues().get(3)) {
				handBlack.setGreatestValue(Integer.toString(handBlack.getIntCardValues().get(3)));
				switchToNormalCardValues(handBlack);
				handWhite.setGreatestValue(Integer.toString(handWhite.getIntCardValues().get(3)));
				switchToNormalCardValues(handWhite);
				degree = "second highest";
				result = "Black wins - its " + degree + " card, " + handBlack.getGreatestValue()
						+ ", is greater than that of white, which is " + handWhite.getGreatestValue();
			}
			if (handBlack.getIntCardValues().get(3) < handWhite.getIntCardValues().get(3)) {
				handBlack.setGreatestValue(Integer.toString(handBlack.getIntCardValues().get(3)));
				switchToNormalCardValues(handBlack);
				handWhite.setGreatestValue(Integer.toString(handWhite.getIntCardValues().get(3)));
				switchToNormalCardValues(handWhite);
				degree = "second highest";
				result = "White wins - its " + degree + " card, " + handWhite.getGreatestValue()
						+ ", is greater than that of black, which is " + handBlack.getGreatestValue();
			}

			if (handBlack.getIntCardValues().get(3) == handWhite.getIntCardValues().get(3)) {
				if (handBlack.getIntCardValues().get(2) > handWhite.getIntCardValues().get(2)) {
					handBlack.setGreatestValue(Integer.toString(handBlack.getIntCardValues().get(2)));
					switchToNormalCardValues(handBlack);
					handWhite.setGreatestValue(Integer.toString(handWhite.getIntCardValues().get(2)));
					switchToNormalCardValues(handWhite);
					degree = "third highest";
					result = "Black wins - its " + degree + " card, " + handBlack.getGreatestValue()
							+ ", is greater than that of white, which is " + handWhite.getGreatestValue();
				}

				if (handBlack.getIntCardValues().get(2) < handWhite.getIntCardValues().get(2)) {
					handBlack.setGreatestValue(Integer.toString(handBlack.getIntCardValues().get(2)));
					switchToNormalCardValues(handBlack);
					handWhite.setGreatestValue(Integer.toString(handWhite.getIntCardValues().get(2)));
					switchToNormalCardValues(handWhite);
					degree = "third highest";
					result = "White wins - its " + degree + " card, " + handWhite.getGreatestValue()
							+ ", is greater than that of black, which is " + handBlack.getGreatestValue();
				}

				if (handBlack.getIntCardValues().get(2) == handWhite.getIntCardValues().get(2)) {
					if (handBlack.getIntCardValues().get(1) > handWhite.getIntCardValues().get(1)) {
						handBlack.setGreatestValue(Integer.toString(handBlack.getIntCardValues().get(1)));
						switchToNormalCardValues(handBlack);
						handWhite.setGreatestValue(Integer.toString(handWhite.getIntCardValues().get(1)));
						switchToNormalCardValues(handWhite);
						degree = "fourth highest";
						result = "Black wins - its " + degree + " card, " + handBlack.getGreatestValue()
								+ ", is greater than that of white, which is " + handWhite.getGreatestValue();
					}

					if (handBlack.getIntCardValues().get(1) < handWhite.getIntCardValues().get(1)) {
						handBlack.setGreatestValue(Integer.toString(handBlack.getIntCardValues().get(1)));
						switchToNormalCardValues(handBlack);
						handWhite.setGreatestValue(Integer.toString(handWhite.getIntCardValues().get(1)));
						switchToNormalCardValues(handWhite);
						degree = "fourth highest";
						result = "White wins - its " + degree + " card, " + handWhite.getGreatestValue()
								+ ", is greater than that of black, which is " + handBlack.getGreatestValue();
					}
					if (handBlack.getIntCardValues().get(1) == handWhite.getIntCardValues().get(1)) {
						if (handBlack.getIntCardValues().get(0) > handWhite.getIntCardValues().get(0)) {
							handBlack.setGreatestValue(Integer.toString(handBlack.getIntCardValues().get(0)));
							switchToNormalCardValues(handBlack);
							handWhite.setGreatestValue(Integer.toString(handWhite.getIntCardValues().get(0)));
							switchToNormalCardValues(handWhite);
							degree = "fifth highest";
							result = "Black wins - its " + degree + " card, " + handBlack.getGreatestValue()
									+ ", is greater than that of white, which is " + handWhite.getGreatestValue();
						}
						if (handBlack.getIntCardValues().get(0) < handWhite.getIntCardValues().get(0)) {
							handBlack.setGreatestValue(Integer.toString(handBlack.getIntCardValues().get(0)));
							switchToNormalCardValues(handBlack);
							handWhite.setGreatestValue(Integer.toString(handWhite.getIntCardValues().get(0)));
							switchToNormalCardValues(handWhite);
							degree = "fifth highest";
							result = "White wins - its " + degree + " card, " + handWhite.getGreatestValue()
									+ ", is greater than that of black, which is " + handBlack.getGreatestValue();
						}
						if (handBlack.getIntCardValues().get(0) == handWhite.getIntCardValues().get(0)) {
							result = "Tie: all of the cards are equal";
						}
					}
				}
			}
		}
		return result;
	}

	private static void switchToNormalCardValues(Hand givenHand) {
		if (givenHand.getQuality() == 1) {
			if (givenHand.getGreatestValue() != null) {
				switchForGreatestValueField(givenHand);
			}
		}

		if (givenHand.getQuality() == 2) {
			if (givenHand.getPairNumberList() != null) {
				switchForPairNumberListField(givenHand);
			}

			if (givenHand.getPairValue() != null && givenHand.getGreatestValue() != null) {
				switch (givenHand.getPairValue()) {
				case "10":
					givenHand.setPairValue("T");
					break;
				case "11":
					givenHand.setPairValue("J");
					break;
				case "12":
					givenHand.setPairValue("Q");
					break;
				case "13":
					givenHand.setPairValue("K");
					break;
				case "14":
					givenHand.setPairValue("A");
					break;
				}
				switchForGreatestValueField(givenHand);
			}
		}
		if (givenHand.getQuality() == 3) {
			if (givenHand.getPairNumberList() != null) {
				for (int i = 0; i < 2; i++) {
					switch (givenHand.getPairNumberList().get(i)) {
					case "10":
						givenHand.getPairNumberList().set(i, "T");
						break;
					case "11":
						givenHand.getPairNumberList().set(i, "J");
						break;
					case "12":
						givenHand.getPairNumberList().set(i, "Q");
						break;
					case "13":
						givenHand.getPairNumberList().set(i, "K");
						break;
					case "14":
						givenHand.getPairNumberList().set(i, "A");
						break;
					}
				}
			}
			if (givenHand.getHigherPairValue() != null && givenHand.getLowerPairValue() != null
					&& givenHand.getGreatestValue() != null) {
				switchForGreatestValueField(givenHand);

				switch (givenHand.getLowerPairValue()) {
				case "10":
					givenHand.setLowerPairValue("T");
					break;
				case "11":
					givenHand.setLowerPairValue("J");
					break;
				case "12":
					givenHand.setLowerPairValue("Q");
					break;
				case "13":
					givenHand.setLowerPairValue("K");
					break;
				case "14":
					givenHand.setLowerPairValue("A");
					break;
				}

				switch (givenHand.getHigherPairValue()) {
				case "10":
					givenHand.setHigherPairValue("T");
					break;
				case "11":
					givenHand.setHigherPairValue("T");
					;
					break;
				case "12":
					givenHand.setHigherPairValue("T");
					break;
				case "13":
					givenHand.setHigherPairValue("T");
					break;
				case "14":
					givenHand.setHigherPairValue("T");
					break;
				}
			}
		}

		if (givenHand.getQuality() == 4) {
			switchForThreeOfKindField(givenHand);
		}

		if (givenHand.getQuality() == 5) {
			if (givenHand.getStraightValues() != null) {
				loopAndSwitchForValuesInStraight(givenHand);
			}
			if (givenHand.getGreatestValue() != null) {
				switchForGreatestValueField(givenHand);
			}
		}

		if (givenHand.getQuality() == 6) {
			if (givenHand.getFlushSuit() != null) {
				switchForSuitOfFlush(givenHand);
			}
			if (givenHand.getGreatestValue() != null) {
				switchForGreatestValueField(givenHand);
			}
		}
		if (givenHand.getQuality() == 7) {
			if (givenHand.getThreeOfKindNumber() != null && givenHand.getPairNumberList() != null) {
				switchForPairNumberListField(givenHand);
				switchForThreeOfKindField(givenHand);
			}
			if (givenHand.getGreatestValue() != null) {
				switchForGreatestValueField(givenHand);
			}
		}
		if (givenHand.getQuality() == 8) {
			if (givenHand.getFourOfKindNumber() != null) {
				switchForFourOfKindField(givenHand);
			}
			if (givenHand.getGreatestValue() != null) {
				switchForGreatestValueField(givenHand);
			}
		}
		if (givenHand.getQuality() == 9) {
			if (givenHand.getStraightValues() != null && givenHand.getStraightFlushSuit() != null) {
				switchForSuitOfStraightFlush(givenHand);
				loopAndSwitchForValuesInStraight(givenHand);
			}
			if (givenHand.getGreatestValue() != null) {
				switchForGreatestValueField(givenHand);
			}
		}
	}

	private static void switchForSuitOfStraightFlush(Hand givenHand) {
		switch (givenHand.getStraightFlushSuit()) {
		case "C":
			givenHand.setStraightFlushSuit("Clubs");
			break;
		case "D":
			givenHand.setStraightFlushSuit("Diamonds");
			break;
		case "H":
			givenHand.setStraightFlushSuit("Hearts");
			break;
		case "S":
			givenHand.setStraightFlushSuit("Spades");
			break;
		}
	}

	private static void loopAndSwitchForValuesInStraight(Hand givenHand) {
		for (int i = 0; i < 5; i++) {
			switch (givenHand.getStraightValues().get(i)) {
			case "10":
				givenHand.getStraightValues().set(i, "T");
				break;
			case "11":
				givenHand.getStraightValues().set(i, "J");
				break;
			case "12":
				givenHand.getStraightValues().set(i, "Q");
				break;
			case "13":
				givenHand.getStraightValues().set(i, "K");
				break;
			case "14":
				givenHand.getStraightValues().set(i, "A");
				break;
			}
		}
	}

	private static void switchForSuitOfFlush(Hand givenHand) {
		switch (givenHand.getFlushSuit()) {
		case "C":
			givenHand.setFlushSuit("Clubs");
			break;
		case "D":
			givenHand.setFlushSuit("Diamonds");
			break;
		case "H":
			givenHand.setFlushSuit("Hearts");
			break;
		case "S":
			givenHand.setFlushSuit("Spades");
			break;
		}
	}

	private static void switchForFourOfKindField(Hand givenHand) {
		switch (givenHand.getFourOfKindNumber()) {
		case "10":
			givenHand.setFourOfKindNumber("T");
			break;
		case "11":
			givenHand.setFourOfKindNumber("J");
			break;
		case "12":
			givenHand.setFourOfKindNumber("Q");
			break;
		case "13":
			givenHand.setFourOfKindNumber("K");
			break;
		case "14":
			givenHand.setFourOfKindNumber("A");
			break;
		}
	}

	private static void switchForThreeOfKindField(Hand givenHand) {
		switch (givenHand.getThreeOfKindNumber()) {
		case "10":
			givenHand.setThreeOfKindNumber("T");
			break;
		case "11":
			givenHand.setThreeOfKindNumber("J");
			break;
		case "12":
			givenHand.setThreeOfKindNumber("Q");
			break;
		case "13":
			givenHand.setThreeOfKindNumber("K");
			break;
		case "14":
			givenHand.setThreeOfKindNumber("A");
			break;
		}
	}

	private static void switchForPairNumberListField(Hand givenHand) {
		switch (givenHand.getPairNumberList().get(0)) {
		case "10":
			givenHand.getPairNumberList().set(0, "T");
			break;
		case "11":
			givenHand.getPairNumberList().set(0, "J");
			break;
		case "12":
			givenHand.getPairNumberList().set(0, "Q");
			break;
		case "13":
			givenHand.getPairNumberList().set(0, "K");
			break;
		case "14":
			givenHand.getPairNumberList().set(0, "A");
			break;
		}
	}

	private static void switchForGreatestValueField(Hand givenHand) {
		switch (givenHand.getGreatestValue()) {
		case "10":
			givenHand.setGreatestValue("T");
			break;
		case "11":
			givenHand.setGreatestValue("J");
			break;
		case "12":
			givenHand.setGreatestValue("Q");
			break;
		case "13":
			givenHand.setGreatestValue("K");
			break;
		case "14":
			givenHand.setGreatestValue("A");
			break;
		}
	}
}