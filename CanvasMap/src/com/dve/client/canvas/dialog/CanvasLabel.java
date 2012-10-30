package com.dve.client.canvas.dialog;

import gwt.awt.Polygon;

import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import com.dve.client.link.LinkShape;
import com.dve.client.selector.SCL;
import com.dve.shared.dto.canvas.DTOCanvas;
import com.dve.shared.dto.canvas.DTOCanvases;
import com.dve.shared.dto.canvas.DTOLink;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;

public class CanvasLabel extends Composite {

	CanvasLabel canvasLabel;
	DTOCanvas dtoCanvas;

	DTOCanvases dtoCanvases;
	Vector<CanvasLabel> canvasLabels = new Vector();

	LinkShape linkShape;

	Label label = new Label();
	
	Logger log = Logger.getLogger(CanvasLabel.class.getName());

	public CanvasLabel(DTOCanvas dtoCanvas) {
		this.dtoCanvas = dtoCanvas;
		this.canvasLabel = this;

		if(dtoCanvas!=null) {
			if(dtoCanvas.getDtoLinks()!=null) {
				linkShape = new LinkShape(SCL.getCanvasScreen());
				linkShape.setDtoLinkNodes(dtoCanvas.getDtoLinks());
			}

			label.setText(dtoCanvas.getId() + " - " + dtoCanvas.getName());

			label.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					SCL.getCanvasDialog().setSecCurrCanvas(canvasLabel);

				}
			});

			label.addDoubleClickHandler(new DoubleClickHandler() {
				public void onDoubleClick(DoubleClickEvent event) {
					SCL.getCanvasDialog().openCanvas(canvasLabel);

				}
			});
		}

		initWidget(label);

	}

	public DTOCanvas getDtoCanvas() {
		return dtoCanvas;
	}

	public LinkShape getLinkShape() {
		return linkShape;
	}

	public void setLinkShape(LinkShape linkShape) {
		this.linkShape = linkShape;

	}

	public Vector<CanvasLabel> getCanvasLabels() {
		return canvasLabels;
	}

	public void setCanvasLabels(Vector<CanvasLabel> canvasLabels) {
		this.canvasLabels = canvasLabels;
	}

	public void highlight() {
		label.getElement().getStyle().setBackgroundColor("yellow");

	}

	public void unhighlight() {
		label.getElement().getStyle().setBackgroundColor("transparent");

	}

	public void getLink(int x, int y) {
		if(contains(x,y)!=null) {
			Window.alert(x + ", " + y);
		}

	}

	public DTOCanvas contains(double x, double y) {

		DTOCanvas tempCanvas = null;
		if(dtoCanvases!=null) {
			for(int i=0; i<dtoCanvases.getDTOCanvases().size(); i++) {
				tempCanvas = dtoCanvases.getDTOCanvases().get(i);
				if(tempCanvas.getDtoLinks()!=null) {
					Polygon polygon = new Polygon();
					Iterator<DTOLink> it = tempCanvas.getDtoLinks().getDTOLinks().iterator();
					while(it.hasNext()) {
						DTOLink p = it.next();
						polygon.addPoint((int)((double)p.getX()*SCL.getCanvasScreen().zoom), (int)((double)p.getY()*SCL.getCanvasScreen().zoom));
					}
					if(polygon.contains(x,y)) {
						return tempCanvas;
					}
				} 

			}
		}
		return null;

	}

	public void setDtoCanvases(DTOCanvases dtoCanvases) {
		this.dtoCanvases = dtoCanvases;
		canvasLabels.clear();
		if(dtoCanvases!=null) {
			Iterator<DTOCanvas> it = dtoCanvases.getDTOCanvases().iterator();
			while(it.hasNext()) {
				CanvasLabel canvasLabel = new CanvasLabel(it.next());
				this.canvasLabels.add(canvasLabel);


			}
		} if(dtoCanvas!=null) {
			SCL.getCanvasDialog().updatePrimeCanvas();
		} else {
			SCL.getCanvasDialog().updateRootCanvas();
		}

	}
	
	public void drawLinks() {
		if(dtoCanvases!=null) {
			Iterator<DTOCanvas> it = dtoCanvases.getDTOCanvases().iterator();
			while(it.hasNext()) {
				CanvasLabel canvasLabel = new CanvasLabel(it.next());
				if(canvasLabel.getLinkShape()!=null) {
					canvasLabel.getLinkShape().draw();
				}
			}
		}
	}

}
