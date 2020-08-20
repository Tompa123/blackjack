package application;

import blackjack.domain.*;
import widgets.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

public class DesktopApplication {
	public static void main(String[] args) {
		JFrame frame = new JFrame("My First GUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 300);
		frame.setBackground(Color.RED);
		
		try {
			frame.setIconImage(ImageIO.read(new File("resources/app_icon.png")));
		} catch (IOException e) {
			System.out.println("Could not load application icon.");
			e.printStackTrace();
		}
		
		JPanel mainPanel = new JPanel();
		FlowLayout mainLayout = new FlowLayout();
		mainLayout.setHgap(20);
		mainPanel.setLayout(mainLayout);
		
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
		
		PlayerSlotsPanel slots = new PlayerSlotsPanel(5);
		slots.displayPlayer(0, player);
		slots.displayPlayer(1, player2);
		slots.displayPlayer(2, player3);
		slots.displayPlayer(3, player4);
		slots.displayPlayer(4, player5);
		
		mainPanel.add(slots);
		frame.add(mainPanel);
		frame.pack();
		frame.setVisible(true);
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
 