package widgets;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import blackjack.domain.Game;
import blackjack.domain.HandState;
import blackjack.domain.Player;

public class PlayerSlotsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanel[] slotPanels;
	private PlayerPanel[] playerPanels;
	private int highlightedSlot = -1;
	private int highlightedHand = 0;
	
	public PlayerSlotsPanel(int slots) {
		FlowLayout mainLayout = new FlowLayout();
		mainLayout.setHgap(30);
		slotPanels = new JPanel[slots];
		playerPanels = new PlayerPanel[slots];
		
		for (int slot = 0; slot < slots; ++slot) {
			JPanel slotPanel = new JPanel();
			slotPanel.setOpaque(false);
			add(slotPanel);
			slotPanels[slot] = slotPanel;
			displaySlotAsVacant(slot);
		}
		
		setOpaque(false);
		setLayout(mainLayout);
	}
	
	public void displayResults(Game game) {
		for (int slot = 0; slot < Game.MAX_SLOTS; ++slot) {
			if (!game.IsSlotVacant(slot)) {
				Player player = game.GetPlayerAtSlot(slot);
				for (int hand = 0; hand < player.GetNumberOfHands(); ++hand) {
					HandState result = game.GetHandState(slot, hand);
					PlayerPanel panel = playerPanels[slot];
					if (panel != null) {
						panel.displayResultAs(hand, result);
					}
				}
			}
		}
	}
	
	public void displayPlayer(Player player, int slot) {
		if (slot >= 0 && slot < slotPanels.length) {
			PlayerPanel panel = new PlayerPanel(player);
			JPanel slotPanel = slotPanels[slot];
			slotPanel.removeAll();
			slotPanel.add(panel);
			playerPanels[slot] = panel;
			
			if (slot == highlightedSlot) {
				highlight(slot, highlightedHand);
			}
		}
	}
	
	public Player getPlayerFromSlot(int slot) {
		if (slot < 0 || slot >= playerPanels.length || playerPanels[slot] == null) {
			throw new IllegalArgumentException("Attempted to fetch player from a slot where none exist.");
		}
		
		return playerPanels[slot].getPlayer();
	}
	
	public void highlight(int slot, int hand) {
		if (slot < 0 || slot >= slotPanels.length) {
			throw new IllegalArgumentException("Attempted to display an out-of-bounds slot.");
		}
		
		for (int i = 0; i < slotPanels.length; ++i) {
			if (slotPanels[i].getComponentCount() <= 0) {
				continue;
			}
			
			Component slotPanel = slotPanels[i].getComponent(0);
			if (slotPanel instanceof PlayerPanel) {
				PlayerPanel playerPanel = (PlayerPanel) slotPanel;
				if (i == slot) {
					playerPanel.highlight(hand);
				} else {
					playerPanel.removeHighlight();
				}
			}
		}
		
		highlightedSlot = slot;
		highlightedHand = hand;
	}
	
	public void removeHighlight() {
		for (JPanel panel : slotPanels) {
			if (panel.getComponent(0) instanceof PlayerPanel) {
				PlayerPanel player = (PlayerPanel) panel.getComponent(0);
				player.removeHighlight();
			}
		}
	}
	
	private void displaySlotAsVacant(int slot) {
		if (slot < 0 || slot >= slotPanels.length) {
			throw new IllegalArgumentException("Attempted to display an out-of-bounds slot.");
		}
		
		slotPanels[slot].removeAll();
		JLabel label = new JLabel("Vacant Slot");
		label.setForeground(GraphicsSettings.TEXT_COLOR);
		label.setAlignmentX(CENTER_ALIGNMENT);
		slotPanels[slot].add(label);
		playerPanels[slot] = null;
	}
}
