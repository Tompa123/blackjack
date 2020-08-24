package blackjack.events;

import java.util.LinkedList;

import blackjack.domain.Hand;

public class DealerUpdated {
	private LinkedList<DealerUpdatedListener> listeners = new LinkedList<DealerUpdatedListener>();
	
	public void Subscribe(DealerUpdatedListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	public void Unsubscribe(DealerUpdatedListener listener) {
		listeners.remove(listener);
	}
	
	public void Fire(Hand hand) {
		for (DealerUpdatedListener listener : listeners) {
			listener.HandUpdated(new Hand(hand));
		}
	}
}
