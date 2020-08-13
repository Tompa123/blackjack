package blackjack.domain;

import java.util.LinkedList;

public class Player {
	private LinkedList<PlayerHand> hands;
	private int balance;
	private String name;
	
	public Player(String name, int balance) {
		hands = new LinkedList<PlayerHand>();
		this.name = name;
		this.balance = balance;
	}
	
	public int GetNumberOfHands() {
		return hands.size();
	}
	
	public PlayerHand GetHand(int index) {
		if (!IsValidHandIndex(index)) {
			throw new RuntimeException("Attempted to get a hand (at index %s) out of bounds. Use the method 'GetNumberOfHands()' "
									   + "before accessing any hands of a player.");
		}
		
		return new PlayerHand(hands.get(index));
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
