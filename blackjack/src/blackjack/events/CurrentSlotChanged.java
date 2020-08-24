package blackjack.events;

import java.util.LinkedList;


public class CurrentSlotChanged {
	private LinkedList<CurrentSlotChangedListener> listeners = new LinkedList<CurrentSlotChangedListener>();
	
	public void Subscribe(CurrentSlotChangedListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	public void Unsubscribe(CurrentSlotChangedListener listener) {
		listeners.remove(listener);
	}
	
	public void Fire(int slot, int hand) {
		for (CurrentSlotChangedListener listener : listeners) {
			listener.SlotChanged(slot, hand);
		}
	}
}
