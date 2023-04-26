package main;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ModeSelectDialog extends JDialog {
	private Player[] players = new Player[2];;
	private JComboBox<String> comboBox1;
	private JComboBox<String> comboBox2;
	public ModeSelectDialog(JFrame parent) {
		super(parent, "Select the mode", true);
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Please choose a game mode : ");
		panel.add(label);
		
		JLabel label1 = new JLabel("Player 1:");
        comboBox1 = new JComboBox<>(new String[]{"Humain", "AI Easy", "AI Medium", "AI Hard"});
        panel.add(label1);
        panel.add(comboBox1); 
        
        JLabel label2 = new JLabel(" vs Player 2:");
        comboBox2 = new JComboBox<>(new String[]{"Humain", "AI Easy", "AI Medium", "AI Hard"});
        panel.add(label2);
        panel.add(comboBox2);
        
        JButton start = new JButton("start");
        start.addActionListener(e -> {
            setPlayer();
            dispose();
        });
        panel.add(start);
        
        getContentPane().add(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
	}
	
	private void setPlayer() {
		switch(comboBox1.getSelectedIndex()) {
			case 0:
				players[0] = Player.HUMAIN;
				break;
			case 1:
				players[0] = Player.EASY;
				break;
			case 2:
				players[0] = Player.MEDIUM;
				break;
			case 3:
				players[0] = Player.HARD;
				break;
		}
		
		switch(comboBox2.getSelectedIndex()) {
		case 0:
			players[1] = Player.HUMAIN;
			break;
		case 1:
			players[1] = Player.EASY;
			break;
		case 2:
			players[1] = Player.MEDIUM;
			break;
		case 3:
			players[1] = Player.HARD;
			break;
		}
	}
	
	public Player[] getPlayers() {
		return players;
	}
}
