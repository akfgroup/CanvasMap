package com.dve.client.link;

import gwt.awt.Polygon;

import java.util.Iterator;
import java.util.logging.Logger;

import com.dve.client.canvas.screen.CanvasScreen;
import com.dve.client.selector.SCL;
import com.dve.shared.dto.canvas.DTOLink;
import com.dve.shared.dto.canvas.DTOLinks;


public class LinkShape {
	
	String color = "black";
	int weight = 5;

	int nodeRadius = 5;

	DTOLinks dtoLinks;

	DTOLink dtoLink;
	
	Polygon polygon = new Polygon();

	boolean moving;

	Logger log = Logger.getLogger(LinkShape.class.getName());

	public LinkShape() {

	}

	public void nodeDown(int x, int y) {
		log.info("nodeDn x=" + x + ", y=" + y);
		if(dtoLinks==null) {
			dtoLinks = new DTOLinks();
			SCL.getCurrSecCanvas().getDtoCanvas().setDtoLinks(dtoLinks);
		}
		dtoLink = null;
		moving = false;
		
		Iterator<DTOLink> it = dtoLinks.getDTOLinks().iterator();
		while(it.hasNext()) {
			DTOLink p = it.next();
			if((Math.abs(x-getCoord(p.getX()))^2) + (Math.abs(y-getCoord(p.getY()))^2) < (nodeRadius^2)) {
				dtoLink = p;
				return;
			}
		}

		clear();
		
		DTOLink p = new DTOLink();
		p.setCanvasMapId(SCL.getCurrSecCanvas().getDtoCanvas().getId());
		p.setX(getRevCoord(x));
		p.setY(getRevCoord(y));
		
		dtoLinks.getDTOLinks().add(p);
		draw();

	}

	public void nodeMove(int x, int y) {
		log.info("nodeMve");
		if(dtoLink!=null) {
			if(Math.pow(x-getCoord(dtoLink.getX()),2) + Math.pow(y-getCoord(dtoLink.getY()),2) > Math.pow(nodeRadius,2)) {
				moving = true;
				clear();
				dtoLink.setX(getRevCoord(x));
				dtoLink.setY(getRevCoord(y));
				draw();
				
			}

		}

	}

	public void nodeUp(int x, int y) {
		log.info("nodeUp!");
		if(dtoLink==null) {
			log.severe("nodeDn == null");
		}
		if(moving) {
			log.severe("moving!");
		}
		if(dtoLink!=null && !moving) {
			log.info("x,y = " + x + ", " + y);
			log.info("x = " + x);
			log.info("dtoLink.x = " + getCoord(dtoLink.getX()));
			log.info("y = " + getRevCoord(y));
			log.info("dtoLink.y = " + getCoord(dtoLink.getY()));
			log.info("nodeUp 2! value = " + (Math.pow(x-getCoord(dtoLink.getX()),2) + Math.pow(y-getCoord(dtoLink.getY()),2)) + ", nodeRadius = " + nodeRadius + " nodeRadius^2 = " + (Math.pow(nodeRadius,2)));
			if(Math.pow(x-getCoord(dtoLink.getX()),2) + Math.pow(y-getCoord(dtoLink.getY()),2) <= Math.pow(nodeRadius,2)) {
				log.info("nodeUp 2-a!");
				clear();
				dtoLinks.getDTOLinks().remove(dtoLink);
				draw();			
			}
		}
		
		SCL.getCanvasDialog().updateCurrLinkNodes();

	}

