package com.dve.client.link;

import java.util.Iterator;
import java.util.logging.Logger;

import com.dve.client.canvas.dialog.CanvasLabel;
import com.dve.client.selector.SCL;
import com.dve.client.utilities.ServiceUtilities;
import com.dve.equip.client.utilities.EquipUtilities;
import com.dve.shared.dto.canvas.DTOCanvas;
import com.dve.shared.dto.canvas.DTOCanvases;
import com.dve.shared.dto.canvas.DTOLink;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LinkPanel extends Composite {

	VerticalPanel linkPanel = new VerticalPanel();
	FlexTable linkTable = new FlexTable();
	
	Button modeBtn = new Button("On");
	HorizontalPanel canvasBtnPanel = new HorizontalPanel();
	
	Logger log = Logger.getLogger(LinkPanel.class.getName());
	
	public LinkPanel() {
		
		linkTable.setBorderWidth(1);
		
		linkPanel.setBorderWidth(1);
		linkPanel.add(linkTable);
		linkTable.setWidth("100%");
		linkPanel.setHeight("100%");
		linkPanel.setWidth("100%");
		linkPanel.setCellHeight(linkTable, "100%");
			
		canvasBtnPanel.add(modeBtn);
		
		linkPanel.add(canvasBtnPanel);
		
		modeBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(modeBtn.getText().equals("On")) {
					modeBtn.setText("Off");
				} else {
					modeBtn.setText("On");
				}
			}
		});
		
		
		initWidget(linkPanel);
		
	}
	
	public void addLink() {
		
	}

	public FlexTable getLinkTable() {
		return linkTable;
		
	}

	public void updateLinks() {
		linkTable.removeAllRows();
		if(SCL.getCurrSecCanvas()!=null && SCL.getCurrSecCanvas().getDtoCanvas().getDtoLinks()!=null) {
			Iterator<DTOLink> it = SCL.getCurrSecCanvas().getDtoCanvas().getDtoLinks().getDTOLinks().iterator();
			while(it.hasNext()) {
				DTOLink dtoLink = it.next();
				LinkLabel linkLabel = new LinkLabel(dtoLink);
				linkTable.setWidget(linkTable.getRowCount(), 1, linkLabel);
				
			}
			
		}
		
	}

}
