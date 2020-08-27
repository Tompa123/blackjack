package blackjack.domain;

import java.util.LinkedList;
import java.util.Objects;

import blackjack.exceptions.InsufficientBalanceException;

public class Player {
	private LinkedList<PlayerHand> hands;
	private int balance;
	private int initialBet;
	private String name;
	
	public Player(String name, int balance) {
		Objects.requireNonNull(name, "A player must be given a name upon construction.");
		hands = new LinkedList<PlayerHand>();
		this.name = name;
		this.balance = balance;
	}
	
	public Player(Player otherPlayer) {
		this(otherPlayer.name, otherPlayer.balance);
		initialBet = otherPlayer.initialBet;
		for (PlayerHand hand : otherPlayer.hands) {
			hands.add(new PlayerHand(hand));
		}
	}
	
	@Override
	public boolean equals(Object otherPlayer) {
		if (otherPlayer == null || !(otherPlayer instanceof Player)) {
			return false;
		}
		
		Player other = (Player)otherPlayer;
		return this.name == other.name;
	}
	
	public void PlaceInitialBet(int bet) {
		if (bet > balance) {
			throw new InsufficientBalanceException("Attempted to place a bet larger than what the player can afford. " +
													"Check balance with the method 'GetBalance()' before placing bets.");
		} else if (bet < 0) {
			String description = new String("Negative bets (%d) are not legal.");
			throw new IllegalArgumentException(String.format(description, bet));
		}
		
		initialBet = bet;
	}
	
	public String GetName() {
		return name;
	}
	
	public int GetInitialBet() {
		return initialBet;
	}
	
	public int GetNumberOfHands() {
		return hands.size();
	}
	
	public int GetBalance() {
		return balance;
	}
	
	public void WithdrawBet() {
		balance -= initialBet;
		initialBet = 0;
	}
	
	public int GetTotalBet() {
		int totalBet = 0;
		for (PlayerHand hand : hands) {
			totalBet += hand.GetBet();
		}
		
		return totalBet;
	}
	
	public PlayerHand GetHand(int index) {
		if (!IsValidHandIndex(index)) {
			String description = "Attempted to get a hand (at index %d) out of bounds. Use the method 'GetNumberOfHands()' "
			   		 			+ "before accessing any hands of a player.";
			throw new IllegalArgumentException(description.formatted(index));
		}
		
		return new PlayerHand(hands.get(index));
	}
	
	public boolean IsBusted(int hand) {
		if (!IsValidHandIndex(hand)) {
			throw new IllegalArgumentException("Attempted to check whether a hand at invalid index %d is busted.".formatted(hand));
		}
		
		return hands.get(hand).IsBusted();
	}
	
	public void AddCardToHand(Card card, int hand) {
		Objects.requireNonNull(card, "Cannot add a null card to a player's hand.");
		hands.get(hand).AddCard(card);
	}
	
	public void AddHand(PlayerHand hand) {
		Objects.requireNonNull(hand, "Cannot add a null hand to a player's hand.");
		if (hand.GetBet() > balance) {
			String description = "Attempted to add hand to a player whose balance is insufficient; the bet (%d) is larger than the available balance (%d).";
			throw new InsufficientBalanceException(description.formatted(hand.GetBet(), balance));
		}
		
		balance -= hand.GetBet();
		hands.add(new PlayerHand(hand));
	}
	
	public void DoubleDown(int handIndex) {
		if (!IsValidHandIndex(handIndex)) {
			String description = "Attempted to double down a non-existent hand at index %d.";
			throw new IllegalArgumentException(description.formatted(handIndex));
		}

		hands.get(handIndex).DoubleDown();
	}
	
	public PairOfHands SplitHand(int handIndex) {	
		PlayerHand hand = GetHand(handIndex);
		if (hand.GetBet() > balance) {
			throw new InsufficientBalanceException("Attempted to add hand to a player whose balance is insufficient; the bet is larger than the available balance.");
		}
		
		PairOfHands split = hand.Split();
		hands.add(split.firstHand);
		hands.add(split.secondHand);
		hands.remove(handIndex);
		balance -= hand.GetBet();
		return split;
	}
	
	public void RemoveHands() {
		hands.clear();
	}
	
	private boolean IsValidHandIndex(int index) {
		return index >= 0 && index < hands.size();
	}
}
