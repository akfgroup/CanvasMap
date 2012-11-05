package com.dve.client.canvas.dialog;


import java.util.logging.Logger;

import com.dve.client.selector.SCL;
import com.dve.client.utilities.ServiceUtilities;
import com.dve.shared.dto.canvas.DTOCanvas;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class CanvasNameLabel extends Composite {

	
	HorizontalPanel mainPanel = new HorizontalPanel();
	Label nameLA = new Label();
	TextBox nameTB = new TextBox();
	
	Logger log = Logger.getLogger(CanvasNameLabel.class.getName());
	
	public CanvasNameLabel() {
		
		nameLA.setPixelSize(300, 20);
		nameLA.getElement().getStyle().setBorderColor("gray");
		nameLA.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		nameLA.getElement().getStyle().setBorderWidth(1, Unit.PX);
		
		nameTB.setPixelSize(300, 20);
		nameTB.getElement().getStyle().setBorderColor("yellow");
		nameTB.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		nameTB.getElement().getStyle().setBorderWidth(1, Unit.PX);
		
		nameLA.addDoubleClickHandler(new DoubleClickHandler() {
			public void onDoubleClick(DoubleClickEvent event) {
				mainPanel.clear();
				nameTB.setText(nameLA.getText());
				mainPanel.add(nameTB);
				
			}
		});
		
		nameTB.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				SCL.getCurrPrimeCanvas().getDtoCanvas().setName(nameTB.getText());
				
				AsyncCallback callback = new AsyncCallback() {
					public void onFailure(Throwable caught) {
						log.severe(caught.getMessage());

					}

					public void onSuccess(Object result) {
						DTOCanvas dtoCanvas = (DTOCanvas) result;
						nameLA.setText(dtoCanvas.getName());
						mainPanel.clear();
						mainPanel.add(nameLA);
						
					}
				};
				ServiceUtilities.getEquipService().updateCanvas(SCL.getCurrPrimeCanvas().getDtoCanvas(), callback);
			}
		});
		
		mainPanel.add(nameLA);
		initWidget(mainPanel);
		
	}
	
	public void update() {
		if(SCL.getCurrPrimeCanvas()!=null) {
			nameLA.setText(SCL.getCurrPrimeCanvas().getDtoCanvas().getName());
		}
		
	}
}
