package main;

import java.util.List;
import java.util.Vector;

public class GameTree {
	private int maxDepth;
	private Node root;
	private Node nodeSearching = null;
	
	private List<Node> nodeWaiting = new Vector<Node>();
	private List<Node> nodeSearched = new Vector<Node>();
	
	public GameTree(int[][] board, int player, int maxDepth) {
		System.out.println("game tree creating");

		if(board[7][7] == 0) {
			root = null;
		}else {
			this.maxDepth = maxDepth;
			root = new Node(board, player, maxDepth);
		}
		
		System.out.println("game tree created");
		
	}
	
	public int[] AIgame() {
		if(root == null) return new int[] {8,8};
		int result = root.isTerminalNode();
		if (result == 1) return null;
		if(result == 2) return null;
		
		nodeWaiting.add(root);
		while(!nodeWaiting.isEmpty()) {
			nodeSearching = nodeWaiting.remove(0);
			nodeSearched.add(nodeSearching);
			if (isAlphaBetaCut(nodeSearching.getFather())) continue;
			if (nodeSearching.getDepth() < maxDepth) {
				addWaitingNodes(nodeSearching);
				if(!nodeSearching.getChildrens().isEmpty()) continue;
			}
			
			System.out.println("Searching Value "+ nodeSearching.getValue());
			nodeSearching.evaluate();
			updateValue(nodeSearching);
		}
		
		Node nextMove = root.getChildrens().get(0);
		for(Node n : root.getChildrens()) {
			if(n.getValue() > nextMove.getValue())
				nextMove = n;
		}
		
		return nextMove.getMove();
	}
	
	private void addWaitingNodes(Node node) {
		nodeWaiting.addAll(0, node.generateChildren());
		
	}
	
	private static boolean isAlphaBetaCut(Node node) {
		if(node == null || node.getFather() == null) return false;
		if(node.isMaxNode() && node.getValue() > node.getFather().getValue()) return true;
		if(!node.isMaxNode() && node.getValue() < node.getFather().getValue()) return true;
		return isAlphaBetaCut(node.getFather());
	}
	
	private static void updateValue(Node node) {
		if(node == null) return;
		if(node.isLeaf()) {
			updateValue(node.getFather());
			return;
		}
		if(node.isMaxNode()) {
			int currentValue = Integer.MIN_VALUE;
			for(Node n : node.getChildrens())
				if(n.getValue() != Integer.MAX_VALUE) currentValue = Math.max(currentValue, n.getValue());
				
			if(currentValue > node.getValue()) {
				node.setValue(currentValue);
				updateValue(node.getFather());
			}
		}else {
			int currentValue = Integer.MAX_VALUE;
			for(Node n : node.getChildrens())
				if(n.getValue() != Integer.MIN_VALUE) currentValue = Math.min(currentValue, n.getValue());
				
			if(currentValue < node.getValue()) {
				node.setValue(currentValue);
				updateValue(node.getFather());
			}
		}
	}
}
