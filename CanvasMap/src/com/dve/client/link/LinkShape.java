package com.dve.client.link;


import gwt.awt.Polygon;

import java.util.Iterator;
import java.util.logging.Logger;

import com.dve.client.canvas.screen.CanvasScreen;
import com.dve.client.selector.SCL;
import com.dve.shared.dto.canvas.DTOLink;
import com.dve.shared.dto.canvas.DTOLinks;


public class LinkShape {

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
			log.info("dtoLinks==null");
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

		SCL.getCanvasDialog().updateCurrLinkNodes();

		draw(false);

	}

	public void nodeMove(int x, int y) {
		log.info("nodeMve");
		if(dtoLink!=null) {
			if(Math.pow(x-getCoord(dtoLink.getX()),2) + Math.pow(y-getCoord(dtoLink.getY()),2) > Math.pow(nodeRadius,2)) {
				moving = true;
				clear();
				dtoLink.setX(getRevCoord(x));
				dtoLink.setY(getRevCoord(y));
				draw(false);

			}

		}

		SCL.getCanvasDialog().updateCurrLinkNodes();

	}

	public void nodeUp(int x, int y) {
		log.info("nodeUp!");
		if(dtoLink==null) {
			log.severe("nodeDn == null");
		}
		if(moving) {
			//			log.severe("moving!");
		}
		if(dtoLink!=null && !moving) {
			//			log.info("x,y = " + x + ", " + y);
			//			log.info("x = " + x);
			//			log.info("dtoLink.x = " + getCoord(dtoLink.getX()));
			//			log.info("y = " + getRevCoord(y));
			//			log.info("dtoLink.y = " + getCoord(dtoLink.getY()));
			//			log.info("nodeUp 2! value = " + (Math.pow(x-getCoord(dtoLink.getX()),2) + Math.pow(y-getCoord(dtoLink.getY()),2)) + ", nodeRadius = " + nodeRadius + " nodeRadius^2 = " + (Math.pow(nodeRadius,2)));
			if(Math.pow(x-getCoord(dtoLink.getX()),2) + Math.pow(y-getCoord(dtoLink.getY()),2) <= Math.pow(nodeRadius,2)) {
				//				log.info("nodeUp 2-a!");
				clear();
				dtoLinks.getDTOLinks().remove(dtoLink);
				draw(false);			
			}
		}

		SCL.getCanvasDialog().updateCurrLinkNodes();

	}

	public void clear() {
		if(dtoLinks!=null && dtoLinks.getDTOLinks().size()>0) {
			CanvasScreen canvasScreen = SCL.getCurrPrimeCanvas().getCanvasScreen();

			String prevGlobalCompositeOperation = canvasScreen.context1.getGlobalCompositeOperation();
			canvasScreen.context1.setGlobalCompositeOperation("destination-out");

			Iterator<DTOLink> it = dtoLinks.getDTOLinks().iterator();
			while(it.hasNext()) {
				DTOLink p = it.next();
				canvasScreen.context1.setFillStyle("rgba(255,255,255,1.0)");
				canvasScreen.context1.beginPath();
				canvasScreen.context1.arc(getCoord(p.getX()), getCoord(p.getY()), nodeRadius+1, 0, Math.PI * 2.0, true);
				canvasScreen.context1.closePath();
				canvasScreen.context1.fill();

			}

			canvasScreen.context1.setLineWidth(5);
			canvasScreen.context1.setStrokeStyle("rgba(255,255,255,1.0)");
			canvasScreen.context1.beginPath();
			it = dtoLinks.getDTOLinks().iterator();
			DTOLink p = it.next();
			canvasScreen.context1.moveTo(getCoord(p.getX()),getCoord(p.getY()));
			while(it.hasNext()) {
				p = it.next();

				canvasScreen.context1.lineTo(getCoord(p.getX()), getCoord(p.getY()));		

			}
			canvasScreen.context1.closePath();
			canvasScreen.context1.setFillStyle("rgba(255,255,255,1.0)");
			canvasScreen.context1.setGlobalAlpha(1);
			canvasScreen.context1.fill();
			canvasScreen.context1.stroke();

			canvasScreen.context1.setGlobalCompositeOperation(prevGlobalCompositeOperation);
		}
	}

	public void draw(boolean highlight) {
		if(dtoLinks!=null && dtoLinks.getDTOLinks().size()>0) {
			CanvasScreen canvasScreen = SCL.getCurrPrimeCanvas().getCanvasScreen();
			Iterator<DTOLink> it;
			if(SCL.getCanvasDialog().isEdit()) {
				it = dtoLinks.getDTOLinks().iterator();
				while(it.hasNext()) {
					DTOLink p = it.next();
					canvasScreen.context1.setFillStyle(SCL.getCurrPrimeCanvas().getEditLineColor());
					canvasScreen.context1.beginPath();
					canvasScreen.context1.arc(getCoord(p.getX()), getCoord(p.getY()), nodeRadius, 0, Math.PI * 2.0, true);
					canvasScreen.context1.closePath();
					canvasScreen.context1.fill();

				}
			}

			//		log.severe("Resetting Polygon!");
			polygon = new Polygon();

			if(SCL.getCanvasDialog().isEdit()) {
				canvasScreen.context1.setLineWidth(2);
				canvasScreen.context1.setStrokeStyle(SCL.getCurrPrimeCanvas().getEditLineColor());
				
			} else {
				canvasScreen.context1.setLineWidth(1);
				canvasScreen.context1.setStrokeStyle(SCL.getCurrPrimeCanvas().getUserLineColor());
				
			}
			canvasScreen.context1.beginPath();
			it = dtoLinks.getDTOLinks().iterator();
			DTOLink p = it.next();
			canvasScreen.context1.moveTo(getCoord(p.getX()),getCoord(p.getY()));
			polygon.addPoint(getCoord(p.getX()), getCoord(p.getY()));
			while(it.hasNext()) {
				p = it.next();

				canvasScreen.context1.lineTo(getCoord(p.getX()), getCoord(p.getY()));		

				polygon.addPoint(getCoord(p.getX()), getCoord(p.getY()));

			}
			canvasScreen.context1.closePath();

			if(highlight) {
				canvasScreen.context1.setGlobalAlpha(0.3);
				canvasScreen.context1.setFillStyle(SCL.getCurrPrimeCanvas().getUserShadeColor());
				canvasScreen.context1.fill();
			}

			canvasScreen.context1.setGlobalAlpha(1);
			canvasScreen.context1.stroke();
		}
	}

	private int getCoord(int coord) {
		double x = (double)coord;
		//		log.info("getCoord() zoom = " + SCL.getCurrPrimeCanvas().getCanvasScreen().zoom);
		return (int)(x*SCL.getCurrPrimeCanvas().getCanvasScreen().zoom);

	}

	private int getRevCoord(int coord) {
		double x = (double)coord;
		//		log.info("getRevCoord() zoom = " + canvasScreen.zoom);
		return (int)(x/SCL.getCurrPrimeCanvas().getCanvasScreen().zoom);
	}

	public boolean contains(double x, double y) {
		//		log.info("polygon.height = " + polygon.getBounds().height);
		//		log.info("polygon.width = " + polygon.getBounds().width);

		if(polygon.contains(x,y)) {
			return true;
		}
		return false;

	}

	public void highlight() {
		clear();
		draw(true);

	}

	public void unhighlight() {
		clear();
		draw(false);

	}

	public void setDtoLinkNodes(DTOLinks dtoLinks) {
		this.dtoLinks = dtoLinks;

	}


}
