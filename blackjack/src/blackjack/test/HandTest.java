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
	
	@Test
	void AHandHigherThan21IsBusted() {
		hand.AddCard(new Card(Rank.Ten, Suit.Hearts));
		hand.AddCard(new Card(Rank.Ten, Suit.Diamonds));
		hand.AddCard(new Card(Rank.Ten, Suit.Diamonds));
		
		assertTrue(hand.IsBusted());
	}
	
	@Test
	void AHandLesserThan21IsNotBusted() {
		hand.AddCard(new Card(Rank.Ten, Suit.Hearts));
		assertFalse(hand.IsBusted());
	}
	
	@Test 
	void AHandEqualTo21IsNotBusted() {
		hand.AddCard(new Card(Rank.Ten, Suit.Hearts));
		hand.AddCard(new Card(Rank.Ten, Suit.Diamonds));
		hand.AddCard(new Card(Rank.Ace, Suit.Clubs));
		
		assertFalse(hand.IsBusted());
	}
	
	@Test
	void APairOfTwoCardsMaking21IsABlackJack() {
		hand.AddCard(new Card(Rank.Ten, Suit.Clubs));
		hand.AddCard(new Card(Rank.Ace, Suit.Diamonds));
		
		assertTrue(hand.IsBlackJack());
	}
	
	@Test
	void MoreCardsThanTwoCannotMakeABlackJack() {
		// A hand is only considered a black jack if the hand makes 21 with exactly 2 cards.
		hand.AddCard(new Card(Rank.Ten, Suit.Clubs));
		hand.AddCard(new Card(Rank.Ten, Suit.Clubs));
		hand.AddCard(new Card(Rank.Ace, Suit.Hearts));
		
		assertFalse(hand.IsBlackJack());
	}
}
