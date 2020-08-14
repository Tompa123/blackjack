package blackjack.exceptions;

public class NoActivePlayersException extends RuntimeException {
	public NoActivePlayersException(String message) {
		super(message);
	}
}
