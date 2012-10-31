package com.dve.client.canvas.dialog;

import gwt.awt.Polygon;

import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import com.dve.client.link.LinkShape;
import com.dve.client.selector.SC;
import com.dve.client.selector.SCL;
import com.dve.shared.dto.canvas.DTOCanvas;
import com.dve.shared.dto.canvas.DTOCanvases;
import com.dve.shared.dto.canvas.DTOLink;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.Window;
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
				image.setUrl(SC.getContextName() + "/getImage?nimage=" + dtoCanvas.getImageId() + "." + dtoCanvas.getImageType());

				ImagePreloader.load(image.getUrl(), new ImageLoadHandler() {
					public void imageLoaded(ImageLoadEvent event) {
						if (event.isLoadFailed()) {log.severe("Load Failed!");}
						else{
							imgWidth = (int)((double)event.getDimensions().getWidth());
							imgHeight = (int)((double)event.getDimensions().getHeight());

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

	public void setImage(Image image) {
		this.image = image;
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

	public void getLink(int x, int y) {
		if(contains(x,y)!=null) {
			Window.alert(x + ", " + y);
		}

	}

	public CanvasLabel contains(double x, double y) {

		DTOCanvas tempCanvas = null;
		if(dtoCanvases!=null) {
			for(int i=0; i<dtoCanvases.getDTOCanvases().size(); i++) {
				tempCanvas = dtoCanvases.getDTOCanvases().get(i);
				if(tempCanvas.getDtoLinks()!=null) {
					Polygon polygon = new Polygon();
					Iterator<DTOLink> it = tempCanvas.getDtoLinks().getDTOLinks().iterator();
					while(it.hasNext()) {
						DTOLink p = it.next();
						polygon.addPoint((int)((double)p.getX()*SCL.getCanvasScreen().zoom), (int)((double)p.getY()*SCL.getCanvasScreen().zoom));
					}
					if(polygon.contains(x,y)) {
						return canvasLabels.get(i);
					}
				} 

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
		image.setUrl(SC.getContextName() + "/getImage?nimage=" + dtoCanvas.getImageId() + "." + dtoCanvas.getImageType());

		ImagePreloader.load(image.getUrl(), new ImageLoadHandler() {
			public void imageLoaded(ImageLoadEvent event) {
				if (event.isLoadFailed()) {log.severe("Load Failed!");}
				else{
					imgWidth = (int)((double)event.getDimensions().getWidth());
					imgHeight = (int)((double)event.getDimensions().getHeight());
					SCL.getCanvasScreen().updateImage();
				}
			}
		});
		
	}

}
