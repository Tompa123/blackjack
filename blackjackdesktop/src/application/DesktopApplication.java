package application;

import blackjack.domain.*;
import widgets.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

public class DesktopApplication {
	public static void main(String[] args) {
		//JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("My First GUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 300);
		frame.setBackground(Color.RED);
		
		JPanel mainPanel = new JPanel();
		FlowLayout mainLayout = new FlowLayout();
		mainLayout.setHgap(20);
		mainPanel.setLayout(mainLayout);
		
		PlayerPanel player = new PlayerPanel(new Player("John Doe", 100));
		player.addHand(createRandomHand());

		PlayerPanel player2 = new PlayerPanel(new Player("Jane Doe", 100));
		player2.addHand(createRandomHand());
		
		PlayerPanel player3 = new PlayerPanel(new Player("Jane Doe II", 100));
		player3.addHand(createRandomHand());
		player3.addHand(createRandomHand());
		
		mainPanel.add(player);
		mainPanel.add(player2);
		mainPanel.add(player3);
		frame.add(mainPanel);
		frame.pack();
		frame.setVisible(true);
	}
	
	private static Hand createRandomHand() {
		Deck deck = new Deck();
		Hand hand = new Hand();
		
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
 