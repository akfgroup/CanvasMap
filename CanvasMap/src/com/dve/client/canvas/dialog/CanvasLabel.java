package com.dve.client.canvas.dialog;

import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import com.dve.client.canvas.screen.CanvasScreen;
import com.dve.client.link.LinkShape;
import com.dve.client.resource.CanvasResourcePanel;
import com.dve.client.selector.SCL;
import com.dve.client.utilities.ServiceUtilities;
import com.dve.shared.dto.canvas.DTOCanvas;
import com.dve.shared.dto.canvas.DTOCanvases;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.reveregroup.gwt.imagepreloader.ImageLoadEvent;
import com.reveregroup.gwt.imagepreloader.ImageLoadHandler;
import com.reveregroup.gwt.imagepreloader.ImagePreloader;

public class CanvasLabel extends Composite {

	CanvasLabel canvasLabel;
	DTOCanvas dtoCanvas;
	
	CanvasLabel parentCanvasLabel;
	
	CanvasScreen canvasScreen = new CanvasScreen(this);
	CanvasResourcePanel resourcePanel = new CanvasResourcePanel();
	
	Vector<CanvasLabel> canvasLabels = new Vector<CanvasLabel>();
	
	LinkShape linkShape;

	Label label = new Label();
	
	Logger log = Logger.getLogger(CanvasLabel.class.getName());

	public CanvasLabel(DTOCanvas dtoCanvas) {
		this.dtoCanvas = dtoCanvas;
		this.canvasLabel = this;

		if(dtoCanvas!=null) {
			
			if(dtoCanvas.getDtoLinks()!=null) {
				linkShape = new LinkShape();
				linkShape.setDtoLinkNodes(dtoCanvas.getDtoLinks());
				
			}

			label.setText(dtoCanvas.getId() + " - " + dtoCanvas.getName());

			label.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					SCL.getCanvasDialog().setSecCurrCanvas(canvasLabel);
					if(SCL.getCurrPrimeCanvas()!=null) {
						SCL.getCurrPrimeCanvas().drawLinks();
					}
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
	
	public CanvasLabel getParentCanvasLabel() {
		return parentCanvasLabel;
	}
	
	private void setParentCanvasLabel(CanvasLabel parentCanvasLabel) {
		this.parentCanvasLabel = parentCanvasLabel;
		
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
	
	public CanvasScreen getCanvasScreen() {
		return canvasScreen;
	}

	public void setCanvasScreen(CanvasScreen canvasScreen) {
		this.canvasScreen = canvasScreen;
	}

	public CanvasResourcePanel getResourcePanel() {
		return resourcePanel;
	}

	public void setResourcePanel(CanvasResourcePanel resourcePanel) {
		this.resourcePanel = resourcePanel;
	}

	public void highlight() {
		label.getElement().getStyle().setBackgroundColor("yellow");

	}

	public void unhighlight() {
		label.getElement().getStyle().setBackgroundColor("transparent");

	}

	public CanvasLabel contains(double x, double y) {
//		log.info("# Canvas Labels = " + canvasLabels.size());
		Iterator<CanvasLabel> it = canvasLabels.iterator();
		while(it.hasNext()) {
			CanvasLabel temp = it.next();
			if(temp.linkShape!=null && temp.linkShape.contains(x, y)) {
				return temp;
			}
		}
		return null;

	}

	public void setDtoCanvases(DTOCanvases dtoCanvases) {
		canvasLabels.clear();
		if(dtoCanvases!=null) {
			Iterator<DTOCanvas> it = dtoCanvases.getDTOCanvases().iterator();
			while(it.hasNext()) {
				DTOCanvas dtoCanvas = it.next();
				CanvasLabel temp = new CanvasLabel(dtoCanvas);
				temp.setParentCanvasLabel(canvasLabel);
				this.canvasLabels.add(temp);

			}
		} 

	}

	public void drawLinks() {
		if(canvasLabels!=null) {
			Iterator<CanvasLabel> it = canvasLabels.iterator();
			while(it.hasNext()) {
				CanvasLabel canvasLabel = it.next();
				if(canvasLabel.getLinkShape()!=null) {
//					log.info("draw link");
					canvasLabel.getLinkShape().draw();
				}
			}
		}
	}

	public void updateImage() {
		canvasScreen.updateImage();
		
	}

	public void updateResource() {
		AsyncCallback callback = new AsyncCallback() {
			public void onFailure(Throwable caught) {
				log.severe(caught.getMessage());

			}

			public void onSuccess(Object result) {
				DTOCanvas dtoCanvas = (DTOCanvas) result;
				resourcePanel.updateResourcePanel();
				
			}
		};
		ServiceUtilities.getEquipService().updateCanvas(SCL.getCurrPrimeCanvas().getDtoCanvas(), callback);

	}

	public void unhighlightLink() {
		if(linkShape!=null) {
			linkShape.unhighlight();
		}
		
	}

	public void highlighLink() {
		if(linkShape!=null) {
			linkShape.highlight();
		}
		
	}


}
