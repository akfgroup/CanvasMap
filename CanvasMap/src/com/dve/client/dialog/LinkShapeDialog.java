package com.dve.client.dialog;

import com.dve.client.CanvasMap;
import com.dve.client.LinkShape;
import com.dve.client.LinkShapeLabel;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlexTable;


public class LinkShapeDialog {
	
	public NonModalDialog nonModalDialog = new NonModalDialog();
	
	public FlexTable flexTable = new FlexTable();
	
	LinkShapeLabel currLabel;
	LinkShapeLabel prevLabel;
	
	LinkShapeDialog linkShapeDialog;
	public CanvasMap canvasMap;
	
	public LinkShapeDialog(CanvasMap canvasMap) {
		this.linkShapeDialog = this;
		this.canvasMap = canvasMap;
		
		nonModalDialog.setAutoHideEnabled(false);
		nonModalDialog.setPreviewingAllNativeEvents(false);
		nonModalDialog.setWidget(flexTable);
		nonModalDialog.setText("Link Shape");
		
		DOM.setStyleAttribute(nonModalDialog.getElement(), "backgroundColor", "white");
		
	}

	public void show() {
		nonModalDialog.show();

	}
	
	public void update(int left, int top) {
		nonModalDialog.setPopupPosition(left, top);
	}
	
	public void addLinkShape(LinkShape linkShape) {
		LinkShapeLabel linkShapeLabel = new LinkShapeLabel(linkShapeDialog, linkShape);
		flexTable.setWidget(flexTable.getRowCount(), 0, linkShapeLabel);
		setCurrLinkShapeLabel(linkShapeLabel);
		
	}

	public void setCurrLinkShapeLabel(LinkShapeLabel linkShapeLabel) {
		prevLabel = currLabel;
		if(prevLabel!=null) {
			prevLabel.unhighlight();
		}
		currLabel = linkShapeLabel;
		if(currLabel!=null) {
			currLabel.highlight();
			canvasMap.setCurrShape(currLabel.getLinkShape());
		} else {
			canvasMap.setCurrShape(null);
		}
		
		
	}
	

}
