package blackjack.domain;

import blackjack.exceptions.*;
import blackjack.events.*;

public class Game {
	private Player[] players;
	private int currentTurn;
	private int currentHand;
	private Deck deck;
	private Hand dealer;
	private PlayerStateUpdated playerStateUpdatedEvent = new PlayerStateUpdated();
	private GameEnded gameEndedEvent = new GameEnded();
	private DealerUpdated dealerUpdatedEvent = new DealerUpdated();
	private CurrentSlotChanged currentSlotChangedEvent = new CurrentSlotChanged();
	private static final int DEALER_DRAW_LIMIT = 17;
	private static final int NO_VACANT_SLOTS = -1;
	private static final int NO_PLAYER_FOUND = -1;
	private static final int DEALER_SLOT = -1;
	public static final int MAX_SLOTS = 5;
	
	public Game() {
		players = new Player[MAX_SLOTS];
		currentTurn = DEALER_SLOT;
		deck = new Deck();
		dealer = new Hand();
	}
	
	public Game(Deck deck) {
		this();
		this.deck = deck;
	}
	
	public PlayerStateUpdated PlayerUpdatedEvent() {
		return playerStateUpdatedEvent;
	}
	
	public GameEnded GameEndedEvent() {
		return gameEndedEvent;
	}
	
	public DealerUpdated DealerUpdatedEvent() {
		return dealerUpdatedEvent;
	}
	
	public CurrentSlotChanged CurrentSlotChanged() {
		return currentSlotChangedEvent;
	}
	
	public void StartNewRound() {
		if (!AtLeastOneActivePlayer()) {
			throw new NoActivePlayersException("Attempted to start a game with no players in it. " +
												"Use the method 'AtLeastOneActivePlayer()' to make sure there are players before starting the game.");
		}
		
		dealer.RemoveCards();
		dealer.AddCard(deck.PickRandomCard());
		dealerUpdatedEvent.Fire(dealer);
		
		DealStarterHands();
		SetCurrentTurn(GetFirstOccupiedSlot());
	}
	
	public void PerformActionOnCurrentPlayer(Action action) {
		switch(action) {
		case Hit:
			Hit();
			break;
		case Stand:
			Stand();
			break;
		case DoubleDown:
			DoubleDown();
			break;
		case Split:
			Split();
			break;
		}
	}
	
	public void SetCurrentTurn(int slot) {
		if (!IsSlotInbounds(slot) || players[slot] == null) {
			throw new PlayerDoesNotExistException("No player is occupying the slot %d.".formatted(slot));
		}
		
		currentTurn = slot;
		currentHand = 0;
		currentSlotChangedEvent.Fire(currentTurn, currentHand);
	}
	
	public void SetDealerHand(Hand hand) {
		dealer = new Hand(hand);
	}
	
	public HandState GetHandState(int slot, int handIndex) {
		if (!IsSlotInbounds(slot) || players[slot] == null) {
			String description = "Attempted to get player state for non-existent player (%d).";
			throw new PlayerDoesNotExistException(String.format(description, slot));
		}
		
		Player player = players[slot];
		PlayerHand playerHand = player.GetHand(handIndex);
		
		if (playerHand.IsBusted()) {
			return HandState.Bust;
		} else if (playerHand.DistanceToBlackJack() < dealer.DistanceToBlackJack() || dealer.IsBusted()) {
			return HandState.Win;
		} else if (playerHand.DistanceToBlackJack() > dealer.DistanceToBlackJack()) {
			return HandState.Loss;
		} else {
			return HandState.Push;
		}
	}
	
	public void GiveDealerCards() {
		while (dealer.HardValue() < DEALER_DRAW_LIMIT 
				&& (dealer.SoftValue() < DEALER_DRAW_LIMIT || dealer.SoftValue() > Hand.BLACK_JACK_LIMIT)) {
			dealer.AddCard(deck.PickRandomCard());
		}
		
		dealerUpdatedEvent.Fire(dealer);
	}
	
	public Hand GetDealerHand() {
		return new Hand(dealer);
	}
	
	public void DoubleDown() {
		if (!CurrentlyRunning()) {
			throw new GameNotStartedException("Can't double down; no game is currently running.");
		}
		
		Player player = players[currentTurn];
		player.DoubleDown(currentHand);
		player.AddCardToHand(deck.PickRandomCard(), currentHand);
		playerStateUpdatedEvent.Fire(player, currentTurn);
		MoveToNextHand();
	}
	
	public void Hit() {
		if (!CurrentlyRunning()) {
			throw new GameNotStartedException("Could not move onto the next hand; no game is running");
		}
		
		Player currentPlayer = players[currentTurn];
		currentPlayer.AddCardToHand(deck.PickRandomCard(), currentHand);
		PlayerHand hand = currentPlayer.GetHand(currentHand);
		
		playerStateUpdatedEvent.Fire(currentPlayer, currentTurn);
		if (hand.IsBusted()) {
			MoveToNextHand();
		} else {
			currentSlotChangedEvent.Fire(currentTurn, currentHand);
		}
	}
	
