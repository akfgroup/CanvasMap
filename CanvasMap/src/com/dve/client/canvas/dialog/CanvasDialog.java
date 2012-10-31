package com.dve.client.canvas.dialog;

import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.IUploader.UploadedInfo;

import com.dve.client.CanvasBreadCrumb;
import com.dve.client.canvas.CanvasPanel;
import com.dve.client.dialogs.nonmodal.ModalDialog;
import com.dve.client.dialogs.nonmodal.NonModalClickhandler;
import com.dve.client.link.LinkLabel;
import com.dve.client.link.LinkPanel;
import com.dve.client.selector.SC;
import com.dve.client.selector.SCL;
import com.dve.client.utilities.ServiceUtilities;
import com.dve.equip.client.utilities.EquipUtilities;
import com.dve.shared.dto.canvas.DTOCanvas;
import com.dve.shared.dto.canvas.DTOCanvases;
import com.dve.shared.dto.canvas.DTOLinks;
import com.dve.shared.dto.project.DTOProject;
import com.dve.shared.dto.project.DTOProjects;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;


public class CanvasDialog {
	
	public ModalDialog nonModalDialog = new ModalDialog();
	
	VerticalPanel mainPanel = new VerticalPanel();
	
	HorizontalPanel topPanel = new HorizontalPanel();
	HorizontalPanel centerPanel = new HorizontalPanel();
	HorizontalPanel bottomPanel = new HorizontalPanel();
		
	FlexTable flexTable = new FlexTable();
	
	CanvasPanel canvasPanel = new CanvasPanel();
	VerticalPanel imagePanel = new VerticalPanel();
	LinkPanel linkPanel = new LinkPanel();
	
	SingleUploader singleUploader = new SingleUploader();
	Label imageLA = new Label();
	
	PushButton backBtn = new PushButton(new Image(EquipUtilities.getIr().undo()));
	Button modeBtn = new Button("Edit Mode");
	Label zoomLA = new Label("Zoom = ");
	
	DTOProjects dtoProjects;
	DTOProject dtoProject;
	
	Logger log = Logger.getLogger(CanvasDialog.class.getName());
	
