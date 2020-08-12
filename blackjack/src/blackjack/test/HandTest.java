package blackjack.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import blackjack.domain.*;

class HandTest {
	private Hand hand;
	
	@BeforeEach
	void beforeEachTest() {
		hand = new Hand();
	}
	
	@Test
	void CalculateTheHardValueOfAHandWithASingleCard() {
		Rank rank = Rank.Eight;
		Card card = new Card(rank, Suit.Clubs);
		hand.AddCard(card);
		
		assertEquals(rank.Value(), hand.HardValue());
	}
	
	@Test
	void CalculateTheHardValueOfAHandWithMultipleCards() {
		Rank firstRank = Rank.King;
		Rank secondRank = Rank.Five;
		
		hand.AddCard(new Card(firstRank, Suit.Diamonds));
		hand.AddCard(new Card(secondRank, Suit.Hearts));
		
		assertEquals(firstRank.Value() + secondRank.Value(), hand.HardValue());
	}
	
	@Test
	void CalculateTheSoftValueOfAHandWithASingleAce() {
		hand.AddCard(new Card(Rank.Ace, Suit.Clubs));
		assertEquals(Hand.SOFT_ACE_VALUE, hand.SoftValue());
	}
	
	@Test
	void CalculateTheSoftValueOfAHandWithMultipleCards() {
		hand.AddCard(new Card(Rank.Ace, Suit.Clubs));
		hand.AddCard(new Card(Rank.Seven, Suit.Diamonds));
		
		assertEquals(Hand.SOFT_ACE_VALUE + Rank.Seven.Value(), hand.SoftValue());
		assertEquals(Rank.Ace.Value() + Rank.Seven.Value(), hand.HardValue());
	}
	
	@Test
	void ASoftHandWithAValueAbove21ShouldBeEqualToAHardHand() {
		// 3 cards with soft value 1 + 10 + 11 = 22 (bust, i.e. > 21)
		// [..] with hard value 1 + 10 + 1 = 12
		hand.AddCard(new Card(Rank.Ace, Suit.Clubs));
		hand.AddCard(new Card(Rank.King, Suit.Diamonds));
		hand.AddCard(new Card(Rank.Ace, Suit.Hearts));
		
		// Soft value is irrelevant if it's beyond 21. In those cases, we should
		// consider it to be the same as the hard value.
		assertEquals(hand.HardValue(), hand.SoftValue());
	}
	
	@Test
	void CalculateSoftValueOfAHandWithOnlyAces() {
		hand.AddCard(new Card(Rank.Ace, Suit.Clubs));
		hand.AddCard(new Card(Rank.Ace, Suit.Hearts));
		
		assertEquals(Hand.SOFT_ACE_VALUE + Rank.Ace.Value(), hand.SoftValue());
	}
	
	@Test
	void AnEmptyHandShouldHaveAHardValueOfZero() {
		assertEquals(0, hand.HardValue());
	}

	@Test
	void AHandWith2IdenticallyValuedCardsCanBeSplit() {
		Hand splittable = new Hand();
		Hand notSplittable = new Hand();

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
