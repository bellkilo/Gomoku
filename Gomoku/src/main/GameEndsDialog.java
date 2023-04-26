package main;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameEndsDialog extends JDialog{
	
	public GameEndsDialog(JFrame parent, int winner) {
		super(parent, "game ends", true);
		JPanel panel = new JPanel();
		JLabel label = new JLabel("winner is player " + winner+". Congratulations!!");
		panel.add(label);
		
		getContentPane().add(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
	}
}
