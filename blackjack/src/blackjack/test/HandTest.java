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
	void SoftValueShouldEqualHardValueWhenThereIsNoAce() {
		hand.AddCard(new Card(Rank.Ten, Suit.Clubs));
		hand.AddCard(new Card(Rank.Ten, Suit.Diamonds));
		assertEquals(hand.HardValue(), hand.SoftValue());
	}
	
	@Test
	void AnEmptyHandShouldHaveAHardValueOfZero() {
		assertEquals(0, hand.HardValue());
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
	
	@Test
	void ReturnTheSoftValueAsAHandValueIfItIsNotGreaterThan21() {
		hand.AddCard(new Card(Rank.Ten, Suit.Hearts));
		hand.AddCard(new Card(Rank.Ace, Suit.Clubs));
		
		assertEquals(Hand.BLACK_JACK_LIMIT, hand.GetValue());
	}
	
	@Test
	void ReturnTheHardValueAsAHandValueIfTheSoftValueIsGreaterThan21() {
		hand.AddCard(new Card(Rank.Ten, Suit.Hearts));
		hand.AddCard(new Card(Rank.Ace, Suit.Clubs));
		hand.AddCard(new Card(Rank.Five, Suit.Clubs));
		
		assertEquals(16, hand.GetValue());
	}
	
	@Test
	void ReturnTheDistanceBetweenTheHandValueAnd21IfItIsNotGreaterThan21() {
		hand.AddCard(new Card(Rank.Ten, Suit.Hearts));
		hand.AddCard(new Card(Rank.Seven, Suit.Diamonds));
		
		assertEquals(4, hand.DistanceToBlackJack());
	}
	
	@Test
	void ReturnTheDistanceBetweenTheHandValueAnd21IfItIsGreaterThan21() {
		hand.AddCard(new Card(Rank.Ten, Suit.Hearts));
		hand.AddCard(new Card(Rank.Ten, Suit.Diamonds));
		hand.AddCard(new Card(Rank.Seven, Suit.Clubs));
		
		assertEquals(6, hand.DistanceToBlackJack());
	}
}
