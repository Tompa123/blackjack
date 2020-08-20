package widgets;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import blackjack.domain.Player;

public class PlayerSlotsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanel[] slotPanels;
	
	public PlayerSlotsPanel(int slots) {
		FlowLayout mainLayout = new FlowLayout();
		mainLayout.setHgap(30);
		slotPanels = new JPanel[slots];
		
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
	
	public void displayPlayer(int slot, Player player) {
		if (slot >= 0 && slot < slotPanels.length) {
			PlayerPanel panel = new PlayerPanel(player);
			JPanel slotPanel = slotPanels[slot];
			slotPanel.removeAll();
			slotPanel.add(panel);
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
	}
}
