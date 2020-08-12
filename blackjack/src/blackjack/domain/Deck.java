package blackjack.domain;

import java.util.LinkedList;
import java.util.Random;

public class Deck {
	private LinkedList<Card> cards = new LinkedList<Card>();
	
	public Deck() {
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				cards.add(new Card(rank, suit));
			}
		}
	}
	
	public LinkedList<Card> GetCards() {
		return new LinkedList<Card>(cards);
	}
	
	public Card PickRandomCard(Random rng) {
		if (cards.size() <= 0) {
			throw new RuntimeException( "Tried to pick a random card from an empty deck. " +
									    "Use the method 'NumberOfRemainingCards' before picking cards.");
		}
		
		int chosenCard = rng.nextInt(cards.size());
		return cards.remove(chosenCard);
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
}
