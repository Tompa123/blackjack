package blackjack.events;

import blackjack.domain.Hand;

public interface DealerUpdatedListener {
	public void HandUpdated(Hand hand);
}
