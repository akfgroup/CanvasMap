package com.dve.client;

import java.util.Iterator;
import java.util.logging.Logger;
import com.dve.client.canvas.dialog.CanvasLabel;
import com.dve.client.selector.SCL;
import com.dve.equip.client.utilities.EquipUtilities;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;

public class CanvasBreadCrumb extends Composite {

	HorizontalPanel mainPanel0 = new HorizontalPanel();
	HorizontalPanel mainPanel1 = new HorizontalPanel();

	PushButton backBtn = new PushButton(new Image(EquipUtilities.getIr().undo()));
	ListBox rootCanvas = new ListBox();

	Logger log = Logger.getLogger(CanvasBreadCrumb.class.getName());

	public CanvasBreadCrumb() {

		rootCanvas.addItem("");
		mainPanel1.add(backBtn);
		mainPanel1.add(rootCanvas);

		rootCanvas.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				if(rootCanvas.getSelectedIndex()>0) {
					SCL.getCanvasDialog().openCanvas(SCL.getRootLabel().getCanvasLabels().get(rootCanvas.getSelectedIndex()-1));
				}
			}
		});
		
		backBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(SCL.getCurrPrimeCanvas()!=null && SCL.getCurrPrimeCanvas().getDtoCanvas().getParentCanvas()!=null) {
					SCL.getCanvasDialog().openCanvas(SCL.getCurrPrimeCanvas().getParentCanvasLabel());
					
				} else {
					SCL.getCanvasDialog().getCanvasPanel().getRootCanvases();
					
				}
			}
		});

		mainPanel0.add(backBtn);
		mainPanel0.add(mainPanel1);
		initWidget(mainPanel0);

	}

	public void clear() {
		while(mainPanel1.getWidgetCount()>1) {
			mainPanel1.remove(1);
		}
		rootCanvas.clear();
		rootCanvas.addItem("");

	}

	public void updateRoot() {
		clear();
		Iterator<CanvasLabel> it = SCL.getRootLabel().getCanvasLabels().iterator();
		while(it.hasNext()) {
			CanvasLabel canvasLabel = it.next();
			rootCanvas.addItem(canvasLabel.getDtoCanvas().getId() + " - " + canvasLabel.getDtoCanvas().getName());

		}

	}

	public void openCanvas() {
		if(SCL.getCurrPrimeCanvas()!=null) {
			while(mainPanel1.getWidgetCount()>0) {
				mainPanel1.remove(0);
			}
			CanvasLabel tempCanvasLabel = SCL.getCurrPrimeCanvas();
			boolean valid = true;
			while(valid) {
				if(tempCanvasLabel==null) {
					valid = false;
				} else {
					CanvasBreadCrumbLB breadCrumbLB = new CanvasBreadCrumbLB(tempCanvasLabel);
					mainPanel1.insert(breadCrumbLB,0);
					tempCanvasLabel=tempCanvasLabel.getParentCanvasLabel();
				}

			}

			tempCanvasLabel = SCL.getCurrPrimeCanvas();
			valid = true;
			int widgeCnt = 2;
			while(valid) {
				if(tempCanvasLabel==null) {
					valid = false;
				} else {
					if(tempCanvasLabel!=SCL.getRootLabel()) {
//						log.info("id = " + tempCanvasLabel.getDtoCanvas().getId());
						CanvasBreadCrumbLB lb = (CanvasBreadCrumbLB) mainPanel1.getWidget(mainPanel1.getWidgetCount()-widgeCnt);
						for(int i=0; i<lb.canvasLabel.getCanvasLabels().size(); i++) {
							CanvasLabel temp = lb.canvasLabel.getCanvasLabels().get(i);
//							log.info("tempCanvasLabel.id = " + tempCanvasLabel.getDtoCanvas().getId());
//							log.info("temp.id = " + temp.getDtoCanvas().getId());
							if(tempCanvasLabel==temp) {
								lb.listBox.setSelectedIndex(i+1);
							}
						}
						widgeCnt++;
						tempCanvasLabel=tempCanvasLabel.getParentCanvasLabel();
					} else {
//						log.info("id = root");
						valid = false;
					}
				}

			}

		}

	}

}
