package application;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import blackjack.domain.Action;
import blackjack.domain.Player;
import blackjack.domain.PlayerHand;

public class ComputerInput implements ActionInput {
	public Action GetAction(Player player, PlayerHand hand) {
		
		try {
			// Wait an arbitrary amount to make it appear as if the player is "thinking"
			TimeUnit.SECONDS.sleep(pickNumberBetween(2, 5));
		} catch (InterruptedException e) {
			// Interrupting the thread isn't a problem in this case.
			e.printStackTrace();
		}
		
		if (hand.CanBeSplit()) {
			return Action.Split;
		} else if (hand.HardValue() < 15) {
			return Action.Hit;
		}
		
		return Action.Stand;
	}
	
	public int GetInitialBet(Player player) {
		return 0;
	}
	
	private int pickNumberBetween(int min, int max) {
		Random rng = new Random();
		return rng.nextInt() % max + min;
	}
}
