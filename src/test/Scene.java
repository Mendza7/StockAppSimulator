package test;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.sql.Array;

import javax.swing.text.AttributeSet.ColorAttribute;

public class Scene extends Canvas implements Runnable{
	MainScreen owner;
	Thread t;
	Graphics g = this.getGraphics();
	Stock s = User.currentStock;
	int candleLimit = 50;
	
	int offsetX = 0;
	int offsetY = 0;
	
	double scaleX = 1;
	double scaleY = 1;
	
	int tempPosX;
	int tempPosY;
	private int widthRect;
	private int offset;
	
	boolean started = false;
	
	String text="";
	
	
	
	public void reset() {
		s = User.currentStock;
		offsetX = 0;
		offsetY = 0;
		scaleX = 1;
		scaleY = 1;
		offset = 5;
		text = "";
	}
	
	
	
	private class MouseEventHandler extends MouseAdapter{

		@Override
		public void mousePressed(MouseEvent e) {
			tempPosX = e.getX();
			tempPosY = e.getY();
			
			if (e.getButton() == 3) {
				int i =StWx(e.getX());
				if ((int)(i/(widthRect+Math.ceil(8/scaleX)))> 0 && (int)(i/(widthRect+Math.ceil(8/scaleX)))<s.candles.size()) {
					text = s.name + s.candles.get((int)(i/(widthRect+Math.ceil(8/scaleX))));
				}
				else {
					text = s.name;
				}
				repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			offsetX += e.getX() - tempPosX;
			offsetY += e.getY() - tempPosY;
			
			repaint();
			//System.out.println(offsetX + " " + offsetY);
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			tempPosX = StWx(e.getX());
			tempPosY = StWy(e.getY());
			
			if (e.getWheelRotation() > 0) {
				scaleX*= .9;
				scaleY *= .9;
			}
			else {
				scaleX*= 1.1;
				scaleY *= 1.1;

			}
			
			offsetX +=(StWx(e.getX())-tempPosX);
			offsetY += StWy(e.getY()) - tempPosY;
			
			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			offsetX += (int)((float)(e.getX() - tempPosX)/scaleX);
			offsetY += (int)((float)(e.getY() - tempPosY)/scaleY);
			
			tempPosX = e.getX();
			tempPosY = e.getY();
			
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			
		}
		
		
	}
	
	public Scene(MainScreen owner) {
		super();
		this.owner = owner;
		setBackground(Color.WHITE);
		
		addMouseListener(new MouseEventHandler());
		addMouseMotionListener(new MouseEventHandler());
		addMouseWheelListener(new MouseEventHandler());
		
	}


	@Override
	public void paint(Graphics g) {
		finish();
		t = new Thread(this);
		t.start();
	}
	
	public synchronized void finish() {
		if (t!= null) {
			t.interrupt();
		}
		while(t!=null) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
	}
	
	private void paintGrid(Graphics g) {
		int dimW = owner.centerPanel.getWidth();
		int dimH = owner.centerPanel.getHeight();
		//System.out.println(dimW + " " + dimH);
		int stepW = dimW/60;
		int stepH = dimH/60;
		g.setColor(Color.GRAY);
		for(int i =0 , k =0;k<dimH && i <dimW ; k+=stepH, i+=stepW ) {
			//horizontalne k 
			g.drawLine(0,k,dimW-1,k);
			
			//veritkalne i
			g.drawLine(i,0, i, dimH-1);
		}
	}
	
	
	private int getRectHeight(Candle c) {
		int i = (int) ((Math.abs(c.open-c.close)/User.currentStock.ratio)*owner.centerPanel.getHeight());
		return i;
	}
	
	private int getLineHeight(Candle c) {
		int i = (int) ((Math.abs(c.high-c.low)/User.currentStock.ratio)*owner.centerPanel.getHeight());
		return i;
	}
	
	
	private int WtSx(int coord) {
		return (int)((coord+offsetX)*scaleX);
	}
	
	private int StWx(int coord) {
		return (int)((float)(coord)/scaleX-offsetX);
	}
	
	private int WtSy(int coord) {
		return (int)((coord+offsetY)*scaleY);
	}
	
	private int StWy(int coord) {
		return (int)((float)(coord)/scaleY-offsetY);
	}
	
	
	private int getVertOffsetLine(double d) {
		double max= User.currentStock.max;
		double range = User.currentStock.ratio;
		int height = owner.centerPanel.getHeight();
		
		return (int)(((max - d)/range)*height);
		
		
	}
	
	private int getVertOffset(Candle c) {
		double max= User.currentStock.max;
		double range = User.currentStock.ratio;
		int height = owner.centerPanel.getHeight();
		
		return (int)(((max - c.open)/range)*height);
	}
	
	private void paintCandles(Graphics g) {
		double min = User.currentStock.min;
		double max= User.currentStock.max;
		double range = User.currentStock.ratio;
		
		int width = owner.centerPanel.getWidth();
		int height = owner.centerPanel.getHeight();
		
		offset=5;
		widthRect = (int) ((width/(candleLimit))*scaleX);
		
		g.setColor(Color.BLUE);
		g.drawString(text,40,40);
		
		ArrayList<Candle> candles = User.currentStock.candles;
		int i = 0;
		
		for(Candle c : candles) {
			
			
			int rectHeight = getRectHeight(c);
			int vertOffset = getVertOffset(c);
			g.setColor(c.color);
			g.fillRect(WtSx(offset),WtSy(vertOffset),widthRect,rectHeight);
			
			int xLine =offset+widthRect/2;
			int y1Line = getVertOffsetLine(c.high);
			int y2Line = getVertOffsetLine(c.low);
			
			g.drawLine(WtSx(xLine),WtSy(y1Line),WtSx(xLine),WtSy(y2Line));			
			
			
			if (Stock.advanced_info && i >0) {
				xLine =offset+widthRect/2;
				y1Line = getVertOffsetLine(User.currentStock.MAs.get(i-1));
				y2Line = getVertOffsetLine(User.currentStock.MAs.get(i));
				g.setColor(Color.BLUE);
				g.drawLine(WtSx(xLine-(int)(widthRect+Math.ceil(8*scaleX))),WtSy(y1Line),WtSx(xLine),WtSy(y2Line));
				
				
				y1Line = getVertOffsetLine(User.currentStock.EMAs.get(i-1));
				y2Line = getVertOffsetLine(User.currentStock.EMAs.get(i));
				g.setColor(Color.PINK);
				g.drawLine(WtSx(xLine-(int)(widthRect+Math.ceil(8*scaleX))),WtSy(y1Line),WtSx(xLine),WtSy(y2Line));
			}
			i++;
			
			offset+=widthRect+Math.ceil(8*scaleX);
			
		}
		
		
		
		
		
		
		
	}
	
	
	private int getDim() {
		int width = owner.centerPanel.getWidth();
		int height = owner.centerPanel.getHeight();
		
		return Math.min(width, height);
		
	}


	
	@Override
	public void run() {
		try {
			Thread.sleep(50);
			g = this.getGraphics();
			super.paint(g);
			//paintGrid(g);
			paintCandles(g);
		} catch(InterruptedException e) {}
		
		
		synchronized(this) {
			t = null;
			notify();
		}
	}

}
