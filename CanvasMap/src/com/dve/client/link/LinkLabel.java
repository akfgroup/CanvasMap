package com.dve.client.link;

import com.dve.client.selector.SCL;
import com.dve.shared.dto.canvas.DTOCanvas;
import com.dve.shared.dto.canvas.DTOLink;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;

public class LinkLabel extends Composite {
	
	LinkLabel linkLabel;
	DTOLink dtoLink;
	
	Label label = new Label();
	
	public LinkLabel(DTOLink dtoLink) {
		this.dtoLink = dtoLink;
		this.linkLabel = this;
		
		label.setText(dtoLink.getX() + ", " + dtoLink.getY());
		
		label.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SCL.getCanvasDialog().setCurrLinkNode(linkLabel);
				
			}
		});
		
		label.addDoubleClickHandler(new DoubleClickHandler() {
			public void onDoubleClick(DoubleClickEvent event) {
//				SCL.getCanvasDialog().openCanvas(linkLabel);
				
			}
		});
		
		initWidget(label);
		
	}
	
	public DTOLink getDtoLinkNode() {
		return dtoLink;
	}

	public void highlight() {
		label.getElement().getStyle().setBackgroundColor("yellow");
		
	}
	
	public void unhighlight() {
		label.getElement().getStyle().setBackgroundColor("transparent");
		
	}


}
