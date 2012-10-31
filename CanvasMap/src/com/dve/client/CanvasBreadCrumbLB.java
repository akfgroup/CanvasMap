package com.dve.client;

import java.util.Iterator;

import com.dve.client.canvas.dialog.CanvasLabel;
import com.dve.client.selector.SCL;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;

public class CanvasBreadCrumbLB extends Composite {
	
	CanvasLabel canvasLabel;
	ListBox listBox = new ListBox();
	
	public CanvasBreadCrumbLB(final CanvasLabel canvasLabel) {
		this.canvasLabel = canvasLabel;
		listBox.addItem("");
		Iterator<CanvasLabel> it = canvasLabel.getCanvasLabels().iterator();
		while(it.hasNext()) {
			CanvasLabel temp = it.next();
			listBox.addItem(temp.getDtoCanvas().getId() + " - " + temp.getDtoCanvas().getName());
		}
		listBox.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				if(listBox.getSelectedIndex()>0) {
					SCL.getCanvasDialog().openCanvas(canvasLabel.getCanvasLabels().get(listBox.getSelectedIndex()-1));
				}
			}
		});
		initWidget(listBox);
	}

}
