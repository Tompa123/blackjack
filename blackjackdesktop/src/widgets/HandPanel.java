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
		cardContainer = new JLayeredPane();
		cardContainer.setPreferredSize(new Dimension(125, 300));
		
		add(handValue, SwingConstants.CENTER);
		add(cardContainer);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setVisible(true);
	}
	
	public void addCard(Card card) {
		CardPanel cardImage = new CardPanel(card);
		cardImage.setBounds(0, cardContainer.getComponentCount() * 20, 125, 200);

		hand.AddCard(card);
		displayHandValue();
		cardContainer.add(cardImage, Integer.valueOf(cardContainer.getComponentCount()));
	}
	
	public void removeCards() {
		cardContainer.removeAll();
	}
	
	private void displayHandValue() {
		if (hand.HardValue() != hand.SoftValue()) {
			handValue.setText("%d/%d".formatted(hand.HardValue(), hand.SoftValue()));
		} else {
			handValue.setText(Integer.toString(hand.HardValue()));
		}
	}
}
