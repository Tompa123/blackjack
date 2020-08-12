package blackjack.domain;

import java.util.Objects;

public class Card {
	private Rank rank;
	private Suit suit;
	
	public Card(Rank rank, Suit suit) {
		Objects.requireNonNull(rank, "A card can never have a 'null' rank.");
		Objects.requireNonNull(suit, "A card can never have a 'null' suit.");
		
		this.rank = rank;
		this.suit = suit;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		
		if (other == null || this.getClass() != other.getClass()) {
			return false;
		}
		
		Card otherCard = (Card)other;
		
		if (this.rank.equals(otherCard.rank) && this.suit.equals(otherCard.suit)) {
			return true;
		}
		
		return false;
	}
	
}
