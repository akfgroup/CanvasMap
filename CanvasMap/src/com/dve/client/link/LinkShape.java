package com.dve.client.link;

import gwt.awt.Point;
import gwt.awt.Polygon;

import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import com.dve.client.canvas.CanvasPanel;


public class LinkShape {

	CanvasPanel canvasPanel;
	
	String color = "black";
	int weight = 5;

	int nodeRadius = 5;

	int startX;
	int startY;

	Vector<Point> nodes = new Vector<Point>();

	Point nodeDn;

	boolean moving;
	double zoom;

	Logger log = Logger.getLogger(LinkShape.class.getName());

	public LinkShape(CanvasPanel canvasPanel, int x, int y) {
		this.canvasPanel = canvasPanel;
		this.startX = x;
		this.startY = y;
		this.zoom = canvasPanel.zoom;

		Point p1 = new Point(canvasPanel.roundIt(x),canvasPanel.roundIt(y));
		Point p2 = new Point(p1.x+(int)(canvasPanel.spacing/(1/zoom)), p1.y);
		Point p3 = new Point(p1.x+(int)(canvasPanel.spacing/(1/zoom)), p1.y+(int)(canvasPanel.spacing/(1/zoom)));
		Point p4 = new Point(p1.x, p1.y+(int)(canvasPanel.spacing/(1/zoom)));

		nodes.add(p1);
		nodes.add(p2);
		nodes.add(p3);
		nodes.add(p4);

		draw();
	}

	public void nodeDown(int x, int y) {
		log.info("nodeDn");
		nodeDn = null;
		moving = false;
		
		Iterator<Point> it = nodes.iterator();
		while(it.hasNext()) {
			Point p = it.next();
			if((Math.abs(x-getCoord(p.x))^2) + (Math.abs(y-getCoord(p.y))^2) < (nodeRadius^2)) {
				nodeDn = p;
				return;
			}
		}

		clear();
		Point p = new Point(canvasPanel.roundIt((int)((double)x*zoom/canvasPanel.zoom)),canvasPanel.roundIt((int)((double)y*zoom/canvasPanel.zoom)));
		nodes.add(p);
		draw();

	}

	public void nodeMove(int x, int y) {
		log.info("nodeMve");
		if(nodeDn!=null) {
			if((Math.abs(x-getCoord(nodeDn.x))^2) + (Math.abs(y-getCoord(nodeDn.y))^2) > (nodeRadius^2)) {
				moving = true;
				clear();
				nodeDn.x=(int)((double)x*zoom/canvasPanel.zoom);
				nodeDn.y=(int)((double)y*zoom/canvasPanel.zoom);
				canvasPanel.draw();
				draw();		
			}

		}

	}

	public void nodeUp(int x, int y) {
		log.info("nodeUp!");
		if(nodeDn==null) {
			log.severe("nodeDn == null");
		}
		if(moving) {
			log.severe("moving!");
		}
		if(nodeDn!=null && !moving) {
			log.info("nodeUp 2!");
			if((Math.abs(x-getCoord(nodeDn.x))^2) + (Math.abs(y-getCoord(nodeDn.y))^2) < (nodeRadius^2)) {		
				clear();
				nodes.remove(nodeDn);
				draw();			
			}
		}

	}

	private void clear() {

		Point prevP = null;
		Point currP = null;

		String prevGlobalCompositeOperation = canvasPanel.context1.getGlobalCompositeOperation();
		canvasPanel.context1.setGlobalCompositeOperation("destination-out");
		
		Iterator<Point> it = nodes.iterator();
		while(it.hasNext()) {
			Point p = it.next();
			prevP = currP;

			
			canvasPanel.context1.setFillStyle("rgba(255,255,255,1.0)");
			canvasPanel.context1.beginPath();
			canvasPanel.context1.arc(getCoord(p.x), getCoord(p.y), nodeRadius+1, 0, Math.PI * 2.0, true);
			canvasPanel.context1.closePath();
			canvasPanel.context1.fill();

			if(prevP!=null) {
				canvasPanel.context1.setLineWidth(5);
				canvasPanel.context1.setStrokeStyle("rgba(255,255,255,1.0)");
				canvasPanel.context1.beginPath();
				canvasPanel.context1.moveTo(getCoord(prevP.x),getCoord(prevP.y));
				canvasPanel.context1.lineTo(getCoord(p.x), getCoord(p.y));
				canvasPanel.context1.stroke();
			}

			currP = p;

		}

		if(nodes.size()>0) {
			Point firstP = nodes.firstElement();
			canvasPanel.context1.setLineWidth(5);
			canvasPanel.context1.setStrokeStyle("rgba(255,255,255,1.0)");
			canvasPanel.context1.beginPath();
			canvasPanel.context1.moveTo(getCoord(currP.x),getCoord(currP.y));
			canvasPanel.context1.lineTo(getCoord(firstP.x), getCoord(firstP.y));
			canvasPanel.context1.stroke();
		}

		canvasPanel.context1.setGlobalCompositeOperation(prevGlobalCompositeOperation);
	}

	public void draw() {

		Point prevP = null;
		Point currP = null;

		Iterator<Point> it = nodes.iterator();
		while(it.hasNext()) {
			Point p = it.next();
			prevP = currP;

			canvasPanel.context1.setFillStyle(color);
			canvasPanel.context1.beginPath();
			canvasPanel.context1.arc(getCoord(p.x), getCoord(p.y), nodeRadius, 0, Math.PI * 2.0, true);
			canvasPanel.context1.closePath();
			canvasPanel.context1.fill();

			if(prevP!=null) {
				canvasPanel.context1.setLineWidth(2);
				canvasPanel.context1.setStrokeStyle(color);
				canvasPanel.context1.beginPath();
				canvasPanel.context1.moveTo(getCoord(prevP.x),getCoord(prevP.y));
				canvasPanel.context1.lineTo(getCoord(p.x), getCoord(p.y));
				canvasPanel.context1.stroke();
			}

			currP = p;

		}

		if(nodes.size()>0) {
			Point firstP = nodes.firstElement();
			canvasPanel.context1.setLineWidth(2);
			canvasPanel.context1.setStrokeStyle(color);
			canvasPanel.context1.beginPath();
			canvasPanel.context1.moveTo(getCoord(currP.x),getCoord(currP.y));
			canvasPanel.context1.lineTo(getCoord(firstP.x), getCoord(firstP.y));
			canvasPanel.context1.stroke();
		}

	}
	
	private int getCoord(int coord) {
		double x = (double)coord;
		return (int)(x/zoom*canvasPanel.zoom);
		
	}

	public boolean contains(double x, double y) {
		Polygon polygon = new Polygon();

		Iterator<Point> it = nodes.iterator();
		while(it.hasNext()) {
			Point p = it.next();
			polygon.addPoint((int)((double)p.x/zoom*canvasPanel.zoom), (int)((double)p.y/zoom*canvasPanel.zoom));
		}

		return polygon.contains(x, y);

	}
	
	public void highlight() {
		clear();
		color = "orange";
		draw();
		
	}
	
	public void unhighlight() {
		clear();
		color = "black";
		draw();
		
	}



}
