package blackjack.test;

import java.util.LinkedList;

import blackjack.domain.*;

public class FakeDeck extends Deck {
	
	public FakeDeck(LinkedList<Card> cards) {
		super.cards = cards;
	}
	
	@Override
	public Card PickRandomCard() {
		return cards.pop();
	}
}
