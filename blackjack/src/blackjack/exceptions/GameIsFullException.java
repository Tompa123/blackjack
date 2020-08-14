package blackjack.exceptions;

public class GameIsFullException extends RuntimeException {
	public GameIsFullException(String message) {
		super(message);
	}
}
