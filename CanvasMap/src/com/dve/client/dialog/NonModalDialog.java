package com.dve.client.dialog;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.DialogBox;

public class NonModalDialog extends DialogBox {
	
	
	public NonModalDialog() {
		super(false, false);

	}
	
	public HandlerRegistration addClickHandler(ClickHandler clickHandler) {
		return addDomHandler(clickHandler, ClickEvent.getType());
		
	}
	
	public HandlerRegistration addKeyDownHandler(KeyDownHandler keyDownHandler) {
		return addDomHandler(keyDownHandler, KeyDownEvent.getType());
		
	}
	
	public HandlerRegistration addKeyUpHandler(KeyUpHandler keyUpHandler) {
		return addDomHandler(keyUpHandler, KeyUpEvent.getType());
		
	}

	protected static native void killContextMenu() /*-{ 
$doc.oncontextmenu = function() { return false; }; 
	}-*/; 

	protected static native void aliveContextMenu() /*-{ 
$doc.oncontextmenu = function() { return true; }; 
	}-*/;

}