	public CanvasDialog() {
		
		singleUploader.setServletPath(".gupld?canvasId=");
		
		bottomPanel.setWidth("100%");
		
		topPanel.add(backBtn);
		topPanel.add(modeBtn);
		topPanel.add(zoomLA);
		
		mainPanel.setBorderWidth(1);
		mainPanel.add(topPanel);

		flexTable.setBorderWidth(1);
		flexTable.setWidget(0,0,canvasPanel);
		flexTable.getFlexCellFormatter().setRowSpan(0,0,2);
		
		flexTable.getFlexCellFormatter().setHeight(0,0,"500px");
		flexTable.getFlexCellFormatter().setWidth(0,0,"300px");
		
		imagePanel.setBorderWidth(1);
		imagePanel.add(singleUploader);
		imagePanel.add(imageLA);
		
		flexTable.setWidget(0,1,imagePanel);
		flexTable.setWidget(1,0,linkPanel);
		
		flexTable.getFlexCellFormatter().setAlignment(0,1,HasHorizontalAlignment.ALIGN_LEFT,HasVerticalAlignment.ALIGN_TOP);
		
		flexTable.getFlexCellFormatter().setHeight(1,0,"400px");
		flexTable.getFlexCellFormatter().setWidth(1,0,"500px");
		
		centerPanel.add(flexTable);

		mainPanel.add(centerPanel);
		mainPanel.setCellVerticalAlignment(centerPanel, HasVerticalAlignment.ALIGN_TOP);
	
		mainPanel.setCellWidth(centerPanel, "100%");
		mainPanel.setCellHeight(centerPanel, "100%");
		centerPanel.setWidth("100%");
		centerPanel.setHeight("100%");
		
		mainPanel.add(bottomPanel);
	
		nonModalDialog.setAutoHideEnabled(false);
		nonModalDialog.setPreviewingAllNativeEvents(false);
		nonModalDialog.setWidget(mainPanel);
		nonModalDialog.setText("Canvas");
		nonModalDialog.setModal(false);

		nonModalDialog.addClickHandler(new NonModalClickhandler());

		DOM.setStyleAttribute(nonModalDialog.getElement(), "backgroundColor", "white");
		
		backBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(SCL.getCurrPrimeCanvas()!=null && SCL.getCurrPrimeCanvas().getDtoCanvas().getParentCanvas()!=null) {
					SCL.getCanvasDialog().openCanvas(SCL.getCurrPrimeCanvas().getParentCanvasLabel());
					
				} else {
					canvasPanel.getRootCanvases();
					
				}
			}
		});
		
		modeBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(SCL.getCanvasScreen().editMode) {
					SCL.getCanvasScreen().editMode = false;
					modeBtn.setText("User Mode");
					return;
				} 
				if(!SCL.getCanvasScreen().editMode) {
					SCL.getCanvasScreen().editMode = true;
					modeBtn.setText("Edit Mode");
					return;
				}
				
			}
		});
		
		singleUploader.addOnFinishUploadHandler(new OnFinishUploaderHandler() {
			public void onFinish(IUploader uploader) {
				if(uploader.getStatus()==Status.SUCCESS) {
					log.info("CanvasDialog Uploader #1 = " + uploader.getStatus());
					UploadedInfo info = uploader.getServerInfo();
					log.info("File Name " + info.name);
					log.info("File content-type " + info.ctype);
					log.info("File Size " + info.size);

					String msg = info.message;
					
					log.info("msg = " + msg);
					
					String name = msg.substring(0, info.message.indexOf(","));
					
					msg = msg.substring(info.message.indexOf(",")+1);
					
					String type = msg.substring(0, msg.indexOf(","));
					
					msg = msg.substring(msg.indexOf(",")+1);
					
					String id = msg.substring(0, msg.indexOf(","));
					
					msg = msg.substring(msg.indexOf(",")+1);
					
					String url = msg;

					log.info("Name = " + name);
					log.info("Type = " + type);
					log.info("id = " + id);
					log.info("url = " + url);
					
					SCL.getCurrPrimeCanvas().getDtoCanvas().setImageId(Integer.parseInt(id));
					SCL.getCurrPrimeCanvas().getDtoCanvas().setImageType(type);
					SCL.getCurrPrimeCanvas().loadImage();

				} else {
					log.severe("Error in singleUploader.onFinishUploadHandler()! " + uploader.getStatus());

				}
			}
		});
		
		singleUploader.setValidExtensions("jpg", "png");
		singleUploader.setFileInputSize(40);
		singleUploader.setEnabled(true);
		singleUploader.setVisible(true);
		
		canvasPanel.getRootCanvases();
		
	}

	public void setSecCurrCanvas(CanvasLabel canvasLabel) {
		SCL.setPrevSecCanvas(SCL.getCurrSecCanvas());
		if(SCL.getPrevSecCanvas()!=null) {
			SCL.getPrevSecCanvas().unhighlight();
		}
		SCL.setCurrSecCanvas(canvasLabel);
		if(SCL.getCurrSecCanvas()!=null) {
			SCL.getCurrSecCanvas().highlight();
			
			linkPanel.updateLinks();
			
		}
	}
	
	public void openCanvas(CanvasLabel canvasLabel) {
		SCL.setPrevPrimeCanvas(SCL.getCurrPrimeCanvas());
		SCL.setCurrSecCanvas(null);
		if(SCL.getPrevPrimeCanvas()!=null) {
			SCL.getPrevPrimeCanvas().unhighlight();
		}
		SCL.setCurrPrimeCanvas(canvasLabel);
		
		if(SCL.getCurrPrimeCanvas()!=null) {
			Vector<String> canvasLabels = new Vector<String>();
			canvasLabels.add(SCL.getCurrPrimeCanvas().getDtoCanvas().getId() + " - " + SCL.getCurrPrimeCanvas().getDtoCanvas().getName());

			DTOCanvas tempCanvas = SCL.getCurrPrimeCanvas().getDtoCanvas().getParentCanvas();;
			boolean valid = true;
			while(valid) {
				if(tempCanvas==null) {
					valid = false;
				} else {
					canvasLabels.add(tempCanvas.getId() + " - " + tempCanvas.getName());
					tempCanvas = tempCanvas.getParentCanvas();
				}
			}
			
			String label = "Canvas ";
			for(int i=canvasLabels.size()-1;i>=0; i--) {
				label = label + " - " + canvasLabels.get(i);
			}
			
			nonModalDialog.setText(label);
			SCL.getCurrPrimeCanvas().highlight();
			
			AsyncCallback callback = new AsyncCallback() {
				public void onFailure(Throwable caught) {
					log.severe(caught.getMessage());

				}

				public void onSuccess(Object result) {
					DTOCanvases dtoCanvases = (DTOCanvases) result;
					SCL.getCurrPrimeCanvas().setDtoCanvases(dtoCanvases);
					SCL.getBreadCrumb().openCanvas();
					
				}
			};
			ServiceUtilities.getEquipService().getCanvasesByCanvas(SCL.getCurrPrimeCanvas().getDtoCanvas(), callback);
				
		}
		
		SCL.getCanvasScreen().updateImage();
		singleUploader.setServletPath(".gupld?canvasId="+SCL.getCurrPrimeCanvas().getDtoCanvas().getId());
		
		
	}
	
	public void updateRootCanvas() {
		canvasPanel.getCanvasTable().removeAllRows();
		Iterator<CanvasLabel> it = SCL.getRootLabel().getCanvasLabels().iterator();
		while(it.hasNext()) {
			CanvasLabel canvasLabel = it.next();
			canvasPanel.getCanvasTable().setWidget(canvasPanel.getCanvasTable().getRowCount(), 0, canvasLabel);
		}
		SCL.getCanvasScreen().updateImage();
		
	}
	
	public void updatePrimeCanvas() {
		canvasPanel.getCanvasTable().removeAllRows();
		Iterator<CanvasLabel> it = SCL.getCurrPrimeCanvas().getCanvasLabels().iterator();
		while(it.hasNext()) {
			CanvasLabel canvasLabel = it.next();
			canvasPanel.getCanvasTable().setWidget(canvasPanel.getCanvasTable().getRowCount(), 0, canvasLabel);
		}
		
	}
	
	public LinkPanel getLinkPanel() {
		return linkPanel;
		
	}
	
	public Label getZoomLA() {
		return zoomLA;
		
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

	public void setCurrLinkNode(LinkLabel linkLabel) {
		SCL.setPrevLinkNode(SCL.getCurrLinkNode());
		if(SCL.getPrevLinkNode()!=null) {
			SCL.getPrevLinkNode().unhighlight();
		}
		SCL.setCurrLinkNode(linkLabel);
		if(SCL.getCurrLinkNode()!=null) {
			SCL.getCurrLinkNode().highlight();
		}
		
	}

	public void updateCurrLinkNodes() {
		AsyncCallback callback = new AsyncCallback() {
			public void onFailure(Throwable caught) {
				log.severe(caught.getMessage());

			}

			public void onSuccess(Object result) {
				DTOLinks dtoLinks = (DTOLinks) result;
				SCL.getCurrSecCanvas().getDtoCanvas().setDtoLinks(dtoLinks);
				linkPanel.updateLinks();
				SCL.getCanvasScreen().draw();
			}
		};
		ServiceUtilities.getEquipService().updateLinkNodes(SCL.getCurrSecCanvas().getDtoCanvas().getDtoLinks(), callback);
		
	}

	public CanvasPanel getCanvasPanel() {
		return canvasPanel;
		
	}

	

}