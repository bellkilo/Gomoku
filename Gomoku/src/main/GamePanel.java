package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {
	private JMenuBar jmb = null;
	private GameFrame mainFrame = null;

	private int gameStatus = 0; // 0 not starts, 1 in game, 2 game ends

	private final int ROWS = 15;
	private final int COLS = 15;

	private int start = 26; // start point (26,26)
	private int distance = 40; // one cell 40

	// the array of pointers
	private Pointer[][] pointers = new Pointer[ROWS][COLS];

	// the array of pieces
	public int[][] allPieces = new int[ROWS][COLS]; // 0 no piece, 1 black, 2 white
	
	private Player[] players = new Player[2];
	private int depth = 0;
	
	private int playerNow = 1; // 1 player1, 2 player2
	private int winner = 0;
	
	private class DebugThread extends Thread{
		//constructor
		public void run() {
			while(true) {
				System.out.println("Player now : "+ playerNow);
				System.out.println("Player now type : "+ players[playerNow-1]);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private class AIThread extends Thread{
		
		//constructor
		public void run() {
			System.out.println("detecting");
			while(true) {
				if(players[playerNow-1] != Player.HUMAIN && gameStatus == 1){
					depth = 0;
					if(players[playerNow-1] == Player.EASY) {
						depth = 3;
					}else if (players[playerNow-1] == Player.MEDIUM) {
						depth = 5;
					}else if (players[playerNow-1] == Player.HARD) {
						depth = 7;
					}
					GameTree playerAI = new GameTree(allPieces, playerNow, depth);
					int[] move = playerAI.AIgame();
					System.out.println(move);
					setPiece(move[0], move[1]);
	 			}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// constructor
	public GamePanel(GameFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.setLayout(null);
		this.setOpaque(false);
		// create menu
		createMenu();
		// create mouse listener
		createMouseListener();
		// complete the array of pointers
		createPointers();
		
		players[0] = Player.HUMAIN;
		players[1] = Player.HUMAIN;
		DebugThread debugThread = new DebugThread();
		debugThread.start();
		AIThread AIThread1 = new AIThread();
		AIThread1.start();
		
		
	}

	private void createPointers() {
		Pointer pointer = null;
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				pointer = new Pointer(i, j, start, distance);
				pointers[i][j] = pointer;
			}
		}

	}

	private void createMouseListener() {
		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (gameStatus != 1 || players[playerNow-1] != Player.HUMAIN)
					return;
				Pointer pointer = null;
				int x = e.getX();
				int y = e.getY();
			
				for (int i = 0; i < ROWS; i++) { 
					for (int j = 0; j < COLS; j++) { 
						pointer = pointers[i][j]; 
						if(pointer.isPointed(x, y) && !pointer.hasPiece()) { //find where is the pointer
							pointer.setHasPiece(true);
							setPiece(i,j);
							repaint();
						return; 
						}
					} 	
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				if (gameStatus != 1)
					return;
				Pointer pointer = null;
				// get the coordinates of the mouse
				int x = e.getX();
				int y = e.getY();
				for (int i = 0; i < ROWS; i++) {
					for (int j = 0; j < COLS; j++) {
						pointer = pointers[i][j];
						pointer.setShow(pointer.isPointed(x, y));
					}
				}

				repaint();
			}
		};

		addMouseMotionListener(mouseAdapter);
		addMouseListener(mouseAdapter);

	}

	private void setPiece(int i, int j) {
		allPieces[i][j] = playerNow;
		playerNow = 3 - playerNow;
		System.out.println("player now " + playerNow);
		if(gameIsEnd()) {
			gameStatus = 2;
			
		}
	}

	private boolean gameIsEnd() {
		if(hasFiveInARow(1)) {
			winner = 1;
			GameEndsDialog ged = new GameEndsDialog(mainFrame, winner);
			return true;
		}else if(hasFiveInARow(2)) {
			winner = 2;
			GameEndsDialog ged = new GameEndsDialog(mainFrame, winner);
			return true;
		}

		return false;
	}
	
	// 检查是否有连续的五个子
    public boolean hasFiveInARow(int player) {
        // 检查每个位置的水平方向是否有连续的五个子
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 11; j++) {
                if (allPieces[i][j] == player && allPieces[i][j+1] == player && allPieces[i][j+2] == player && allPieces[i][j+3] == player && allPieces[i][j+4] == player) {
                    return true;
                }
            }
        }

        // 检查每个位置的垂直方向是否有连续的五个子
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 15; j++) {
                if (allPieces[i][j] == player && allPieces[i+1][j] == player && allPieces[i+2][j] == player && allPieces[i+3][j] == player && allPieces[i+4][j] == player) {
                    return true;
                }
            }
        }

        // 检查每个位置的左上到右下方向是否有连续的五个子
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (allPieces[i][j] == player && allPieces[i+1][j+1] == player && allPieces[i+2][j+2] == player && allPieces[i+3][j+3] == player && allPieces[i+4][j+4] == player) {
                    return true;
                }
            }
        }

        // 检查每个位置的右上到左下方向是否有连续的五个子
        for (int i = 0; i < 11; i++) {
            for (int j = 4; j < 15; j++) {
                if (allPieces[i][j] == player && allPieces[i+1][j-1] == player && allPieces[i+2][j-2] == player && allPieces[i+3][j-3] == player && allPieces[i+4][j-4] == player) {
                    return true;
                }
            }
        }

        return false;
    }

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// draw grid
		drawGrid(g);
		// draw five point
		draw5Point(g);
		// draw pointers
		drawPointer(g);
		// draw pieces
		drawPiece(g);
	}

	private void drawPiece(Graphics g) {
		int x = 0;
		int y = 0;
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				x = start + j * distance;
				y = start + i * distance;
				if (allPieces[i][j] == 1) {
					// black
					g.setColor(Color.BLACK);
					g.fillArc(x - 15, y - 15, 30, 30, 0, 360);
				} else if (allPieces[i][j] == 2) {
					// black
					g.setColor(Color.WHITE);
					g.fillArc(x - 15, y - 15, 30, 30, 0, 360);
				}

			}
		}
	}

	// draw pointers
	private void drawPointer(Graphics g) {
		Pointer pointer = null;
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				pointer = pointers[i][j];
				if (!(pointer == null) && pointer.isShow() && !pointer.hasPiece()) {
					pointer.draw(g);
				}
			}
		}
	}

	// draw five point
	private void draw5Point(Graphics g) {
		// first point (3,3) top left
		int x = 142; // 26 + 3*40 - 4
		int y = 142; // 26 + 3*40 - 4
		g.fillArc(x, y, 8, 8, 0, 360);
		// second point (3,11) top right
		x = 462; // 26 + 11*40 -4
		g.fillArc(x, y, 8, 8, 0, 360);

		// third point (11,11) bottom right
		y = 462; // 26 + 11*40 -4
		g.fillArc(x, y, 8, 8, 0, 360);

		// fourth point (11,3) bottom left
		x = 142; // 26 + 11*40 -4
		g.fillArc(x, y, 8, 8, 0, 360);

		// fifth point (8,8) center
		x = 302; // 26 + 7*40 -4
		y = 302;
		g.fillArc(x, y, 8, 8, 0, 360);
	}

	// draw grid
	private void drawGrid(Graphics g) {
		int x1 = 0;
		int y1 = 0;
		int x2 = 0;
		int y2 = 0;

		// draw 15 rows
		x1 = start;
		x2 = start + (COLS - 1) * distance;
		for (int i = 0; i < ROWS; i++) {
			y1 = i * distance + start;
			y2 = y1;
			g.drawLine(x1, y1, x2, y2);
		}

		// draw 15 columns
		y1 = start;
		y2 = start + (ROWS - 1) * distance;
		for (int i = 0; i < ROWS; i++) {
			x1 = i * distance + start;
			x2 = x1;
			g.drawLine(x1, y1, x2, y2);
		}

	}

	private Font createFont() {
		return new Font("Arial", Font.BOLD, 18);
	}

	// create menu
	private void createMenu() {
		// create JMenuBar
		jmb = new JMenuBar();
		// obtain a font
		Font font = createFont();
		// create the option 'game'
		JMenu jMenu1 = new JMenu("game");
		jMenu1.setFont(font);
		// create the option 'help'
		JMenu jMenu2 = new JMenu("help");
		jMenu2.setFont(font);

		jmb.add(jMenu1);
		jmb.add(jMenu2);

		JMenuItem jmi1_1 = new JMenuItem("new game");
		jmi1_1.setFont(font);
		JMenuItem jmi1_2 = new JMenuItem("quit");
		jmi1_2.setFont(font);
		// add jmenu1_1 and jmenu1_2
		jMenu1.add(jmi1_1);
		jMenu1.add(jmi1_2);

		JMenuItem jmi2_1 = new JMenuItem("operation");
		jmi2_1.setFont(font);
		JMenuItem jmi2_2 = new JMenuItem("rules");
		jmi2_2.setFont(font);
		// add jmenu2_1 and jmenu2_2
		jMenu2.add(jmi2_1);
		jMenu2.add(jmi2_2);

		mainFrame.setJMenuBar(jmb);

		// add Action listener
		jmi1_1.addActionListener(this);
		jmi1_2.addActionListener(this);
		jmi2_1.addActionListener(this);
		jmi2_1.addActionListener(this);

		// set command
		jmi1_1.setActionCommand("restart");
		jmi1_2.setActionCommand("exit");
		jmi2_1.setActionCommand("help");
		jmi2_1.setActionCommand("rule");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(".....");
		String command = e.getActionCommand();
		System.out.println("......");
		switch(command) {
			case "restart":
				for (int i = 0; i < ROWS; i++) {
				    Arrays.fill(allPieces[i], 0);
				    for(Pointer p : pointers[i]) {
				    	p.init();
				    }
				}
				ModeSelectDialog msd = new ModeSelectDialog(mainFrame);
				players = msd.getPlayers();
				gameStatus = 1;
				break;
			case "exit":
				System.exit(gameStatus);
				break;
			
		
		}
	}
}
