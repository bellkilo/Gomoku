package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

//point where is the mouse and where can i put the chess
public class Pointer {
	private int row = 0; //which row are the pointer
	private int column = 0; // which column are the pointer
	private int x = 0;//
	private int y = 0;//
	private int h = 36; // Height of pointer
	
	private boolean hasPiece = false;
	private boolean isShow = false;
	//constructor
	public Pointer(int row, int column, int start, int distance) {
		this.row = row;
		this.column = column;
		x = start + column*distance;
		y = start + row*distance;
	}
	
	public boolean hasPiece() {
		return hasPiece;
	}



	public void setHasPiece(boolean hasPiece) {
		this.hasPiece = hasPiece;
	}



	public boolean isShow() {
		return isShow;
	}

	public void init() {
		hasPiece = false;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}



	//draw the pointer
	public void draw(Graphics g) {
		g.setColor(new Color(255,0,0));
		//g.drawRect(x-h/2, y-h/2, h, h); //drawRect draw the rect from the top left
		//redraw the pointer
		drawPointer(g);
	}
	
	private void drawPointer(Graphics g) {
		//transform g to a 2d painting brush 
		Graphics2D g2d = (Graphics2D)g;
		//set the thickness of the brush
		g2d.setStroke(new BasicStroke(2.0f));
		
		int x1 = 0;
		int y1 = 0;
		int x2 = 0;
		int y2 = 0;
		
		//draw the top left of pointer
		x1 = x - h/2;
		y1 = y - h/2;
		//line horizontal
		x2 = x - h/4;
		y2 = y1;
		g2d.drawLine(x1, y1, x2, y2);
		//line vertical
		x2 = x1;
		y2 = y - h/4;
		g2d.drawLine(x1, y1, x2, y2);
		
		//draw the top right of pointer
		x1 = x + h/2;
		y1 = y - h/2;
		//line horizontal
		x2 = x + h/4;
		y2 = y1;
		g2d.drawLine(x1, y1, x2, y2);
		//line vertical
		x2 = x1;
		y2 = y - h/4;
		g2d.drawLine(x1, y1, x2, y2);
		
		//draw the bottom left of pointer
		x1 = x - h/2;
		y1 = y + h/2;
		//line horizontal
		x2 = x - h/4;
		y2 = y1;
		g2d.drawLine(x1, y1, x2, y2);
		//line vertical
		x2 = x1;
		y2 = y + h/4;
		g2d.drawLine(x1, y1, x2, y2);
		
		//draw the bottom right of pointer
		x1 = x + h/2;
		y1 = y + h/2;
		//line horizontal
		x2 = x + h/4;
		y2 = y1;
		g2d.drawLine(x1, y1, x2, y2);
		//line vertical
		x2 = x1;
		y2 = y + h/4;
		g2d.drawLine(x1, y1, x2, y2);
	}


	//look at where is the mouse and if this pointer should we show
	public boolean isPointed(int x, int y) {
		//top left of this pointer
		int x1 = this.x - h/2; 
		int y1 = this.y - h/2; 
		//bottom right of this pointer
		int x2 = this.x + h/2; 
		int y2 = this.y + h/2; 
		
		return x>x1 && y>y1 && x<x2 && y<y2;
	}
}
