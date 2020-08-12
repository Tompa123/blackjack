package blackjack.test;

import java.util.Random;

public class FakeRandom extends Random {
	private int notARandomNumber;
	
	public void SetNextInt(int number) {
		notARandomNumber = number;
	}
	
	@Override
	public int nextInt(int bound) {
		return notARandomNumber;
	}
}
