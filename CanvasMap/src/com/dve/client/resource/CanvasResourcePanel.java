package com.dve.client.resource;

import com.dve.client.selector.SC;
import com.dve.client.selector.SCL;
import com.dve.equip.client.resources.BldgEqpResource;
import com.dve.equip.client.resources.BldgResource;
import com.dve.equip.client.resources.FlrResource;
import com.dve.equip.client.resources.OrgResource;
import com.dve.equip.client.resources.ResourcePanel;
import com.dve.equip.client.resources.RmResource;
import com.dve.shared.enums.ASSETS;
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

	public void updateResourcePanel() {
		centerPanel.clear();
		if(SCL.getCurrPrimeCanvas().getDtoCanvas().getAssetId()!=-1) {
			if(SCL.getCurrPrimeCanvas().getDtoCanvas().getAssetType()==ASSETS.get("org")) {
				OrgResource resource = new OrgResource();
				resource.setAssetId(SCL.getCurrPrimeCanvas().getDtoCanvas().getAssetId());
				resource.setName(SCL.getCurrPrimeCanvas().getDtoCanvas().getId() + " - " + SCL.getCurrPrimeCanvas().getDtoCanvas().getName());
				ResourcePanel resourcePanel = new ResourcePanel(resource);
				resourcePanel.update();
				centerPanel.add(resourcePanel);
				return;
			}
			if(SCL.getCurrPrimeCanvas().getDtoCanvas().getAssetType()==ASSETS.get("building")) {
				BldgResource resource = new BldgResource();
				resource.setAssetId(SCL.getCurrPrimeCanvas().getDtoCanvas().getAssetId());
				resource.setName(SCL.getCurrPrimeCanvas().getDtoCanvas().getId() + " - " + SCL.getCurrPrimeCanvas().getDtoCanvas().getName());
				ResourcePanel resourcePanel = new ResourcePanel(resource);
				resourcePanel.update();
				centerPanel.add(resourcePanel);
				return;
			}
			if(SCL.getCurrPrimeCanvas().getDtoCanvas().getAssetType()==ASSETS.get("floor")) {
				FlrResource resource = new FlrResource();
				resource.setAssetId(SCL.getCurrPrimeCanvas().getDtoCanvas().getAssetId());
				resource.setName(SCL.getCurrPrimeCanvas().getDtoCanvas().getId() + " - " + SCL.getCurrPrimeCanvas().getDtoCanvas().getName());
				ResourcePanel resourcePanel = new ResourcePanel(resource);
				resourcePanel.update();
				centerPanel.add(resourcePanel);
				return;
			}
			if(SCL.getCurrPrimeCanvas().getDtoCanvas().getAssetType()==ASSETS.get("room")) {
				RmResource resource = new RmResource();
				resource.setAssetId(SCL.getCurrPrimeCanvas().getDtoCanvas().getAssetId());
				resource.setName(SCL.getCurrPrimeCanvas().getDtoCanvas().getId() + " - " + SCL.getCurrPrimeCanvas().getDtoCanvas().getName());
				ResourcePanel resourcePanel = new ResourcePanel(resource);
				resourcePanel.update();
				centerPanel.add(resourcePanel);
				return;
			}
			if(SCL.getCurrPrimeCanvas().getDtoCanvas().getAssetType()==ASSETS.get("bldgequip")) {
				BldgEqpResource resource = new BldgEqpResource();
				resource.setAssetId(SCL.getCurrPrimeCanvas().getDtoCanvas().getAssetId());
				resource.setName(SCL.getCurrPrimeCanvas().getDtoCanvas().getId() + " - " + SCL.getCurrPrimeCanvas().getDtoCanvas().getName());
				ResourcePanel resourcePanel = new ResourcePanel(resource);
				resourcePanel.update();
				centerPanel.add(resourcePanel);
				return;
			}
			
		}
		
	}
	
}
