package com.dve.client.canvas.dialog;

import com.dve.client.dialogs.nonmodal.NonModalDialog;
import com.dve.client.managers.form.FormsManagerDialog;
import com.dve.client.managers.form.tree.FormTreeManager;
import com.dve.client.properties.PropertiesDialog;
import com.dve.client.utilities.FormUtilities;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Waiting {
	
	private NonModalDialog waiting = new NonModalDialog();
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private final Label progressLabel = new Label("");
	
	private boolean exists = false;;
	private boolean isShowing = false;
	
	public Waiting() {
		create();
	}
	
	private void create() {
		
		waiting = new NonModalDialog();
		waiting.setStylePrimaryName("newProjectDialog");
		waiting.setPopupPosition(-100, -100);
		mainPanel.add(progressLabel);
		
		mainPanel.add(FormUtilities.getColorPie());
		
		waiting.setWidget(mainPanel);
		waiting.getElement().getStyle().setZIndex(9999);
		
		exists = true;

	}
	
	public void setProgress(String text) {
		progressLabel.setText(text);
	}
	
	public void close() {
		waiting.hide();
		
		isShowing = false;
	}
	
	public void show() {
		waiting.center();
		
		isShowing = true;
	}
	
	public boolean exists() {
		return exists;
	}
	
	public boolean isShowing() {
		return isShowing;
	}
	
}
