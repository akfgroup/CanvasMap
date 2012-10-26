package com.dve.client.link;

import gwt.awt.Polygon;

import java.util.Iterator;
import java.util.logging.Logger;

import com.dve.client.canvas.CanvasScreen;
import com.dve.client.selector.SCL;
import com.dve.shared.dto.canvas.DTOLink;
import com.dve.shared.dto.canvas.DTOLinks;


public class LinkShape {

	CanvasScreen canvasScreen;
	
	String color = "black";
	int weight = 5;

	int nodeRadius = 5;

	DTOLinks dtoLinks;

	DTOLink dtoLink;

	boolean moving;

	Logger log = Logger.getLogger(LinkShape.class.getName());

	public LinkShape(CanvasScreen canvasScreen) {
		this.canvasScreen = canvasScreen;

	}

	public void nodeDown(int x, int y) {
		log.info("nodeDn");
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
		p.setX(x);
		p.setY(y);
		p.setZoom(SCL.getCanvasScreen().zoom);
		
		dtoLinks.getDTOLinks().add(p);
		draw();

	}

	public void nodeMove(int x, int y) {
		log.info("nodeMve");
		if(dtoLink!=null) {
			if((Math.abs(x-getCoord(dtoLink.getX()))^2) + (Math.abs(y-getCoord(dtoLink.getY()))^2) > (nodeRadius^2)) {
				moving = true;
				clear();
				dtoLink.setX(x);
				dtoLink.setY(y);
				canvasScreen.draw();
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
			log.info("nodeUp 2!");
			if((Math.abs(x-getCoord(dtoLink.getX()))^2) + (Math.abs(y-getCoord(dtoLink.getY()))^2) < (nodeRadius^2)) {		
				clear();
				dtoLinks.getDTOLinks().remove(dtoLink);
				draw();			
			}
		}
		
		SCL.getCanvasDialog().updateCurrLinkNodes();

	}

	private void clear() {

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

		DTOLink prevP = null;
		DTOLink currP = null;

		Iterator<DTOLink> it = dtoLinks.getDTOLinks().iterator();
		while(it.hasNext()) {
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
		
		if(!moving && SCL.getCurrSecCanvas()!=null) {
			SCL.getCanvasDialog().updateCurrLinkNodes();
		}

	}
	
	private int getCoord(int coord) {
		double x = (double)coord;
		return (int)(x*canvasScreen.zoom);
		
	}

	public boolean contains(double x, double y) {
		Polygon polygon = new Polygon();

		Iterator<DTOLink> it = dtoLinks.getDTOLinks().iterator();
		while(it.hasNext()) {
			DTOLink p = it.next();
			polygon.addPoint((int)((double)p.getX()/canvasScreen.zoom), (int)((double)p.getY()/canvasScreen.zoom));
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

	public void setDtoLinkNodes(DTOLinks dtoLinks) {
		this.dtoLinks = dtoLinks;
		
	}


}
