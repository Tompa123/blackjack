package blackjack.domain;

import java.util.LinkedList;
import java.util.Random;

public class Deck {
	protected LinkedList<Card> cards = new LinkedList<Card>();
	
	public Deck() {
		Refill();
	}
	
	public LinkedList<Card> GetCards() {
		return new LinkedList<Card>(cards);
	}
	
	public Card PickRandomCard(Random rng) {
		if (cards.size() <= 0) {
			Refill();
		}
		
		int chosenCard = rng.nextInt(cards.size());
		Card pickedCard = cards.remove(chosenCard);
		
		return pickedCard;
	}
	
	public Card PickRandomCard() {
		Random rng = new Random();
		return PickRandomCard(rng);
	}
	
	public int NumberOfRemainingCards() {
		return cards.size();
	}
	
	public static int MaximumSize() {
		return Rank.values().length * Suit.values().length;
	}
	
	private void Refill() {
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				cards.add(new Card(rank, suit));
			}
		}		
	}
}
