package blackjack.test;

import blackjack.domain.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class DeckTest {
	private Deck deck;
	
	@BeforeEach
	public void BeforeEachTestMethod() {
		deck = new Deck();
	}
	
	@ParameterizedTest
	@EnumSource(Suit.class)
	void ShouldContain13RanksForEachSuit(Suit suit) {
		LinkedList<Card> cards = deck.GetCards();
		String description = "Should have a card with rank %s";
		EnumSet.allOf(Rank.class).forEach(
				rank -> assertThat(String.format(description, rank.toString()), cards, hasItem(new Card(rank, suit))));
	}
	
	@Test
	void ShouldBeAStandardCardDeckOf52Cards() {
		assertEquals(52, Deck.MaximumSize());
		assertEquals(Deck.MaximumSize(), deck.GetCards().size());
		assertEquals(Deck.MaximumSize(), deck.NumberOfRemainingCards());
	}
	
	@Test
	void ShouldPickARandomCard() {
		int predeterminedCard = 12;
		Card expectedCard = deck.GetCards().get(predeterminedCard);
		FakeRandom fakeRNG = new FakeRandom();
		fakeRNG.SetNextInt(predeterminedCard);
		
		Card actualCard = deck.PickRandomCard(fakeRNG);
		assertEquals(expectedCard, actualCard);
	}
	
	@Test
	void ShouldNotRetainItsNumberOfCardsWhenPickingOne() {
		deck.PickRandomCard();
		deck.PickRandomCard();
		assertEquals(Deck.MaximumSize() - 2, deck.NumberOfRemainingCards());
	}
	
	@Test
	void ShouldThrowExceptionWhenTryingToPickCardFromEmptyDeck() {
		// Pick all cards to empty the deck
		IntStream.range(0, Deck.MaximumSize()).forEach(i -> deck.PickRandomCard());
		assertThrows(RuntimeException.class, () -> deck.PickRandomCard());
		assertEquals(0, deck.NumberOfRemainingCards());
	}
}
