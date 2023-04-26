package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Node {
	private static String[] patternsPlayer1 = new String[] {
			"10000", "01000", "00100", "00010", "00001",
            "11000", "01100", "00110", "00011", "10100", "01010", "00101", "10010", "01001", "10001",
            "11100", "01110", "00111", "11010", "01101", "10110", "01011", "11001", "10011", "10101",
            "11110", "11101", "11011", "10111", "01111", "11111"
	};
	
	private static String[] patternsPlayer2 = new String[] {
			"20000", "02000", "00200", "00020", "00002",
            "22000", "02200", "00220", "00022", "20200", "02020", "00202", "20020", "02002", "20002",
            "22200", "02220", "00222", "22020", "02202", "20220", "02022", "22002", "20022", "20202",
            "22220", "22202", "22022", "20222", "02222", "22222"
	};
	
	private static int[] scores = new int[] {
            1, 1, 1, 1, 1,
            10, 10, 10, 10, 10, 10, 10, 10, 10, 10,
            100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
            10000, 10000, 10000, 10000, 10000, 1000000,
    };
	
	private static int expandRadius = 2;
	private int value = 0; // evaluate score
	private int depth; // 
	private int maxDepth;
	private int[] move; //last move
	private int player;
	private Node father;
	private List<Node> childrens = new Vector<Node>();
	private int[][] board;
	
	public Node(int[][]board, int player, int maxDepth) {
		depth = 0;
		this.maxDepth = maxDepth;
		this.player = player;
		father = null;
		this.board = board;
	}
	
	public Node(Node father, int[] move) {
		this.father = father;
		this.depth = father.getDepth()+1;
		maxDepth = father.getMaxDepth();
		int[][] board_f = father.getBoard();
		board = new int[board_f.length][board_f[0].length];
		for(int i = 0; i< board_f.length; i++) {
			System.arraycopy(board_f[i], 0, board[i], 0, board_f[i].length);
		}
		this.move = move;
		player = 3 - father.getPlayer();
		this.board[move[0]][move[1]] = player+1;
		value = evaluate();
			
	}
	
	public int getDepth() {
		return depth;
	}
	
	public int getPlayer() {
		return player;
	}
	
	public int[][] getBoard(){
		return board;
	}
	
	public boolean isMaxNode() {
		return depth%2 == 0;
	}
	
	public int getMaxDepth() {
		return maxDepth;
	}
	
	public Node getFather() {
		return father;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public boolean isLeaf() {
		return childrens.isEmpty();
	}
	
	public List<Node> getChildrens(){
		return childrens;
	}
	
	public int[] getMove() {
		return move;
	}
	
	private static int evaluatePlayer1(String s) {
		for (int i = 0; i < 31; i++)
            if (s.equals(patternsPlayer1[i])) return scores[i];
        return 0;
	}
	
	private static int evaluatePlayer2(String s) {
		for (int i = 0; i < 31; i++)
            if (s.equals(patternsPlayer2[i])) return scores[i];
        return 0;
	}
	
	private static String convert(int pos) {
		if (pos == 0) return "0";
		else if (pos == 1) return "1";
		else return "2";
	}
	
	public int isTerminalNode() {
		for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
            	if (j + 4 < 15) {
            		StringBuffer s = new StringBuffer("");
            		for(int k = 0; k < 5; k++) s.append(convert(board[i][j+k]));
            		if (s.toString().equals("11111")) return 1;
            		if (s.toString().equals("22222")) return 2;
            	}
            	
            	if (i + 4 < 15) {
            		StringBuffer s = new StringBuffer("");
            		for(int k = 0; k < 5; k++) s.append(convert(board[i+k][j]));
            		if (s.toString().equals("11111")) return 1;
            		if (s.toString().equals("22222")) return 2;
            	}
            	
            	if (i + 4 < 15 && j + 4 < 15) {
            		StringBuffer s = new StringBuffer("");
            		for(int k = 0; k < 5; k++) s.append(convert(board[i+k][j+k]));
            		if (s.toString().equals("11111")) return 1;
            		if (s.toString().equals("22222")) return 2;
            	}
            	
            	if (i + 4 < 15 && j - 4 >= 0) {
            		StringBuffer s = new StringBuffer("");
            		for(int k = 0; k < 5; k++) s.append(convert(board[i+k][j-k]));
            		if (s.toString().equals("11111")) return 1;
            		if (s.toString().equals("22222")) return 2;
            	}
            }
		}

		return 0;
	}
	
	
	
	public int evaluate() {
        int score = 0;

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
            	if (j + 4 < 15) {
            		StringBuffer s = new StringBuffer("");
            		for(int k = 0; k < 5; k++) s.append(convert(board[i][j+k]));
            		score += evaluatePlayer1(s.toString()) - evaluatePlayer2(s.toString());
            	}
            	
            	if (i + 4 < 15) {
            		StringBuffer s = new StringBuffer("");
            		for(int k = 0; k < 5; k++) s.append(convert(board[i+k][j]));
            		score += evaluatePlayer1(s.toString()) - evaluatePlayer2(s.toString());
            	}
            	
            	if (i + 4 < 15 && j + 4 < 15) {
            		StringBuffer s = new StringBuffer("");
            		for(int k = 0; k < 5; k++) s.append(convert(board[i+k][j+k]));
            		score += evaluatePlayer1(s.toString()) - evaluatePlayer2(s.toString());
            	}
            	
            	if (i + 4 < 15 && j - 4 >= 0) {
            		StringBuffer s = new StringBuffer("");
            		for(int k = 0; k < 5; k++) s.append(convert(board[i+k][j-k]));
            		score += evaluatePlayer1(s.toString()) - evaluatePlayer2(s.toString());
            	}
            }
		}
        return score;
    }
	
	private List<int[]> generateMoves(){
		List<int[]> possibleMoves = new ArrayList<>();
		for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
            	if(board[i][j] != 0) {
            		for(int m = i - expandRadius; m <= i + expandRadius; m++) {
            			for(int n = j - expandRadius; n <= j + expandRadius; n++) {
            				if(m >= 0 && m < 15 && n >= 0 && n < 15 && board[m][n] == 0)
            					possibleMoves.add(new int[] {m,n});
            			}
            		}
            	}
            	
            }
		}
		
		return possibleMoves;
	}
	
	public List<Node> generateChildren() {
		if(isTerminalNode() == 0) {
			for(int[] m : generateMoves()) {
				addChildren(m);
			}
		}
		
		return childrens;
	}
	
	private void addChildren(int[] move) {
		Node children = new Node(this, move);
		childrens.add(children);
	}
}