	private void clear() {
		
		CanvasScreen canvasScreen = SCL.getCurrPrimeCanvas().getCanvasScreen();

		DTOLink prevP = null;
		DTOLink currP = null;

		String prevGlobalCompositeOperation = canvasScreen.context1.getGlobalCompositeOperation();
		canvasScreen.context1.setGlobalCompositeOperation("destination-out");
		
		Iterator<DTOLink> it = dtoLinks.getDTOLinks().iterator();
		while(it.hasNext()) {
			DTOLink p = it.next();
			prevP = currP;

			canvasScreen.context1.setFillStyle("rgba(255,255,255,1.0)");
			canvasScreen.context1.beginPath();
			canvasScreen.context1.arc(getCoord(p.getX()), getCoord(p.getY()), nodeRadius+1, 0, Math.PI * 2.0, true);
			canvasScreen.context1.closePath();
			canvasScreen.context1.fill();

			if(prevP!=null) {
				canvasScreen.context1.setLineWidth(5);
				canvasScreen.context1.setStrokeStyle("rgba(255,255,255,1.0)");
				canvasScreen.context1.beginPath();
				canvasScreen.context1.moveTo(getCoord(prevP.getX()),getCoord(prevP.getY()));
				canvasScreen.context1.lineTo(getCoord(p.getX()), getCoord(p.getY()));
				canvasScreen.context1.stroke();
			}

			currP = p;

		}

		if(dtoLinks.getDTOLinks().size()>0) {
			DTOLink firstP = dtoLinks.getDTOLinks().firstElement();
			canvasScreen.context1.setLineWidth(5);
			canvasScreen.context1.setStrokeStyle("rgba(255,255,255,1.0)");
			canvasScreen.context1.beginPath();
			canvasScreen.context1.moveTo(getCoord(currP.getX()),getCoord(currP.getY()));
			canvasScreen.context1.lineTo(getCoord(firstP.getX()), getCoord(firstP.getY()));
			canvasScreen.context1.stroke();
		}

		canvasScreen.context1.setGlobalCompositeOperation(prevGlobalCompositeOperation);
	}

	public void draw() {
		
		CanvasScreen canvasScreen = SCL.getCurrPrimeCanvas().getCanvasScreen();

		DTOLink prevP = null;
		DTOLink currP = null;
		polygon.reset();

		Iterator<DTOLink> it = dtoLinks.getDTOLinks().iterator();
		while(it.hasNext()) {
			log.info("draw link node");
			DTOLink p = it.next();
			prevP = currP;

			canvasScreen.context1.setFillStyle(color);
			canvasScreen.context1.beginPath();
			canvasScreen.context1.arc(getCoord(p.getX()), getCoord(p.getY()), nodeRadius, 0, Math.PI * 2.0, true);
			canvasScreen.context1.closePath();
			canvasScreen.context1.fill();

			if(prevP!=null) {
				canvasScreen.context1.setLineWidth(2);
				canvasScreen.context1.setStrokeStyle(color);
				canvasScreen.context1.beginPath();
				canvasScreen.context1.moveTo(getCoord(prevP.getX()),getCoord(prevP.getY()));
				canvasScreen.context1.lineTo(getCoord(p.getX()), getCoord(p.getY()));
				canvasScreen.context1.stroke();
			}

			polygon.addPoint(getCoord(p.getX()), getCoord(p.getY()));
			currP = p;

		}

		if(dtoLinks.getDTOLinks().size()>0) {
			DTOLink firstP = dtoLinks.getDTOLinks().firstElement();
			canvasScreen.context1.setLineWidth(2);
			canvasScreen.context1.setStrokeStyle(color);
			canvasScreen.context1.beginPath();
			canvasScreen.context1.moveTo(getCoord(currP.getX()),getCoord(currP.getY()));
			canvasScreen.context1.lineTo(getCoord(firstP.getX()), getCoord(firstP.getY()));
			canvasScreen.context1.stroke();
		}

	}
	
	private int getCoord(int coord) {
		double x = (double)coord;
//		log.info("getCoord() zoom = " + canvasScreen.zoom);
		return (int)(x*SCL.getCurrPrimeCanvas().getCanvasScreen().zoom);
		
	}
	
	private int getRevCoord(int coord) {
		double x = (double)coord;
//		log.info("getRevCoord() zoom = " + canvasScreen.zoom);
		return (int)(x/SCL.getCurrPrimeCanvas().getCanvasScreen().zoom);
	}
	
	public boolean contains(double x, double y) {
		if(polygon.contains(x,y)) {
			return true;
		}
		return false;

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

	public void setDtoLinkNodes(DTOLinks dtoLinks) {
		this.dtoLinks = dtoLinks;
		
	}


}
