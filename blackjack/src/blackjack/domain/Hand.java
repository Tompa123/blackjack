package blackjack.domain;

import java.util.LinkedList;

public class Hand {
	private LinkedList<Card> cards;
	
	public Hand() {
		cards = new LinkedList<Card>();
	}
	
	public int HardValue() {
		int hardValue = 0;
		for (Card card : cards) {
			hardValue += card.GetValue();
		}
		
		return hardValue;
	}
	
	public int SoftValue() {
		int softValue = HardValue();
		if (ContainsAnAce()) {
			softValue += SOFT_ACE_VALUE - Rank.Ace.Value(); // Basically replaces an ace with its equivalent soft value.
		}
		
		return softValue > BLACK_JACK_LIMIT ? HardValue() : softValue;
	}
	
	public void AddCard(Card card) {
		cards.add(card);
	}
	
	public boolean CanBeSplit() {
		if (cards.size() != 2) {
			return false;
		}
		
		return cards.getFirst().Rank() == cards.getLast().Rank();
	}
	
	public PairOfHands Split() {
		if (cards.size() != 2) {
			throw new RuntimeException("A hand must have exactly 2 cards in order to be split correctly." +
									   "Use the method 'CanBeSplit()' before splitting a hand.");
		}
		
		Hand firstHand = new Hand();
		Hand secondHand = new Hand();
		
		firstHand.AddCard(cards.getFirst());
		secondHand.AddCard(cards.getLast());
		
		return new PairOfHands(firstHand, secondHand);
	}
	
	public boolean IsBusted() {
		return HardValue() > BLACK_JACK_LIMIT;
	}
	
	public boolean IsBlackJack() {
		return SoftValue() == BLACK_JACK_LIMIT && cards.size() == 2;
	}
	
	private boolean ContainsAnAce() {
		for (Card card : cards) {
			if (card.Rank() == Rank.Ace) {
				return true;
			}
		}
		
		return false;
	}
	
	public static final int SOFT_ACE_VALUE = 11;
	public static final int BLACK_JACK_LIMIT = 21;
}
