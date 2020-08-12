package blackjack.test;

import blackjack.domain.Card;
import blackjack.domain.Deck;
import blackjack.domain.Rank;
import blackjack.domain.Suit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CardTest {
	
	@Test
	void ShouldCompareCorrectlyWhenEqual() {
		Card firstCard = new Card(Rank.Ace, Suit.Clubs);
		Card secondCard = new Card(Rank.Ace, Suit.Clubs);
		
		assertEquals(firstCard, secondCard);
	}
	
	@Test
	void ShouldCompareCorrectlyWithItself() {
		Card card = new Card(Rank.Ace, Suit.Clubs);
		assertEquals(card, card);
	}
	
	@Test
	void ShouldNotBeEqualToAnObjectOfADifferentType() {
		Card card = new Card(Rank.Ace, Suit.Diamonds);
		String someString = "Hello";
		
		assertNotEquals(card, someString);
	}
	
	@Test
	void ShouldCompareCorrectlyWhenNotEqual() {
		Card firstCard = new Card(Rank.Three, Suit.Diamonds);
		Card secondCard = new Card(Rank.Ace, Suit.Clubs);
		
		assertNotEquals(firstCard, secondCard);
	}
	
	@Test
	void ShouldAlwaysHaveAValidSuit() {
		assertThrows(RuntimeException.class, () -> new Card(Rank.Ace, null));
	}
	
	@Test
	void ShouldAlwaysHaveAValidRank() {
		assertThrows(RuntimeException.class, () -> new Card(null, Suit.Diamonds));
	}
}
