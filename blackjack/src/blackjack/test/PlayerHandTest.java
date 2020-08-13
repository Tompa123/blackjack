package blackjack.test;

import blackjack.domain.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerHandTest {
	private PlayerHand hand;
	private final int INITIAL_BET = 100;
	
	@BeforeEach
	void beforeEachTest() {
		hand = new PlayerHand(INITIAL_BET);
	}
	
	@Test
	void ANewPlayerHandShouldBeAssociatedWithABet() {
		assertEquals(INITIAL_BET, hand.GetBet());
	}

	@Test
	void DoubleDownDoublesThePlayerHandsBet() {
		hand.DoubleDown();
		assertEquals(INITIAL_BET * 2, hand.GetBet());
		assertTrue(hand.HasDoubledDown());
	}
	
	@Test
	void ShouldNotBeDoubledDownInitially() {
		assertFalse(hand.HasDoubledDown());
	}
	
	@Test
	void ShouldNotBeAbleToDoubleDownTwice() {
		PlayerHand hand = new PlayerHand(100);
		hand.DoubleDown();
		hand.DoubleDown();
		assertEquals(INITIAL_BET * 2, hand.GetBet());
	}
	
	@Test
	void AHandWith2IdenticallyValuedCardsCanBeSplit() {
		PlayerHand splittable = new PlayerHand(100);
		PlayerHand notSplittable = new PlayerHand(100);

		splittable.AddCard(new Card(Rank.Ten, Suit.Clubs));
		splittable.AddCard(new Card(Rank.Ten, Suit.Diamonds));
		notSplittable.AddCard(new Card(Rank.Five, Suit.Hearts));
		notSplittable.AddCard(new Card(Rank.Six, Suit.Clubs));
		
		assertTrue(splittable.CanBeSplit());
		assertFalse(notSplittable.CanBeSplit());
	}
	
	@Test
	void AHandWithMoreThan2CardsCannotBeSplit() {
		hand.AddCard(new Card(Rank.Ace, Suit.Diamonds));
		hand.AddCard(new Card(Rank.Ace, Suit.Diamonds));
		hand.AddCard(new Card(Rank.Ace, Suit.Diamonds));
		
		assertFalse(hand.CanBeSplit());
	}
	
	@Test
	void AHandWithLessThan2CardsCannotBeSplit() {
		hand.AddCard(new Card(Rank.Ace, Suit.Diamonds));
		assertFalse(hand.CanBeSplit());
	}
	
	@Test
	void SplitAHandOfTwoCards() {
		Card firstCard = new Card(Rank.Five, Suit.Clubs);
		Card secondCard = new Card(Rank.Five, Suit.Diamonds);
		hand.AddCard(firstCard);
		hand.AddCard(secondCard);
		
		PairOfHands split = hand.Split();
		assertEquals(Rank.Five.Value(), split.firstHand.HardValue());
		assertEquals(Rank.Five.Value(), split.secondHand.HardValue());
		assertEquals(Rank.Five.Value() + Rank.Five.Value(), hand.HardValue());
	}
	
	@Test
	void ASplitHandShouldRetainTheInitialBet() {
		Card firstCard = new Card(Rank.Five, Suit.Clubs);
		Card secondCard = new Card(Rank.Five, Suit.Diamonds);
		hand.AddCard(firstCard);
		hand.AddCard(secondCard);
		
		PairOfHands split = hand.Split();
		assertEquals(INITIAL_BET, split.firstHand.GetBet());
		assertEquals(INITIAL_BET, split.secondHand.GetBet());
	}
	
	@Test
	void ShouldNotAllowSplittingAHandWithLessThanTwoCards() {
		hand.AddCard(new Card(Rank.Ace, Suit.Hearts));
		
		assertThrows(RuntimeException.class, () -> hand.Split());
	}
	
	@Test
	void ShouldNotAllowSplittingAHandWithMoreThanTwoCards() {
		hand.AddCard(new Card(Rank.Ace, Suit.Hearts));
		hand.AddCard(new Card(Rank.Ace, Suit.Diamonds));
		hand.AddCard(new Card(Rank.Ace, Suit.Clubs));
		
		assertThrows(RuntimeException.class, () -> hand.Split());
	}
}
