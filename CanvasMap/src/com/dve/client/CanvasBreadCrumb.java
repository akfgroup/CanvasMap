package com.dve.client;

import java.util.Iterator;
import java.util.logging.Logger;
import com.dve.client.canvas.dialog.CanvasLabel;
import com.dve.client.selector.SC;
import com.dve.client.selector.SCL;
import com.dve.equip.client.utilities.EquipUtilities;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;

public class CanvasBreadCrumb extends Composite {

	HorizontalPanel mainPanel0 = new HorizontalPanel();
	HorizontalPanel mainPanel1 = new HorizontalPanel();

	Button dialogBtn = new Button("Dialog");
	PushButton backBtn = new PushButton(new Image(EquipUtilities.getIr().undo()));
	ListBox rootCanvas = new ListBox();

	Logger log = Logger.getLogger(CanvasBreadCrumb.class.getName());

	public CanvasBreadCrumb() {

		rootCanvas.addItem("");
		mainPanel0.add(dialogBtn);
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
					log.info("1");
					SCL.getCanvasDialog().openCanvas(SCL.getCurrPrimeCanvas().getParentCanvasLabel());
					
				} else {
					log.info("2");
					SC.getCanvasMap().getRootCanvases();
					
				}
				
			}
		});
		
		dialogBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SCL.getCanvasDialog().center();
				
			}
		});

		mainPanel0.add(backBtn);
		mainPanel0.add(mainPanel1);
		initWidget(mainPanel0);

	}

	public void updateRoot() {
		rootCanvas.clear();
		rootCanvas.addItem("");
		Iterator<CanvasLabel> it = SCL.getRootLabel().getCanvasLabels().iterator();
		while(it.hasNext()) {
			CanvasLabel canvasLabel = it.next();
			rootCanvas.addItem(canvasLabel.getDtoCanvas().getId() + " - " + canvasLabel.getDtoCanvas().getName());

		}
		if(mainPanel1.getWidget(0).getClass()==CanvasBreadCrumbLB.class) {
			CanvasBreadCrumbLB cbLB = (CanvasBreadCrumbLB) mainPanel1.getWidget(0);
			cbLB.listBox.setSelectedIndex(0);
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
						try {
							CanvasBreadCrumbLB lb = (CanvasBreadCrumbLB) mainPanel1.getWidget(mainPanel1.getWidgetCount()-widgeCnt);
							for(int i=0; i<lb.canvasLabel.getCanvasLabels().size(); i++) {
								CanvasLabel temp = lb.canvasLabel.getCanvasLabels().get(i);
								//							log.info("tempCanvasLabel.id = " + tempCanvasLabel.getDtoCanvas().getId());
								//							log.info("temp.id = " + temp.getDtoCanvas().getId());
								if(tempCanvasLabel==temp) {
									lb.listBox.setSelectedIndex(i+1);
								}
							}
						} catch (IndexOutOfBoundsException e) {
							log.severe("OpenCanvas Error IndexOutOfBounds()!");
							
						}
						widgeCnt++;
						if(widgeCnt>mainPanel1.getWidgetCount()) {
							valid = false;
						}
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
