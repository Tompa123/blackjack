package widgets;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import blackjack.domain.Action;
import blackjack.domain.Deck;
import blackjack.domain.Game;
import blackjack.domain.Player;
import blackjack.domain.PlayerHand;

public class GameBoard extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage backgroundImage;
	private PlayerSlotsPanel slots;
	private HandPanel dealer;
	private Game game;
	private PlayerActionDialog dialog;
	
	public GameBoard(JFrame parent) {
		try {
			backgroundImage = ImageIO.read(new File("resources/background.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		BoxLayout mainLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
		
		Player player = new Player("John Doe", 100);
		player.AddHand(createRandomHand());

		Player player2 = new Player("Jane Doe", 100);
		player2.AddHand(createRandomHand());
		
		Player player3 = new Player("Jane Doe II", 100);
		player3.AddHand(createRandomHand());
		player3.AddHand(createRandomHand());
		
		Player player4 = new Player("Jane Doe III", 100);
		player4.AddHand(createRandomHand());
		player4.AddHand(createRandomHand());
		
		Player player5 = new Player("Jane Doe IV", 100);
		player5.AddHand(createRandomHand());
		
		slots = new PlayerSlotsPanel(5);
		slots.displayPlayer(0, player);
		slots.displayPlayer(1, player2);
		slots.displayPlayer(2, player3);
		slots.displayPlayer(3, player4);
		slots.displayPlayer(4, player5);
		
		JPanel dealerPanel = new JPanel(new FlowLayout());
		dealer = new HandPanel(createRandomHand());
		dealerPanel.add(dealer);
		dealerPanel.setOpaque(false);
		
		dialog = new PlayerActionDialog(parent);
		add(dealerPanel);
		add(slots);
		setLayout(mainLayout);
	}
	
	public void showDialog() {
		int centerX = (this.getWidth() / 2) - dialog.getWidth() / 2;
		int centerY = (this.getHeight() / 2) - dialog.getHeight() / 2;
		dialog.setBounds(centerX, centerY, dialog.getWidth(), dialog.getHeight());
		dialog.askUserForAnAction();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
		} 
	}
	
	private static PlayerHand createRandomHand() {
		Deck deck = new Deck();
		PlayerHand hand = new PlayerHand(10);
		
		for (int i = 0; i < pickNumberBetween(1, 4); ++i) {
			hand.AddCard(deck.PickRandomCard());
		}
		
		return hand;
	}
	
	private static int pickNumberBetween(int min, int max) {
		Random rng = new Random();
		return rng.nextInt(max) % max + min;
	}
}
