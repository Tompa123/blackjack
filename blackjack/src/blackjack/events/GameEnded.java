package blackjack.events;

import java.util.LinkedList;

import blackjack.domain.Hand;

public class GameEnded {
	private LinkedList<GameEndedListener> listeners = new LinkedList<GameEndedListener>();
	
	public void Subscribe(GameEndedListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	public void Unsubscribe(GameEndedListener listener) {
		listeners.remove(listener);
	}
	
	public void Fire() {
		for (GameEndedListener listener : listeners) {
			listener.GameEnded();
		}
	}
}
