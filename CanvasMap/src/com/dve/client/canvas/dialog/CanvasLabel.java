package com.dve.client.canvas.dialog;

import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import com.dve.client.link.LinkShape;
import com.dve.client.selector.SC;
import com.dve.client.selector.SCL;
import com.dve.client.utilities.ServiceUtilities;
import com.dve.shared.dto.canvas.DTOCanvas;
import com.dve.shared.dto.canvas.DTOCanvases;
import com.dve.shared.dto.canvas.DTOLink;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.reveregroup.gwt.imagepreloader.ImageLoadEvent;
import com.reveregroup.gwt.imagepreloader.ImageLoadHandler;
import com.reveregroup.gwt.imagepreloader.ImagePreloader;

public class CanvasLabel extends Composite {

	CanvasLabel canvasLabel;
	DTOCanvas dtoCanvas;

	DTOCanvases dtoCanvases;
	Vector<CanvasLabel> canvasLabels = new Vector();
	
	CanvasLabel parentCanvasLabel;

	Image image = new Image();
	boolean imgLoaded = false;
	
	int imgWidth = -1;
	int imgHeight = -1;
	
	LinkShape linkShape;

	Label label = new Label();
	
	Logger log = Logger.getLogger(CanvasLabel.class.getName());

	public CanvasLabel(DTOCanvas dtoCanvas) {
		this.dtoCanvas = dtoCanvas;
		this.canvasLabel = this;

		if(dtoCanvas!=null) {
			if(dtoCanvas.getDtoLinks()!=null) {
				linkShape = new LinkShape(SCL.getCanvasScreen());
				linkShape.setDtoLinkNodes(dtoCanvas.getDtoLinks());
			}
			if(dtoCanvas.getParentCanvas()!=null) {
				parentCanvasLabel = new CanvasLabel(dtoCanvas.getParentCanvas());
			}

			label.setText(dtoCanvas.getId() + " - " + dtoCanvas.getName());

			label.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					SCL.getCanvasDialog().setSecCurrCanvas(canvasLabel);

				}
			});

			label.addDoubleClickHandler(new DoubleClickHandler() {
				public void onDoubleClick(DoubleClickEvent event) {
					SCL.getCanvasDialog().openCanvas(canvasLabel);

				}
			});
			
			if(dtoCanvas.getImageId()!=-1) {
				image.setUrl("getImage?nimage=" + dtoCanvas.getImageId() + "." + dtoCanvas.getImageType());
				log.info("image url = " + image.getUrl());

				ImagePreloader.load(image.getUrl(), new ImageLoadHandler() {
					public void imageLoaded(ImageLoadEvent event) {
						if (event.isLoadFailed()) {log.severe("Load Failed!");}
						else{
							imgWidth = (int)((double)event.getDimensions().getWidth());
							imgHeight = (int)((double)event.getDimensions().getHeight());
							imgLoaded = true;

						}
					}
				});
			}
			
		}
		initWidget(label);

	}

	public DTOCanvas getDtoCanvas() {
		return dtoCanvas;
	}
	
	public CanvasLabel getParentCanvasLabel() {
		return parentCanvasLabel;
	}
	
	private void setParentCanvasLabel(CanvasLabel parentCanvasLabel) {
		this.parentCanvasLabel = parentCanvasLabel;
		
	}

	public LinkShape getLinkShape() {
		return linkShape;
	}

	public void setLinkShape(LinkShape linkShape) {
		this.linkShape = linkShape;

	}

	public Vector<CanvasLabel> getCanvasLabels() {
		return canvasLabels;
	}

	public void setCanvasLabels(Vector<CanvasLabel> canvasLabels) {
		this.canvasLabels = canvasLabels;
	}
	
	public Image getImage() {
		return image;
	}

	public boolean isLoaded() {
		return imgLoaded;
	}
	
	public int getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}

	public int getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
	}

	public void highlight() {
		label.getElement().getStyle().setBackgroundColor("yellow");

	}

	public void unhighlight() {
		label.getElement().getStyle().setBackgroundColor("transparent");

	}

	public CanvasLabel contains(double x, double y) {
		Iterator<CanvasLabel> it = canvasLabels.iterator();
		while(it.hasNext()) {
			CanvasLabel temp = it.next();
			if(temp.linkShape!=null && temp.linkShape.contains(x, y)) {
				return temp;
			}
		}
		return null;

	}

	public void setDtoCanvases(DTOCanvases dtoCanvases) {
		this.dtoCanvases = dtoCanvases;
		canvasLabels.clear();
		if(dtoCanvases!=null) {
			Iterator<DTOCanvas> it = dtoCanvases.getDTOCanvases().iterator();
			while(it.hasNext()) {
				CanvasLabel canvasLabel = new CanvasLabel(it.next());
				canvasLabel.setParentCanvasLabel(this);
				this.canvasLabels.add(canvasLabel);


			}
		} if(dtoCanvas!=null) {
			SCL.getCanvasDialog().updatePrimeCanvas();
		} else {
			SCL.getCanvasDialog().updateRootCanvas();
			SCL.getBreadCrumb().updateRoot();
		}

	}

	public void drawLinks() {
		if(canvasLabels!=null) {
			Iterator<CanvasLabel> it = canvasLabels.iterator();
			while(it.hasNext()) {
				CanvasLabel canvasLabel = it.next();
				if(canvasLabel.getLinkShape()!=null) {
					canvasLabel.getLinkShape().draw();
				}
			}
		}
	}

	public void loadImage() {
		image.setUrl("getImage?nimage=" + dtoCanvas.getImageId() + "." + dtoCanvas.getImageType());

		ImagePreloader.load(image.getUrl(), new ImageLoadHandler() {
			public void imageLoaded(ImageLoadEvent event) {
				if (event.isLoadFailed()) {log.severe("Load Failed!");}
				else{
					imgWidth = (int)((double)event.getDimensions().getWidth());
					imgHeight = (int)((double)event.getDimensions().getHeight());
					imgLoaded = true;
					SCL.getCanvasScreen().updateImage();
				}
			}
		});
		
	}

	public void updateResource() {
		AsyncCallback callback = new AsyncCallback() {
			public void onFailure(Throwable caught) {
				log.severe(caught.getMessage());

			}

			public void onSuccess(Object result) {
				DTOCanvas dtoCanvas = (DTOCanvas) result;
				SCL.getCanvasResourcePanel().updateResourcePanel();
				
			}
		};
		ServiceUtilities.getEquipService().updateCanvas(SCL.getCurrPrimeCanvas().getDtoCanvas(), callback);

	}

}
