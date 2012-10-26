package com.dve.client.canvas.dialog;

import com.dve.client.link.LinkShape;
import com.dve.client.selector.SCL;
import com.dve.shared.dto.canvas.DTOCanvas;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;

public class CanvasLabel extends Composite {
	
	CanvasLabel canvasLabel;
	
	LinkShape linkShape;
	DTOCanvas dtoCanvas;
	
	Label label = new Label();
	
	public CanvasLabel(DTOCanvas dtoCanvas) {
		this.dtoCanvas = dtoCanvas;
		this.canvasLabel = this;
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
		
		initWidget(label);
		
	}
	
	public DTOCanvas getDtoCanvas() {
		return dtoCanvas;
	}
	
	public LinkShape getLinkShape() {
		if(linkShape==null) {
			linkShape = new LinkShape(SCL.getCanvasScreen());
		}
		return linkShape;
	}

	public void setLinkShape(LinkShape linkShape) {
		this.linkShape = linkShape;
		
	}

	public void highlight() {
		label.getElement().getStyle().setBackgroundColor("yellow");
		
	}
	
	public void unhighlight() {
		label.getElement().getStyle().setBackgroundColor("transparent");
		
	}


}
