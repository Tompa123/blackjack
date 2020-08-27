package widgets;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import blackjack.domain.Player;

public class BetDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private JSpinner betInput;
	private JButton confirmButton = new JButton("Place Bet");
	private SpinnerNumberModel bet;
	
	public BetDialog(Frame parent, Player player) {
		super(parent, "Initial Bets");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		JLabel description = new JLabel("Place your bets.");;
		description.setAlignmentX(CENTER_ALIGNMENT);
		
		bet = new SpinnerNumberModel(1, 1, player.GetBalance(), 1);
		betInput = new JSpinner(bet);
		mainPanel.add(description);
		mainPanel.add(betInput);
		mainPanel.add(confirmButton);
		setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
		
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		add(mainPanel);
		pack();
	}
	
	public int askForInitialBet() {
		int centerX = (getParent().getWidth() / 2) - this.getWidth() / 2;
		int centerY = (getParent().getHeight() / 2) - this.getHeight() / 2;
		setBounds(centerX, centerY, this.getWidth(), this.getHeight());
		setVisible(true);
		dispose();
		return (int) bet.getNumber();
	}
}
