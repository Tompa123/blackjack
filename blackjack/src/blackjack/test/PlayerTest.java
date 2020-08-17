package blackjack.test;

import blackjack.domain.*;
import blackjack.exceptions.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class PlayerTest {
	private Player player;
	
	@BeforeEach
	void beforeEachTest() {
		player = new Player("Test Player", 100);
	}
	
	@ParameterizedTest
	@EnumSource(Rank.class)
	void SplitAPlayersHand(Rank rank) {
		PlayerHand hand = new PlayerHand(100);
		hand.AddCard(new Card(rank, Suit.Clubs));
		hand.AddCard(new Card(rank, Suit.Diamonds));
		
		player.AddHand(hand);
		player.SplitHand(0);
		
		assertEquals(2, player.GetNumberOfHands());
		
		PlayerHand firstSplit = player.GetHand(0);
		PlayerHand secondSplit = player.GetHand(1);
		
		assertEquals(rank.Value(), firstSplit.HardValue());
		assertEquals(rank.Value(), secondSplit.HardValue());
	}
	
	@Test
	void DoubleDownAPlayersHand() {
		int bet = 100;
		PlayerHand hand = new PlayerHand(bet);
		player.AddHand(hand);
		player.DoubleDown(0);
		
		PlayerHand actualHand = player.GetHand(0);
		assertEquals(2 * bet, actualHand.GetBet());
		assertTrue(actualHand.HasDoubledDown());
	}
	
	@Test
	void DealACardToAPlayersHand() {
		PlayerHand hand = new PlayerHand(100);
		Card cardToAdd = new Card(Rank.Ace, Suit.Diamonds);
		player.AddHand(hand);
		player.DealCardToHand(cardToAdd, 0);
		
		PlayerHand actualHand = player.GetHand(0);
		Card actualCard = actualHand.GetCard(0);
		assertEquals(cardToAdd, actualCard);
	}
	
	@Test
	void PlaceAnInitialBet() {
		player.PlaceInitialBet(25);
		assertEquals(25, player.GetInitialBet());
	}
	
	@Test
	void ShouldNotPlaceBetsThatAreHigherThanThePlayersBalance() {
		assertThrows(InsufficientBalanceException.class, () -> player.PlaceInitialBet(101));
	}
	
	@Test
	void PlacingNegativeBetsAreNotAllowed() {
		assertThrows(IllegalArgumentException.class, () -> player.PlaceInitialBet(-1));
	}
	
	@Test
	void ThrowAnExceptionWhenAttemptingToGetNonexistentHand() {
		assertThrows(IllegalArgumentException.class, () -> player.GetHand(0));
		assertThrows(IllegalArgumentException.class, () -> player.GetHand(-1));
	}
	
	@Test
	void CompareEqualityBetweenTwoPlayers() {
		Player firstPlayer = new Player("John Doe", 100);
		Player secondPlayer = new Player("John Doe", 321);
		
		assertEquals(firstPlayer, secondPlayer);
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	void CompareEqualityBetweenAPlayerAnIncompatibleType() {
		String someString = new String("Some string");
		assertFalse(player.equals(someString));
	}
	
	@Test
	void CompareEqualityBetweenPlayerAndNull() {
		assertFalse(player.equals(null));
	}
	
	@Test
	void ThrowAnExceptionWhenAttemptingToCreatePlayerWithNullName() {
		assertThrows(RuntimeException.class, () -> new Player(null, 100));
	}
}
