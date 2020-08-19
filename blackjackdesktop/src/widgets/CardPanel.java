package widgets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import blackjack.domain.*;
import java.util.*;

public class CardPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	private Card card;
	private static final HashMap<Rank, String> RANK_NAMES = initRankNames();
	private static final HashMap<Suit, String> SUIT_NAMES = initSuitNames();
	private static final String RESOURCE_FOLDER = "resources/";
	
	public CardPanel(Card card) {
		super();
		this.card = card;
		setOpaque(false);
		
		try {
			image = ImageIO.read(new File(GetCardImage(card)));
		} catch (IOException ex) {
			image = null;
		}
	}

	public Card GetCard() {
		return card;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null) {
			g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
		} 
	}
	
	private static String GetCardImage(Card card) {
		String imageName = "%s%s.png".formatted(SUIT_NAMES.get(card.Suit()), RANK_NAMES.get(card.Rank()));
		return RESOURCE_FOLDER + imageName;
	}

	private static HashMap<Rank, String> initRankNames() {
		HashMap<Rank, String> rankNames = new HashMap<Rank, String>();
		rankNames.put(Rank.Ace, "01");
		rankNames.put(Rank.Two, "02");
		rankNames.put(Rank.Three, "03");
		rankNames.put(Rank.Four, "04");
		rankNames.put(Rank.Five, "05");
		rankNames.put(Rank.Six, "06");
		rankNames.put(Rank.Seven, "07");
		rankNames.put(Rank.Eight, "08");
		rankNames.put(Rank.Nine, "09");
		rankNames.put(Rank.Ten, "10");
		rankNames.put(Rank.Jack, "11");
		rankNames.put(Rank.Queen, "12");
		rankNames.put(Rank.King, "13");

		return rankNames;
	}

	private static HashMap<Suit, String> initSuitNames() {
		HashMap<Suit, String> suitNames = new HashMap<Suit, String>();

		suitNames.put(Suit.Clubs, "Club");
		suitNames.put(Suit.Diamonds, "Diamond");
		suitNames.put(Suit.Hearts, "Heart");
		suitNames.put(Suit.Spades, "Spade");

		return suitNames;
	}
}
