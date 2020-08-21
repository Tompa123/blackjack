package widgets;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import blackjack.domain.*;

public class HandPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLayeredPane cardContainer;
	private JLabel handValue;
	private Hand hand;
	private int cardWidth = 125;
	
	public HandPanel(Hand hand) {
		this();
		for (int i = 0; i < hand.GetNumberOfCards(); ++i) {
			addCard(hand.GetCard(i));
		}
	}
	
	public HandPanel() {
		hand = new Hand();
		handValue = new JLabel("0");
		handValue.setAlignmentX(CENTER_ALIGNMENT);
		handValue.setForeground(GraphicsSettings.TEXT_COLOR);
		cardContainer = new JLayeredPane();
		cardContainer.setPreferredSize(new Dimension(cardWidth, 300));
		cardContainer.setOpaque(false);

		add(handValue, SwingConstants.CENTER);
		add(cardContainer);

		setVisible(true);
		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public void addCard(Card card) {
		CardPanel cardImage = new CardPanel(card);
		cardImage.setBounds(0, cardContainer.getComponentCount() * 20, cardWidth, 200);

		hand.AddCard(card);
		displayHandValue();
		cardContainer.add(cardImage, Integer.valueOf(cardContainer.getComponentCount()));
	}
	
	public void removeCards() {
		cardContainer.removeAll();
	}
	
	private void displayHandValue() {
		if (hand.IsBlackJack()) {
			handValue.setText("BLACKJACK");
		} else if (hand.HardValue() != hand.SoftValue() && hand.SoftValue() <= Hand.BLACK_JACK_LIMIT) {
			handValue.setText("%d/%d".formatted(hand.HardValue(), hand.SoftValue()));
		} else {
			handValue.setText(Integer.toString(hand.HardValue()));
		}
	}
}
