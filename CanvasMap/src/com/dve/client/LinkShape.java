package com.dve.client;

import gwt.awt.Point;
import gwt.awt.Polygon;

import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;


public class LinkShape {

	CanvasMap canvasMap;
	
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

	public LinkShape(CanvasMap canvasMap, int x, int y) {
		this.canvasMap = canvasMap;
		this.startX = x;
		this.startY = y;
		this.zoom = canvasMap.zoom;

		Point p1 = new Point(canvasMap.roundIt(x),canvasMap.roundIt(y));
		Point p2 = new Point(p1.x+(int)(canvasMap.spacing/(1/zoom)), p1.y);
		Point p3 = new Point(p1.x+(int)(canvasMap.spacing/(1/zoom)), p1.y+(int)(canvasMap.spacing/(1/zoom)));
		Point p4 = new Point(p1.x, p1.y+(int)(canvasMap.spacing/(1/zoom)));

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
		Point p = new Point(canvasMap.roundIt((int)((double)x*zoom/canvasMap.zoom)),canvasMap.roundIt((int)((double)y*zoom/canvasMap.zoom)));
		nodes.add(p);
		draw();

	}

	public void nodeMove(int x, int y) {
		log.info("nodeMve");
		if(nodeDn!=null) {
			if((Math.abs(x-getCoord(nodeDn.x))^2) + (Math.abs(y-getCoord(nodeDn.y))^2) > (nodeRadius^2)) {
				moving = true;
				clear();
				nodeDn.x=(int)((double)x*zoom/canvasMap.zoom);
				nodeDn.y=(int)((double)y*zoom/canvasMap.zoom);
				canvasMap.draw();
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

		String prevGlobalCompositeOperation = canvasMap.context1.getGlobalCompositeOperation();
		canvasMap.context1.setGlobalCompositeOperation("destination-out");
		
		Iterator<Point> it = nodes.iterator();
		while(it.hasNext()) {
			Point p = it.next();
			prevP = currP;

			
			canvasMap.context1.setFillStyle("rgba(255,255,255,1.0)");
			canvasMap.context1.beginPath();
			canvasMap.context1.arc(getCoord(p.x), getCoord(p.y), nodeRadius+1, 0, Math.PI * 2.0, true);
			canvasMap.context1.closePath();
			canvasMap.context1.fill();

			if(prevP!=null) {
				canvasMap.context1.setLineWidth(5);
				canvasMap.context1.setStrokeStyle("rgba(255,255,255,1.0)");
				canvasMap.context1.beginPath();
				canvasMap.context1.moveTo(getCoord(prevP.x),getCoord(prevP.y));
				canvasMap.context1.lineTo(getCoord(p.x), getCoord(p.y));
				canvasMap.context1.stroke();
			}

			currP = p;

		}

		if(nodes.size()>0) {
			Point firstP = nodes.firstElement();
			canvasMap.context1.setLineWidth(5);
			canvasMap.context1.setStrokeStyle("rgba(255,255,255,1.0)");
			canvasMap.context1.beginPath();
			canvasMap.context1.moveTo(getCoord(currP.x),getCoord(currP.y));
			canvasMap.context1.lineTo(getCoord(firstP.x), getCoord(firstP.y));
			canvasMap.context1.stroke();
		}

		canvasMap.context1.setGlobalCompositeOperation(prevGlobalCompositeOperation);
	}

	public void draw() {

		Point prevP = null;
		Point currP = null;

		Iterator<Point> it = nodes.iterator();
		while(it.hasNext()) {
			Point p = it.next();
			prevP = currP;

			canvasMap.context1.setFillStyle(color);
			canvasMap.context1.beginPath();
			canvasMap.context1.arc(getCoord(p.x), getCoord(p.y), nodeRadius, 0, Math.PI * 2.0, true);
			canvasMap.context1.closePath();
			canvasMap.context1.fill();

			if(prevP!=null) {
				canvasMap.context1.setLineWidth(2);
				canvasMap.context1.setStrokeStyle(color);
				canvasMap.context1.beginPath();
				canvasMap.context1.moveTo(getCoord(prevP.x),getCoord(prevP.y));
				canvasMap.context1.lineTo(getCoord(p.x), getCoord(p.y));
				canvasMap.context1.stroke();
			}

			currP = p;

		}

		if(nodes.size()>0) {
			Point firstP = nodes.firstElement();
			canvasMap.context1.setLineWidth(2);
			canvasMap.context1.setStrokeStyle(color);
			canvasMap.context1.beginPath();
			canvasMap.context1.moveTo(getCoord(currP.x),getCoord(currP.y));
			canvasMap.context1.lineTo(getCoord(firstP.x), getCoord(firstP.y));
			canvasMap.context1.stroke();
		}

	}
	
	private int getCoord(int coord) {
		double x = (double)coord;
		return (int)(x/zoom*canvasMap.zoom);
		
	}

	public boolean contains(double x, double y) {
		Polygon polygon = new Polygon();

		Iterator<Point> it = nodes.iterator();
		while(it.hasNext()) {
			Point p = it.next();
			polygon.addPoint((int)((double)p.x/zoom*canvasMap.zoom), (int)((double)p.y/zoom*canvasMap.zoom));
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
