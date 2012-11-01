package com.dve.client.canvas.dialog;

import java.util.Iterator;
import java.util.logging.Logger;

import com.dve.client.selector.SCL;
import com.dve.client.utilities.ServiceUtilities;
import com.dve.equip.client.utilities.EquipUtilities;
import com.dve.shared.dto.canvas.DTOCanvas;
import com.dve.shared.dto.canvas.DTOCanvases;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CanvasDialogPanel extends Composite {

	VerticalPanel canvasPanel = new VerticalPanel();
	FlexTable canvasTable = new FlexTable();
	
	PushButton addBtn = new PushButton(new Image(EquipUtilities.getIr().action_add()));
	PushButton delBtn = new PushButton(new Image(EquipUtilities.getIr().action_remove()));
	HorizontalPanel canvasBtnPanel = new HorizontalPanel();
	
	Logger log = Logger.getLogger(CanvasDialogPanel.class.getName());
	
	public CanvasDialogPanel() {
		
		canvasTable.setBorderWidth(1);
		
		canvasPanel.setBorderWidth(1);
		canvasPanel.add(canvasTable);
		canvasTable.setWidth("100%");
		canvasPanel.setHeight("100%");
		canvasPanel.setWidth("100%");
		canvasPanel.setCellHeight(canvasTable, "100%");
			
		canvasBtnPanel.add(addBtn);
		canvasBtnPanel.add(delBtn);
		
		canvasPanel.add(canvasBtnPanel);
		
		addBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AsyncCallback callback = new AsyncCallback() {
					public void onFailure(Throwable caught) {
						log.severe(caught.getMessage());

					}

					public void onSuccess(Object result) {
						DTOCanvas dtoCanvas = (DTOCanvas) result;
						CanvasLabel canvasLabel = new CanvasLabel(dtoCanvas);
						canvasTable.setWidget(canvasTable.getRowCount(), 0, canvasLabel);

					}
				};

				DTOCanvas dtoCanvas = new DTOCanvas();
				if(SCL.getCurrPrimeCanvas()!=null) {
					dtoCanvas.setParentCanvas(SCL.getCurrPrimeCanvas().getDtoCanvas());
				}
				ServiceUtilities.getEquipService().createCanvas(dtoCanvas, callback);
				
			}
		});
		
		delBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AsyncCallback callback = new AsyncCallback() {
					public void onFailure(Throwable caught) {
						log.severe(caught.getMessage());

					}

					public void onSuccess(Object result) {
						if(result==null) {
							AsyncCallback callback = new AsyncCallback() {
								public void onFailure(Throwable caught) {
									log.severe(caught.getMessage());

								}

								public void onSuccess(Object result) {
									if(SCL.getCurrPrimeCanvas().getDtoCanvas().getParentCanvas()!=null) {
										SCL.getCanvasDialog().setSecCurrCanvas(SCL.getCurrPrimeCanvas().getParentCanvasLabel());

									} else {
										getRootCanvases();

									}
								}
							};

							if(SCL.getCurrPrimeCanvas()!=null) {
								ServiceUtilities.getEquipService().deleteCanvas(SCL.getCurrPrimeCanvas().getDtoCanvas(), callback);
							}
						} else {
							Window.alert("Canvas must have no children in order to delete it!");
						}
					}
				};

				if(SCL.getCurrPrimeCanvas()!=null) {
					ServiceUtilities.getEquipService().getCanvasesByCanvas(SCL.getCurrPrimeCanvas().getDtoCanvas(), callback);
				}
				
			}
		});
		
		initWidget(canvasPanel);
		
	}
	
	public void getRootCanvases() {
		AsyncCallback callback = new AsyncCallback() {
			public void onFailure(Throwable caught) {
				log.severe(caught.getMessage());

			}

			public void onSuccess(Object result) {
				log.info("here");
				CanvasLabel rootLabel = new CanvasLabel(null);
				SCL.setRootLabel(rootLabel);
				
				SCL.setCurrPrimeCanvas(null);
				SCL.setCurrSecCanvas(null);
				
				rootLabel.setDtoCanvases((DTOCanvases) result);
				
				SCL.getCanvasDialog().nonModalDialog.setText("Canvas - Roots");
			
			}
		};

		ServiceUtilities.getEquipService().getRootCanvases(callback);

	}

	public FlexTable getCanvasTable() {
		return canvasTable;
		
	}
}
