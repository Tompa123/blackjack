package widgets;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class BetDisplay extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage backgroundImage;
	private JLabel betLabel;
	private int backgroundSize = 64;
	
	public BetDisplay(int bet) {
		setLayout(new GridBagLayout());
		betLabel = new JLabel("$" + Integer.toString(bet), SwingConstants.CENTER);
		
		try {
			backgroundImage = ImageIO.read(new File("resources/coin-1-64.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setPreferredSize(new Dimension(64, 64));
		betLabel.setBounds(0, 0, this.getWidth(), this.getHeight());
		add(betLabel);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (backgroundImage != null) {
			int centerOffset = backgroundSize - backgroundSize / 2;
			g.drawImage(backgroundImage, (this.getWidth() / 2) - centerOffset, (this.getHeight() / 2) - centerOffset, 
					backgroundSize, backgroundSize, null);
		} 
	}
}
