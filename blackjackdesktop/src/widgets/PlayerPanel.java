package widgets;

import java.awt.Color;
import java.awt.Component;
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
	private BetDisplay betDisplay;
	
	public PlayerPanel(Player player) {
		this.player = player;
		this.name = new JLabel(player.GetName());
		this.name.setAlignmentX(CENTER_ALIGNMENT);
		this.name.setForeground(GraphicsSettings.TEXT_COLOR);
		
		handsPanel = new JPanel();
		handsPanel.setLayout(new FlowLayout());
		handsPanel.setOpaque(false);
		
		betDisplay = new BetDisplay(player.GetTotalBet());
		
		add(handsPanel);
		add(this.name);
		add(betDisplay);
		for (int i = 0; i < player.GetNumberOfHands(); ++i) {
			addHand(player.GetHand(i));
		}
		
		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public void addHand(Hand hand) {
		HandPanel handPanel = new HandPanel(hand);
		handsPanel.add(handPanel);
		revalidate();
	}
	
	public void highlight(int hand) {
		if (hand < 0 || hand > handsPanel.getComponentCount()) {
			throw new IllegalArgumentException("Attempt to highlight a non-existent hand panel.");
		}

		for (int i = 0; i < handsPanel.getComponentCount(); ++i) {
			if (!(handsPanel.getComponent(i) instanceof HandPanel)) {
				continue;
			}
			
			HandPanel handPanel = (HandPanel) handsPanel.getComponent(i);
			if (i == hand) {
				handPanel.highlight();
			} else {
				handPanel.removeHighlight();
			}
		}
		
		name.setForeground(GraphicsSettings.HIGHLIGHT_COLOR);
	}
	
	public void removeHighlight() {
		for (Component panel : handsPanel.getComponents()) {
			if (!(panel instanceof HandPanel)) {
				continue;
			}
			
			HandPanel handPanel = (HandPanel) panel;
			if (handPanel != null) {
				handPanel.removeHighlight();
			}
		}
		
		name.setForeground(GraphicsSettings.TEXT_COLOR);
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
