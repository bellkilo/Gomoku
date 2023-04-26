package main;

import java.awt.Color;

import javax.swing.JFrame;

public class GameFrame extends JFrame{
	//constructor
	public GameFrame() {
		setTitle("Gomoku");
		setSize(626,676); //set size of window
		getContentPane().setBackground(new Color(209,146,17));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //kill the thread while the window is closed
		setLocationRelativeTo(null); //placed in the middle
		setResizable(false); //forbid to change the size of window
		//setVisible(true);
	}
}
