package blackjack.test;

import blackjack.exceptions.*;
import blackjack.domain.*;

import static org.junit.jupiter.api.Assertions.*;

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
		game.Start();
		
		Player player1 = game.GetPlayerAtSlot(0);
		Player player2 = game.GetPlayerAtSlot(2);
		assertEquals(1, player1.GetNumberOfHands());
		assertEquals(1, player2.GetNumberOfHands());
		assertEquals(2, player1.GetHand(0).GetNumberOfCards());
		assertEquals(2, player2.GetHand(0).GetNumberOfCards());
	}
	
	@Test
	void TheDealersHandShouldContainOneCardAtTheStartOfAGame() {
		game.AddPlayerToFirstVacantSlot(new Player("John Doe", 100));
		game.Start();
		Hand dealerHand = game.GetDealerHand();
		assertEquals(1, dealerHand.GetNumberOfCards());
	}
	
	@Test
	void ThrowAnExceptionWhenAttemptingToStartAGameWithNoPlayers() {
		assertThrows(NoActivePlayersException.class, () -> game.Start());
	}
	
	@Test
	void xyz() {
		
	}
}
