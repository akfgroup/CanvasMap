package com.dve.client;

import com.dve.client.dialog.LinkShapeDialog;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class LinkShapeLabel extends Composite {
	
	FocusPanel focusPanel = new FocusPanel();
	
	Label label = new Label();
	TextBox textBox = new TextBox();
	
	LinkShapeLabel linkShapeLabel;
	LinkShapeDialog linkShapeDialog;
	LinkShape linkShape;
	
	public LinkShapeLabel(final LinkShapeDialog linkShapeDialog, final LinkShape linkShape) {
		this.linkShapeDialog = linkShapeDialog;
		this.linkShape = linkShape;
		this.linkShapeLabel = this;
		
		label = new Label("Link Shape");
		focusPanel.setWidget(label);
		
		focusPanel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				linkShapeDialog.setCurrLinkShapeLabel(linkShapeLabel);
				
			}
		});
		
		focusPanel.addDoubleClickHandler(new DoubleClickHandler() {
			public void onDoubleClick(DoubleClickEvent event) {
				if(focusPanel.getWidget()==label) {
					textBox.setText(label.getText());
					focusPanel.setWidget(textBox);
				}
			}
		});
		
		textBox.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				if(textBox.getText().length()>3) {
					label.setText(textBox.getText());
				}
				focusPanel.setWidget(label);
			}
		});
		
		textBox.addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				focusPanel.setWidget(label);
				
			}
		});
		
		initWidget(focusPanel);
		
	}
	
	public LinkShape getLinkShape() {
		return linkShape;
		
	}
	
	public void highlight() {
		DOM.setStyleAttribute(focusPanel.getElement(), "backgroundColor", "yellow");
		
	}
	
	public void unhighlight() {
		DOM.setStyleAttribute(focusPanel.getElement(), "backgroundColor", "white");

		
	}
	
	

}
