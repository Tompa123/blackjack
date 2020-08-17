package blackjack.test;

import blackjack.exceptions.*;
import blackjack.domain.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {
	private Game game;
	
	@BeforeEach
	void beforeEachTest() {
		game = new Game();
	}
	
	@Test
	void AddAPlayerToASpecificSlotInAGame() {
		Player player = new Player("John Doe", 100);
		int slot = 0;
		game.AddPlayerToSlot(player, slot);
		
		Player actualPlayer = game.GetPlayerAtSlot(0);
		assertEquals(player, actualPlayer);
	}
	
	@Test
	void ThrowAnExceptionWhenAttemptingToAddPlayerToAnOccupiedSlot() {
		Player player = new Player("John Doe", 100);
		Player anotherPlayer = new Player("Jane Doe", 100);
		game.AddPlayerToSlot(player, 0);
		
		assertThrows(PlayerSlotOccupiedException.class, () -> game.AddPlayerToSlot(anotherPlayer, 0));
	}
	
	@Test
	void ThrowAnExceptionWhenAttemptingToAddPlayerToANonExistentSlot() {
		Player player = new Player("John Doe", 100);
		assertThrows(RuntimeException.class, () -> game.AddPlayerToSlot(player, -1));
	}
	
	@Test
	void AddAPlayerToTheFirstVacantSlot() {
		Player player1 = new Player("Player 1", 100);
		Player player2 = new Player("Player 2", 100);
		Player player3 = new Player("Player 3", 100);
		
		game.AddPlayerToSlot(player1, 0);
		game.AddPlayerToSlot(player2, 2);
		game.AddPlayerToFirstVacantSlot(player3);
		
		Player playerAtVacantSlot = game.GetPlayerAtSlot(1);
		assertEquals(player3, playerAtVacantSlot);
	}
	
	@Test
	void ThrowExceptionWhenAttemptingToAddAPlayerToAFullGame() {
		Player playerToAdd = new Player("John Doe", 100);
		IntStream.range(0, Game.MAX_SLOTS).forEach(slot -> game.AddPlayerToSlot(new Player("Player " + slot, 100), slot));
		assertThrows(GameIsFullException.class, () -> game.AddPlayerToFirstVacantSlot(playerToAdd));
	}
	
	@Test
	void GiveEachPlayerASingleHandOfTwoCardsWhenStarting() {
		game.AddPlayerToSlot(new Player("John Doe", 100), 0);
		game.AddPlayerToSlot(new Player("Jane Doe", 100), 2);
		game.StartNewRound();
		
		Player player1 = game.GetPlayerAtSlot(0);
		Player player2 = game.GetPlayerAtSlot(2);
		assertEquals(1, player1.GetNumberOfHands());
		assertEquals(1, player2.GetNumberOfHands());
		assertEquals(2, player1.GetHand(0).GetNumberOfCards());
		assertEquals(2, player2.GetHand(0).GetNumberOfCards());
	}
	
	@Test
	void TheDealersHandShouldContainOneCardAtTheStartOfARound() {
		game.AddPlayerToFirstVacantSlot(new Player("John Doe", 100));
		game.StartNewRound();
		Hand dealerHand = game.GetDealerHand();
		assertEquals(1, dealerHand.GetNumberOfCards());
	}
	
	@Test
	void ThrowAnExceptionWhenAttemptingToStartAGameWithNoPlayers() {
		assertThrows(NoActivePlayersException.class, () -> game.StartNewRound());
	}
	
	@Test
	void DealCardToCurrentPlayerDuringARound() {
		Player player = new Player("John Doe", 100);
		game.AddPlayerToSlot(player, 0);
		game.StartNewRound();
		game.Hit();
		
		// Remember, players start with 2 cards at the start of a round; therefore 2 + 1 = 3 cards expected.
		assertEquals(3, game.GetPlayerAtSlot(0).GetHand(0).GetNumberOfCards());
	}
	
	@Test
	void DealingACardThatCausesAPlayersSingleHandToBustShouldChangeCurrentTurn() {
		Player player1 = new Player("John Doe", 100);
		Player player2 = new Player("Jane Doe", 100);
		player1.AddHand(CreateHandWithHardValue21());
		player2.AddHand(CreateHandWithHardValue21());
		
		game.AddPlayerToSlot(player1, 0);
		game.AddPlayerToSlot(player2, 1);
		game.SetCurrentTurn(0);
		game.Hit();
		
		assertEquals(player2, game.GetCurrentPlayer());
		assertEquals(0, game.GetCurrentHand());
	}
	
	@Test
	void DealingACardToAHandThatBustsShouldMoveOntoTheNextHand() {
		Player player1 = new Player("John Doe", 100);
		player1.AddHand(CreateHandWithHardValue21());
		player1.AddHand(CreateHandWithHardValue21());
		
		game.AddPlayerToSlot(player1, 0);
		game.SetCurrentTurn(0);
		game.Hit();
		
		assertEquals(player1, game.GetCurrentPlayer());
		assertEquals(1, game.GetCurrentHand());
	}
	
	@Test
	void StandingAPlayerShouldMoveOntoTheNextPlayer() {
		Player player1 = new Player("John Doe", 100);
		Player player2 = new Player("Jane Doe", 100);
		player1.AddHand(CreateHandWithHardValue21());
		player2.AddHand(CreateHandWithHardValue21());
		
		game.AddPlayerToSlot(player1, 0);
		game.AddPlayerToSlot(player2, 1);
		game.SetCurrentTurn(0);
		game.Stand();
		
		assertEquals(player2, game.GetCurrentPlayer());
		assertEquals(0, game.GetCurrentHand());
	}
	
	@Test
	void AddCardsToDealerWhenLastPlayerChoosesToStand() {
		Player player1 = new Player("John Doe", 100);
		
		game.AddPlayerToSlot(player1, 0);
		game.StartNewRound();
		game.Stand();
		
		assertTrue(game.GetDealerHand().GetNumberOfCards() > 1);
		assertFalse(game.CurrentlyRunning());
	}
	
	@Test
	void StandingAPlayerWithSeveralHandsShouldMoveOntoTheNextHand() {
		Player player1 = new Player("John Doe", 100);
		player1.AddHand(CreateHandWithHardValue21());
		player1.AddHand(CreateHandWithHardValue21());
		
		game.AddPlayerToSlot(player1, 0);
		game.SetCurrentTurn(0);
		game.Stand();
		
		assertEquals(player1, game.GetCurrentPlayer());
		assertEquals(1, game.GetCurrentHand());
	}
	
	@Test
	void StandingAPlayerWhenAGameHasEndedIsNotAllowed() {
		Player player1 = new Player("John Doe", 100);
		
		game.AddPlayerToSlot(player1, 0);
		game.StartNewRound();
		game.Stand();
		
		// At this point the turn as gone over to the dealer and the game is done.
		// Therefore, the next call to Stand() is illegal, because it's no player's turn.
		assertThrows(GameNotStartedException.class, () -> game.Stand());
	}
	
	@Test
	void HittingAPlayerWhenTheLastPlayerHasBustedIsNotAllowed() {
		Player player1 = new Player("John Doe", 100);
		player1.AddHand(CreateHandWithHardValue21());
		
		game.AddPlayerToSlot(player1, 0);
		game.SetCurrentTurn(0);
		game.Hit();
		
		// At this point the turn as gone over to the dealer and the game is done.
		// Therefore, the next call to Hit() is illegal, because it's no player's turn.
		assertThrows(GameNotStartedException.class, () -> game.Hit());		
	}
	
	@Test
	void HittingAPlayerWhenNoGameIsRunningIsNotAllowed() {
		Player player1 = new Player("John Doe", 100);
		game.AddPlayerToSlot(player1, 0);
		assertThrows(GameNotStartedException.class, () -> game.Hit());
	}
	
	@Test
	void FirstPlayersTurnShouldBeAtStartOfRound() {
		Player player1 = new Player("John Doe", 100);
		Player player2 = new Player("Jane Doe", 100);
		
		game.AddPlayerToSlot(player1, 0);
		game.AddPlayerToSlot(player2, 1);
		game.StartNewRound();
		
		assertEquals(player1, game.GetCurrentPlayer());
	}
	
	@Test
	void AllBetsShouldBeWithdrawnFromPlayersAtStartOfRound() {
		Player player1 = new Player("John Doe", 100);
		Player player2 = new Player("Jane Doe", 100);
		player1.PlaceInitialBet(25);
		player2.PlaceInitialBet(70);
		
		game.AddPlayerToSlot(player1, 0);
		game.AddPlayerToSlot(player2, 1);
		game.StartNewRound();
		
		assertEquals(75, game.GetPlayerAtSlot(0).GetBalance());
		assertEquals(30, game.GetPlayerAtSlot(1).GetBalance());
	}
	
	@Test
	void DealerDrawsCardUntilHard17OrGreater() {
		LinkedList<Card> cards = new LinkedList<Card>(Arrays.asList(
													  new Card(Rank.Five, Suit.Diamonds), 
													  new Card(Rank.Five, Suit.Clubs), 
													  new Card(Rank.Eight, Suit.Clubs),
													  new Card(Rank.Ten, Suit.Hearts)));
		FakeDeck fakeDeck = new FakeDeck(cards);
		Game gameWithFakeDeck = new Game(fakeDeck);
		gameWithFakeDeck.GiveDealerCards();
		
		// Should only pick the first three cards in the deck. (5 + 5 + 8 = 18, (> 17))
		assertEquals(3, gameWithFakeDeck.GetDealerHand().GetNumberOfCards());
		assertEquals(1, fakeDeck.NumberOfRemainingCards());
	}
	
	@Test
	void DealerDrawsCardUntilBetweenSoft17AndSoft21() {
		LinkedList<Card> cards = new LinkedList<Card>(Arrays.asList(
				  									  new Card(Rank.Ace, Suit.Diamonds), 
													  new Card(Rank.Ten, Suit.Clubs), 
													  new Card(Rank.Eight, Suit.Clubs),
													  new Card(Rank.Ten, Suit.Hearts)));
		FakeDeck fakeDeck = new FakeDeck(cards);
		Game gameWithFakeDeck = new Game(fakeDeck);
		gameWithFakeDeck.GiveDealerCards();

		// Should only pick the 2 first cards in the deck. (11 (soft ace value) + 10 = 21)
		assertEquals(2, gameWithFakeDeck.GetDealerHand().GetNumberOfCards());
		assertEquals(2, fakeDeck.NumberOfRemainingCards());		
	}
	
	@Test
	void DealerDrawsUntilHard17IfSoftValueIsBeyond21() {
		LinkedList<Card> cards = new LinkedList<Card>(Arrays.asList(
				  									  new Card(Rank.Ace, Suit.Diamonds), 
													  new Card(Rank.Four, Suit.Clubs), 
													  new Card(Rank.Eight, Suit.Clubs),
													  new Card(Rank.Ten, Suit.Hearts)));
		FakeDeck fakeDeck = new FakeDeck(cards);
		Game gameWithFakeDeck = new Game(fakeDeck);
		gameWithFakeDeck.GiveDealerCards();

		// Should pick all of the cards in the deck. The soft value of first 3 cards is (11 + 4 + 8 = 23 (> 21)) 
		assertEquals(4, gameWithFakeDeck.GetDealerHand().GetNumberOfCards());
		assertEquals(0, fakeDeck.NumberOfRemainingCards());		
	}
	
	@Test
	void PlayersLoseWhenTheDealerIsCloserTo21ThanTheyAre() {
		Hand dealer = new Hand();
		Player player = new Player("John Doe", 100);
		PlayerHand playerHand = new PlayerHand(100);
		playerHand.AddCard(new Card(Rank.Ten, Suit.Hearts));
		playerHand.AddCard(new Card(Rank.Seven, Suit.Clubs));
		dealer.AddCard(new Card(Rank.Ten, Suit.Clubs));
		dealer.AddCard(new Card(Rank.Eight, Suit.Diamonds));
		player.AddHand(playerHand);
		
		game.AddPlayerToSlot(player, 0);
		game.SetDealerHand(dealer);
		
		HandState state = game.GetHandState(0, 0);
		assertEquals(HandState.Loss, state);
	}
	
	@Test
	void PlayersTieWhenTheDealersHandValueIsEqualToThePlayers() {
		Hand dealer = new Hand();
		Player player = new Player("John Doe", 100);
		PlayerHand playerHand = new PlayerHand(100);
		playerHand.AddCard(new Card(Rank.Ten, Suit.Hearts));
		playerHand.AddCard(new Card(Rank.Nine, Suit.Clubs));
		dealer.AddCard(new Card(Rank.Five, Suit.Clubs));
		dealer.AddCard(new Card(Rank.Five, Suit.Hearts));
		dealer.AddCard(new Card(Rank.Nine, Suit.Diamonds));
		player.AddHand(playerHand);
		
		game.AddPlayerToSlot(player, 0);
		game.SetDealerHand(dealer);
		
		HandState state = game.GetHandState(0, 0);
		assertEquals(HandState.Push, state);
	}
	
	@Test
	void PlayersWinWhenTheirHandIsCloserTo21ThanTheDealer() {
		Hand dealer = new Hand();
		Player player = new Player("John Doe", 100);
		PlayerHand playerHand = new PlayerHand(100);
		playerHand.AddCard(new Card(Rank.Ten, Suit.Hearts));
		playerHand.AddCard(new Card(Rank.Ten, Suit.Clubs));
		dealer.AddCard(new Card(Rank.Ten, Suit.Clubs));
		dealer.AddCard(new Card(Rank.Eight, Suit.Diamonds));
		player.AddHand(playerHand);
		
		game.AddPlayerToSlot(player, 0);
		game.SetDealerHand(dealer);
		
		HandState state = game.GetHandState(0, 0);
		assertEquals(HandState.Win, state);
	}
	
	@Test
	void APlayerHandGreaterThan21IsBust() {
		Player player = new Player("John Doe", 100);
		PlayerHand playerHand = new PlayerHand(100);
		playerHand.AddCard(new Card(Rank.Ten, Suit.Hearts));
		playerHand.AddCard(new Card(Rank.Ten, Suit.Clubs));
		playerHand.AddCard(new Card(Rank.Two, Suit.Clubs));
		player.AddHand(playerHand);
		
		game.AddPlayerToSlot(player, 0);
		
		HandState state = game.GetHandState(0, 0);
		assertEquals(HandState.Bust, state);	
	}
	
	@Test
	void GetTheStateOfAHandThatDoesNotExistIsNotAllowed() {
		Player player1 = new Player("John Doe", 100);
		game.AddPlayerToSlot(player1, 0); // Add a player without any hands.
		assertThrows(RuntimeException.class, () -> game.GetHandState(0,  0));
	}
	
	@Test
	void GetTheStateOfAHandOfANonExistentPlayerIsNotAllowed() {
		assertThrows(PlayerDoesNotExistException.class, () -> game.GetHandState(0,  0));	
	}
	
	private PlayerHand CreateHandWithHardValue21() {
		PlayerHand hand = new PlayerHand(100);
		hand.AddCard(new Card(Rank.Ten, Suit.Clubs));
		hand.AddCard(new Card(Rank.Ten, Suit.Diamonds));
		hand.AddCard(new Card(Rank.Ace, Suit.Diamonds));
		return hand;
	}
}
