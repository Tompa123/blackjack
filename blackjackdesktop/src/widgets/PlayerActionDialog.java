package widgets;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import blackjack.domain.Action;

public class PlayerActionDialog extends JDialog {
	private JButton stand = new JButton("Stand");
	private JButton hit = new JButton("Hit");
	private JButton split = new JButton("Split");
	private JButton doubleDown = new JButton("Double down");
	private Action answer = Action.Stand;
	
	public PlayerActionDialog(Frame o) {
		super(o, "Make a Choice");
		setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
		JPanel mainPanel = new JPanel();
		JPanel buttons = new JPanel(new FlowLayout());
		JLabel description = new JLabel("What would you like to do?");
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		description.setAlignmentX(CENTER_ALIGNMENT);
		
		buttons.add(hit);
		buttons.add(stand);
		buttons.add(split);
		buttons.add(doubleDown);
		connectButtonsToAnswer();
		
		mainPanel.add(description);
		mainPanel.add(buttons);
		add(mainPanel);
		pack();
	}
	
	public Action askUserForAnAction() {
		setVisible(true);
		return answer;
	}
	
	private void connectButtonsToAnswer() {
		hit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectAnswer(Action.Hit);
			}
		});
		
		stand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectAnswer(Action.Stand);
			}
		});
		
		split.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectAnswer(Action.Split);
			}
		});
		
		doubleDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectAnswer(Action.DoubleDown);
			}
		});
	}
	
	private void selectAnswer(Action action) {
		answer = action;
		close();
	}
	
	private void close() {
		setVisible(false);
		dispose();
	}
}