	public void Stand() {
		if (!CurrentlyRunning()) {
			throw new GameNotStartedException("Could not move onto the next hand; no game is running");
		}
		
		MoveToNextHand();
	}
	
	public void Split() {
		if (!CurrentlyRunning()) {
			throw new GameNotStartedException("Cannot split; no game is running.");
		} else if (players[currentTurn].GetBalance() < players[currentTurn].GetHand(currentHand).GetBet()) {
			throw new InsufficientBalanceException("Attempted to split a player's hand, but the player doesn't have sufficient balance.");
		}
		
		Player player = players[currentTurn];
		PairOfHands hand = player.SplitHand(currentHand);
		hand.firstHand.AddCard(deck.PickRandomCard());
		hand.secondHand.AddCard(deck.PickRandomCard());
		
		playerStateUpdatedEvent.Fire(player, currentTurn);
		currentSlotChangedEvent.Fire(currentTurn, currentHand);
	}
	
	public Player GetCurrentPlayer() {
		return new Player(players[currentTurn]);
	}
	
	public int GetCurrentHand() {
		return currentHand;
	}
	
	public void AddPlayerToSlot(Player player, int slot) {
		if (!IsSlotInbounds(slot)) {
			String description = "Attempted to add a player to a non-existent slot (%d). Valid slots are [0, Game.MAX_SLOT)";
			throw new RuntimeException(String.format(description, slot));
		} else if (!IsSlotVacant(slot)) {
			throw new PlayerSlotOccupiedException("Attempted to add a player to an occupied slot. Use the method 'IsSlotVacant()' "
												+ "before attempting to add a player to a slot.");
		}
		
		players[slot] = player;
		playerStateUpdatedEvent.Fire(player, slot);
	}
	
	public void AddPlayerToFirstVacantSlot(Player player) {
		if (IsFull()) {
			throw new GameIsFullException("Attempted to add a player to a full game. Use the 'IsFull()' method before attempting to " +
										  "add a player to a game.");
		}
		
		int vacantSlot = GetFirstVacantSlot();
		AddPlayerToSlot(player, vacantSlot);
	}
	
	public void PlaceInitialBet(int slot, int bet) {
		if (slot < 0 || slot >= players.length || players[slot] == null) {
			throw new PlayerDoesNotExistException("Attempted to place an initial bet for a non-existent player at slot %d.".formatted(slot));
		}
		
		Player player = players[slot];
		player.PlaceInitialBet(bet);
		playerStateUpdatedEvent.Fire(player, slot);
	}
	
	public Player GetPlayerAtSlot(int slot) {
		return new Player(players[slot]);
	}
	
	public boolean IsFull() {
		return GetFirstVacantSlot() == NO_VACANT_SLOTS;
	}
	
	public boolean CurrentlyRunning() {
		return currentTurn != DEALER_SLOT;
	}
	
	public boolean IsSlotVacant(int slot) {
		return IsSlotInbounds(slot) && players[slot] == null;
	}
	
	public boolean AtLeastOneActivePlayer() {
		for (Player player : players) {
			if (player != null) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean IsSlotInbounds(int slot) {
		return slot >= 0 && slot < players.length;
	}
	
	private int GetFirstVacantSlot() {
		for (int index = 0; index < players.length; ++index) {
			if (players[index] == null) {
				return index;
			}
		}
		
		return NO_VACANT_SLOTS;
	}
	
	private int GetFirstOccupiedSlot() {
		for (int index = 0; index < players.length; ++index) {
			if (players[index] != null) {
				return index;
			}
		}
		
		return NO_PLAYER_FOUND;
	}

	private void MoveToNextHand() {
		boolean anyHandsLeft = currentHand + 1 < players[currentTurn].GetNumberOfHands();
		if (!anyHandsLeft) {
			int nextSlot = GetNextOccupiedSlot();
			if (nextSlot != DEALER_SLOT) {
				SetCurrentTurn(nextSlot);
			} else {
				EndGame();
			}
		} else {
			currentHand++;
			currentSlotChangedEvent.Fire(currentTurn, currentHand);
		}
	}
	
	private int GetNextOccupiedSlot() {
		for (int slot = currentTurn + 1; slot < MAX_SLOTS; ++slot) {
			if (players[slot] != null) {
				return slot;
			}
		}
		
		return DEALER_SLOT;
	}
	
	private void EndGame() {
		GiveDealerCards();
		currentTurn = DEALER_SLOT;
		gameEndedEvent.Fire();
	}
	
	private void DealStarterHands() {
		int slot = 0;
		for (Player player : players) {
			if (player != null) {
				PlayerHand hand = new PlayerHand(player.GetInitialBet());
				hand.AddCard(deck.PickRandomCard());
				hand.AddCard(deck.PickRandomCard());
				
				player.RemoveHands();
				player.AddHand(hand);
				playerStateUpdatedEvent.Fire(player, slot);
			}
			slot++;
		}		
	}
}
