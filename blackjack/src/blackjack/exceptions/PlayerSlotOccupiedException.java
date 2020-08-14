package blackjack.exceptions;

public class PlayerSlotOccupiedException extends RuntimeException {
	public PlayerSlotOccupiedException(String message) {
		super(message);
	}
}
