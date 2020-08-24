package application;

import blackjack.domain.Action;
import blackjack.domain.Player;
import blackjack.domain.PlayerHand;

public interface ActionInput {
	public Action GetAction(Player player, PlayerHand hand);
	public int GetInitialBet(Player player);
}
