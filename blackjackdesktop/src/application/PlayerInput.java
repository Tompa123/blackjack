package application;

import java.awt.Frame;

import javax.swing.JFrame;

import blackjack.domain.Action;
import blackjack.domain.Player;
import blackjack.domain.PlayerHand;
import widgets.BetDialog;
import widgets.PlayerActionDialog;

public class PlayerInput implements ActionInput {
	private Frame parentFrame;
	
	public PlayerInput(Frame parent) {
		parentFrame = parent;
	}
	
	public Action GetAction(Player player, PlayerHand hand) {
		PlayerActionDialog dialog = new PlayerActionDialog(parentFrame);
		if (!hand.CanBeSplit()) {
			dialog.disableSplitting();
		}
		
		return dialog.askUserForAnAction();
	}
	
	public int GetInitialBet(Player player) {
		BetDialog betDialog = new BetDialog(parentFrame, player);
		return betDialog.askForInitialBet();
	}
}
