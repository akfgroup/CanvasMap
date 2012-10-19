package com.dve.client.image;

import gwtupload.client.SingleUploader;

import com.dve.client.dialogs.nonmodal.NonModalClickhandler;
import com.dve.client.dialogs.nonmodal.NonModalDialog;
import com.dve.client.selector.SC;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ImageDialog {
	
	public NonModalDialog nonModalDialog = new NonModalDialog();
	
	VerticalPanel mainPanel = new VerticalPanel();
	
	HorizontalPanel topPanel = new HorizontalPanel();
	HorizontalPanel centerPanel = new HorizontalPanel();
	HorizontalPanel bottomPanel = new HorizontalPanel();
	
	SingleUploader singleUploader = new SingleUploader();
	
	public ImageDialog() {
		
		singleUploader.setServletPath(".gupld?canvasId=999");
		
		bottomPanel.setWidth("100%");
		
		mainPanel.setBorderWidth(1);
		topPanel.add(singleUploader);
		mainPanel.add(topPanel);

		mainPanel.setCellVerticalAlignment(centerPanel, HasVerticalAlignment.ALIGN_TOP);
	
		mainPanel.setCellWidth(centerPanel, "100%");
		mainPanel.setCellHeight(centerPanel, "100%");
		centerPanel.setWidth("100%");
		centerPanel.setHeight("100%");
		mainPanel.add(bottomPanel);
	
		nonModalDialog.setAutoHideEnabled(false);
		nonModalDialog.setPreviewingAllNativeEvents(false);
		nonModalDialog.setWidget(mainPanel);
		nonModalDialog.setText("Images");

		nonModalDialog.addClickHandler(new NonModalClickhandler());

		DOM.setStyleAttribute(nonModalDialog.getElement(), "backgroundColor", "white");

	}
	
	public void center() {
		Timer t = new Timer() {
			public void run() {
				SC.setSelectedDialog(nonModalDialog);
			}
		};
		t.schedule(50);
		
		nonModalDialog.center();

	}
	
	public void show() {
		Timer t = new Timer() {
			public void run() {
				SC.setSelectedDialog(nonModalDialog);
			}
		};
		t.schedule(50);
		
		nonModalDialog.show();

	}

}
