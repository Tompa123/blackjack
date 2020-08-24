package blackjack.events;

import blackjack.domain.Player;

public interface PlayerUpdateListener {
	public void PlayerUpdated(Player player, int slot);
}
