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
	
	public PlayerHand GetHand(int index) {
		if (!IsValidHandIndex(index)) {
			throw new IllegalArgumentException("Attempted to get a hand (at index %s) out of bounds. Use the method 'GetNumberOfHands()' "
									   		 + "before accessing any hands of a player.");
		}
		
		return new PlayerHand(hands.get(index));
	}
	
	public boolean IsBusted(int hand) {
		return hands.get(hand).IsBusted();
	}
	
	public void AddCardToHand(Card card, int hand) {
		hands.get(hand).AddCard(card);
	}
	
	public void AddHand(PlayerHand hand) {
		hands.add(new PlayerHand(hand));
	}
	
	public void DoubleDown(int handIndex) {
		if (IsValidHandIndex(handIndex)) {
			hands.get(handIndex).DoubleDown();
		}
	}
	
	public void SplitHand(int handIndex) {		
		PlayerHand hand = GetHand(handIndex);
		PairOfHands split = hand.Split();
		hands.add(split.firstHand);
		hands.add(split.secondHand);
		hands.remove(handIndex);
	}
	
	public void DealCardToHand(Card card, int hand) {
		hands.get(hand).AddCard(card);
	}
	
	private boolean IsValidHandIndex(int index) {
		return index >= 0 && index < hands.size();
	}
}
