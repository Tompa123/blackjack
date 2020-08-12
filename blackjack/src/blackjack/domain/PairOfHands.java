package blackjack.domain;

public class PairOfHands {
	public Hand firstHand;
	public Hand secondHand;
	
	public PairOfHands(Hand firstHand, Hand secondHand) {
		this.firstHand = firstHand;
		this.secondHand = secondHand;
	}
}
