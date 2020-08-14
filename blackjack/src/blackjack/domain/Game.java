package blackjack.domain;

import java.util.Random;

import blackjack.exceptions.*;

public class Game {
	private Player[] players;
	private int currentTurn;
	private Deck deck;
	private Hand dealer;
	private static int NO_VACANT_SLOTS = -1;
	private static int NO_PLAYER_FOUND = -1;
	public static int MAX_SLOTS = 5;
	
	public Game() {
		players = new Player[MAX_SLOTS];
		currentTurn = NO_PLAYER_FOUND;
		deck = new Deck();
		dealer = new Hand();
	}
	
	public void Start() {
		if (!AtLeastOneActivePlayer()) {
			throw new NoActivePlayersException("Attempted to start a game with no players in it." +
												"Use the method 'AtLeastOneActivePlayer()' to make sure there are players before starting the game.");
		}
		
		dealer.AddCard(deck.PickRandomCard());
		currentTurn = GetFirstPlayer();
		DealStarterHands();
	}
	
	public Hand GetDealerHand() {
		return new Hand(dealer);
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
	}
	
	public void AddPlayerToFirstVacantSlot(Player player) {
		if (IsFull()) {
			throw new GameIsFullException("Attempted to add a player to full game. Use the 'IsFull()' method before attempting to " +
										  "add a player to a game.");
		}
		
		int vacantSlot = GetFirstVacantSlot();
		AddPlayerToSlot(player, vacantSlot);
	}
	
	public Player GetPlayerAtSlot(int slot) {
		return new Player(players[slot]);
	}
	
	public boolean IsFull() {
		return GetFirstVacantSlot() == NO_VACANT_SLOTS;
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
	
	private int GetFirstPlayer() {
		for (int index = 0; index < players.length; ++index) {
			if (players[index] != null) {
				return index;
			}
		}
		
		return NO_PLAYER_FOUND;
	}
	
	private void DealStarterHands() {
		for (Player player : players) {
			if (player != null) {
				PlayerHand hand = new PlayerHand(100);
				hand.AddCard(deck.PickRandomCard());
				hand.AddCard(deck.PickRandomCard());
				player.AddHand(hand);
			}
		}		
	}
}
