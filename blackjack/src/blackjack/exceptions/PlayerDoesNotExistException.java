package blackjack.exceptions;

public class PlayerDoesNotExistException extends RuntimeException {
	public PlayerDoesNotExistException(String message) {
		super(message);
	}
}
