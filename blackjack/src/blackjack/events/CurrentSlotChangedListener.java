package blackjack.events;

public interface CurrentSlotChangedListener {
	public void SlotChanged(int slot, int hand);
}
