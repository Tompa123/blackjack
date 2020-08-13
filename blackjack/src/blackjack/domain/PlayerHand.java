package blackjack.domain;

public class PlayerHand extends Hand {
	private int bet;
	private boolean doubleDown;
	
	public PlayerHand(int bet) {
		super();
		this.bet = bet;
		this.doubleDown = false;
	}
	
	public PlayerHand(PlayerHand otherHand) {
		super(otherHand);
		bet = otherHand.bet;
		doubleDown = otherHand.doubleDown;
	}
	
	public int GetBet() {
		return bet;
	}
	
	public void DoubleDown() {
		if (doubleDown) {
			return;
		}
		
		doubleDown = true;
		bet *= 2;
	}
	
	public boolean CanBeSplit() {
		return cards.size() == 2 && cards.getFirst().Rank() == cards.getLast().Rank();
	}
	
	public PairOfHands Split() {
		if (cards.size() != 2) {
			throw new RuntimeException("A hand must have exactly 2 cards in order to be split correctly. " +
									   "Use the method 'CanBeSplit()' before splitting a hand.");
		}
		
		PlayerHand firstHand = new PlayerHand(bet);
		PlayerHand secondHand = new PlayerHand(bet);
		
		firstHand.AddCard(cards.getFirst());
		secondHand.AddCard(cards.getLast());
		
		return new PairOfHands(firstHand, secondHand);
	}
	
	public boolean HasDoubledDown() {
		return doubleDown;
	}
}
