package blackjack.domain;

import java.util.LinkedList;

public class Hand {
	protected LinkedList<Card> cards;
	
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
		
		return softValue;
	}
	
	public void AddCard(Card card) {
		cards.add(card);
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
