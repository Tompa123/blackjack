package blackjack.events;

import java.util.LinkedList;

import blackjack.domain.Player;

public class PlayerStateUpdated {
	private LinkedList<PlayerUpdateListener> listeners = new LinkedList<PlayerUpdateListener>();
	
	public void Subscribe(PlayerUpdateListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	public void Unsubscribe(PlayerUpdateListener listener) {
		listeners.remove(listener);
	}
	
	public void Fire(Player player, int slot) {
		for (PlayerUpdateListener listener : listeners) {
			listener.PlayerUpdated(new Player(player), slot);
		}
	}
}
