package widgets;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import blackjack.domain.Card;
import blackjack.domain.Hand;
import blackjack.domain.HandState;
import blackjack.domain.Player;

public class PlayerPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Player player;
	private JLabel name;
	private JPanel handContainer;
	private BetDisplay betDisplay;
	private LinkedList<HandPanel> handPanels = new LinkedList<HandPanel>();
	
	public PlayerPanel(Player player) {
		this.player = player;
		name = new JLabel(player.GetName());
		name.setAlignmentX(CENTER_ALIGNMENT);
		name.setForeground(GraphicsSettings.TEXT_COLOR);
		
		handContainer = new JPanel();
		handContainer.setLayout(new FlowLayout());
		handContainer.setOpaque(false);
		
		betDisplay = new BetDisplay(player.GetTotalBet());
		
		add(handContainer);
		add(name);
		add(betDisplay);
		for (int i = 0; i < player.GetNumberOfHands(); ++i) {
			addHand(player.GetHand(i));
		}
		
		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public void displayResultAs(int hand, HandState handState) {
		if (hand < 0 || hand >= handPanels.size()) {
			throw new IllegalArgumentException("Attempted to display result on a non-existent hand (%d).".formatted(hand));
		}
		
		HandPanel panel = handPanels.get(hand);
		panel.displayResult(handState);
	}
	
	public void addHand(Hand hand) {
		HandPanel handPanel = new HandPanel(hand);
		handContainer.add(handPanel);
		handPanels.add(handPanel);
		revalidate();
	}
	
	public void highlight(int hand) {
		if (hand < 0 || hand > handPanels.size()) {
			throw new IllegalArgumentException("Attempt to highlight a non-existent hand panel (%d).".formatted(hand));
		}

		for (int i = 0; i < handPanels.size(); ++i) {
			HandPanel handPanel = handPanels.get(i);
			if (i == hand) {
				handPanel.highlight();
			} else {
				handPanel.removeHighlight();
			}
		}
		
		name.setForeground(GraphicsSettings.HIGHLIGHT_COLOR);
	}
	
	public void removeHighlight() {
		for (HandPanel handPanel : handPanels) {
			handPanel.removeHighlight();
		}
		
		name.setForeground(GraphicsSettings.TEXT_COLOR);
	}
	
	public void addCardToHand(Card card, int hand) {
		if (hand >= 0 && hand < player.GetNumberOfHands()) {
			HandPanel panel = (HandPanel)handContainer.getComponent(hand);
			panel.addCard(card);
		}
	}
	
	public Player getPlayer() {
		return new Player(player);
	}
}
