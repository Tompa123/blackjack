package blackjack.domain;

public class PairOfHands {
	public final PlayerHand firstHand;
	public final PlayerHand secondHand;
	
	public PairOfHands(PlayerHand firstHand, PlayerHand secondHand) {
		this.firstHand = firstHand;
		this.secondHand = secondHand;
	}
}
