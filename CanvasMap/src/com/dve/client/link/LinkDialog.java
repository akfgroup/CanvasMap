package com.dve.client.link;

import com.dve.client.canvas.CanvasScreen;
import com.dve.client.dialogs.nonmodal.NonModalClickhandler;
import com.dve.client.dialogs.nonmodal.NonModalDialog;
import com.dve.client.selector.SC;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlexTable;


public class LinkDialog {
	
	public NonModalDialog nonModalDialog = new NonModalDialog();
	
	public FlexTable flexTable = new FlexTable();
	
	LinkShapeLabel currLabel;
	LinkShapeLabel prevLabel;
	
	LinkDialog linkShapeDialog;
	public CanvasScreen canvasPanel;
	
	public LinkDialog(CanvasScreen canvasPanel) {
		this.linkShapeDialog = this;
		this.canvasPanel = canvasPanel;
		
		nonModalDialog.setAutoHideEnabled(false);
		nonModalDialog.setPreviewingAllNativeEvents(false);
		nonModalDialog.setWidget(flexTable);
		nonModalDialog.setText("Links");
		
		nonModalDialog.addClickHandler(new NonModalClickhandler());
		
		DOM.setStyleAttribute(nonModalDialog.getElement(), "backgroundColor", "white");
		
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
//		if(currLabel!=null) {
//			currLabel.highlight();
//			canvasPanel.setCurrShape(currLabel.getLinkShape());
//		} else {
//			canvasPanel.setCurrShape(null);
//		}
		
		
	}
	

}
