package widgets;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import application.ActionInput;
import application.ComputerInput;
import application.PlayerInput;
import blackjack.domain.*;
import blackjack.events.*;

public class GameBoard extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage backgroundImage;
	private PlayerSlotsPanel slots;
	private JPanel dealerPanel = new JPanel(new FlowLayout());
	private Game game = new Game();
	private final int REAL_PLAYER_SLOT = 0;
	private Frame parent;
	
	public GameBoard(Frame parent) {
		this.parent = parent;
		
		try {
			backgroundImage = ImageIO.read(new File("resources/background.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BoxLayout mainLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
		slots = new PlayerSlotsPanel(Game.MAX_SLOTS);
		dealerPanel.setOpaque(false);
		
		add(dealerPanel);
		add(slots);
		
		game.PlayerUpdatedEvent().Subscribe(new PlayerUpdateListener() {
			public void PlayerUpdated(Player player, int slot) {
				slots.displayPlayer(player, slot);
				parent.pack();
			}
		});
		
		game.DealerUpdatedEvent().Subscribe(new DealerUpdatedListener() {
			public void HandUpdated(Hand hand) {
				dealerPanel.removeAll();
				dealerPanel.add(new HandPanel(hand));
				parent.pack();
			}
		});
		
		game.CurrentSlotChanged().Subscribe(new CurrentSlotChangedListener() {
			public void SlotChanged(int slot, int hand) {
				ActionInput input = getInput(slot);
				Player player = game.GetPlayerAtSlot(slot);
				slots.highlight(slot, hand);
				
				Action action = input.GetAction(player, player.GetHand(hand));
				game.PerformActionOnCurrentPlayer(action);
			}
		});
		
		game.AddPlayerToSlot(new Player("You", 10000), REAL_PLAYER_SLOT);
		game.AddPlayerToSlot(new Player("John Doe", 100), 2);
		game.AddPlayerToSlot(new Player("Jane Doe", 100), 3);
		setLayout(mainLayout);
	}
	
	public void start() {
		game.StartNewRound();
	}
	
	public void showActionDialog() {
		PlayerActionDialog dialog = new PlayerActionDialog(parent);
		int centerX = (this.getWidth() / 2) - dialog.getWidth() / 2;
		int centerY = (this.getHeight() / 2) - dialog.getHeight() / 2;
		dialog.setBounds(centerX, centerY, dialog.getWidth(), dialog.getHeight());
		dialog.askUserForAnAction();
	}
	
	private ActionInput getInput(int slot) {
		if (slot == REAL_PLAYER_SLOT) {
			return new PlayerInput(this.parent);
		} else {
			return new ComputerInput();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
		} 
	}
}
