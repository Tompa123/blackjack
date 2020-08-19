package application;

import blackjack.domain.*;
import widgets.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;

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
		mainPanel.setLayout(new FlowLayout());
		
		mainPanel.add(new HandPanel());
		mainPanel.add(new HandPanel());
		mainPanel.add(new HandPanel());

		frame.add(mainPanel);
		frame.pack();
		frame.setVisible(true);
	}
}
 