package widgets;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import blackjack.domain.Card;
import blackjack.domain.Hand;
import blackjack.domain.Player;

public class PlayerPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Player player;
	private JLabel name;
	private JPanel handsPanel;
	
	public PlayerPanel(Player player) {
		this.player = player;
		this.name = new JLabel(player.GetName());
		this.name.setAlignmentX(CENTER_ALIGNMENT);
		
		handsPanel = new JPanel();
		handsPanel.setLayout(new FlowLayout());
		
		add(this.name);
		add(handsPanel);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		for (int i = 0; i < player.GetNumberOfHands(); ++i) {
			addHand(player.GetHand(i));
		}
	}
	
	public void addHand(Hand hand) {
		HandPanel handPanel = new HandPanel(hand);
		handsPanel.add(handPanel);
		revalidate();
	}
	
	public void addCardToHand(Card card, int hand) {
		if (hand >= 0 && hand < player.GetNumberOfHands()) {
			HandPanel panel = (HandPanel)handsPanel.getComponent(hand);
			panel.addCard(card);
		}
	}
	
	public Player getPlayer() {
		return new Player(player);
	}
}
