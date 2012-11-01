package com.dve.client.resource;

import com.dve.client.selector.SC;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CanvasResourcePanel extends Composite {
	
	VerticalPanel mainPanel = new VerticalPanel();
	
	HorizontalPanel topPanel = new HorizontalPanel();
	VerticalPanel centerPanel = new VerticalPanel();
	HorizontalPanel bottomPanel = new HorizontalPanel();
	
	Button orgBtn = new Button("Org");
	Button bldgBtn = new Button("Building");
	
	public CanvasResourcePanel() {
		
		topPanel.add(orgBtn);
		topPanel.add(bldgBtn);
		
		mainPanel.add(topPanel);
		mainPanel.add(centerPanel);
		mainPanel.add(bottomPanel);
		
		orgBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.getOrgDialog().show();
				
			}
		});
		
		bldgBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.getBuildingDialog().show();
				
			}
		});
		
		initWidget(mainPanel);
		
	}
}
