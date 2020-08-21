package application;

import blackjack.domain.*;
import blackjack.domain.Action;
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
		JFrame frame = new JFrame("Tom's Blackjack");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 300);
		frame.setBackground(Color.RED);
		
		try {
			frame.setIconImage(ImageIO.read(new File("resources/app_icon.png")));
		} catch (IOException e) {
			System.out.println("Could not load application icon.");
			e.printStackTrace();
		}

		GameBoard board = new GameBoard(frame);
		frame.add(board);
		frame.pack();
		frame.setVisible(true);
		board.showDialog();
	}
}
 