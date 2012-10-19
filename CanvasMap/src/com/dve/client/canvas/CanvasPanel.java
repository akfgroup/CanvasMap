package com.dve.client.canvas;

import java.util.logging.Logger;

import com.dve.client.LinkShape;
import com.dve.client.LinkShapeLabel;
import com.dve.client.link.LinkDialog;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.reveregroup.gwt.imagepreloader.ImageLoadEvent;
import com.reveregroup.gwt.imagepreloader.ImageLoadHandler;
import com.reveregroup.gwt.imagepreloader.ImagePreloader;

public class CanvasPanel extends Composite {
	
	CanvasPanel canvasPanel;
	LinkDialog linkShapeDialog;
	
	ScrollPanel scrollPanel = new ScrollPanel();
	
	Image image = new Image("flrpln.png");
	
	Canvas canvas0;
	Canvas canvas1;
	Context2d context0;
	public Context2d context1;
	
	LinkShape currShape;
	LinkShape prevShape;
	
	public boolean editMode = true;
	
	boolean mouseDn;
	int mouseDnX, mouseDnY;
	
	public int spacing = 10;
	public double zoom = 1.0;
	
	AbsolutePanel absolutePanel = new AbsolutePanel();
	
	int width, height;
	
	Logger log = Logger.getLogger(CanvasPanel.class.getName());
	

	public CanvasPanel() {
		canvasPanel = this;
		linkShapeDialog = new LinkDialog(canvasPanel);
		
		canvas0 = Canvas.createIfSupported();
		canvas1 = Canvas.createIfSupported();
		context0 = canvas0.getContext2d();
		context1 = canvas1.getContext2d();
		
		canvas0.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		canvas0.getElement().getStyle().setBorderWidth(2, Unit.PX);
		canvas0.getElement().getStyle().setBorderColor("black");
		
		canvas1.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if(editMode) {
					DOM.setStyleAttribute(scrollPanel.getElement(), "cursor", "auto");
					
				} else {
					DOM.setStyleAttribute(scrollPanel.getElement(), "cursor", "pointer");
					
				}
				
			}
			
		});
		
		canvas1.addMouseDownHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
				mouseDn = true;
				mouseDnX = event.getClientX();
				mouseDnY = event.getClientY();
				
				if(editMode) {
					int x = event.getRelativeX(canvas0.getCanvasElement());
					int y = event.getRelativeY(canvas0.getCanvasElement());

					if(currShape!=null) {
						currShape.nodeDown(x, y);
					}
				} else if(!editMode) {
					
					
				}
			}
		});
		
		canvas1.addMouseMoveHandler(new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent event) {
				if(editMode && mouseDn && currShape!=null) {
					int x = roundIt(event.getRelativeX(canvas0.getCanvasElement()));
					int y = roundIt(event.getRelativeY(canvas0.getCanvasElement()));
					currShape.nodeMove(x,y);
				}
				if(!editMode && mouseDn) {
					if(event.getClientX()<mouseDnX) {
						scrollPanel.setHorizontalScrollPosition(scrollPanel.getHorizontalScrollPosition()+(mouseDnX-event.getClientX()));
					} else {
						scrollPanel.setHorizontalScrollPosition(scrollPanel.getHorizontalScrollPosition()-(event.getClientX()-mouseDnX));
					}
					if(event.getClientY()>mouseDnY) {
						scrollPanel.setVerticalScrollPosition(scrollPanel.getVerticalScrollPosition()+(mouseDnY-event.getClientY()));
					} else {
						scrollPanel.setVerticalScrollPosition(scrollPanel.getVerticalScrollPosition()-(event.getClientY()-mouseDnY));
					}
					mouseDnX = event.getClientX();
					mouseDnY = event.getClientY();
				}
			}
		});
		
		
		canvas1.addMouseUpHandler(new MouseUpHandler() {
			public void onMouseUp(MouseUpEvent event) {
				mouseDn = false;
				if(editMode) {
					int x = roundIt(event.getRelativeX(canvas1.getCanvasElement()));
					int y = roundIt(event.getRelativeY(canvas1.getCanvasElement()));
					
					if(currShape!=null) {
						currShape.nodeUp(x,y);
						
					}
				}
			}
		});
		
		canvas1.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				int x = event.getRelativeX(canvas1.getCanvasElement());
				int y = event.getRelativeY(canvas1.getCanvasElement());
				
				if(!editMode) {
					for(int i=0; i<linkShapeDialog.flexTable.getRowCount(); i++) {
						LinkShapeLabel linkShapeLabel = (LinkShapeLabel) linkShapeDialog.flexTable.getWidget(i, 0);
		        		if(linkShapeLabel.linkShape.contains(x, y)) {
		        			Window.alert(linkShapeLabel.label.getText());
		        		}
		        		
		        	}
					
				}
			}	
		});
		
		canvas1.addMouseWheelHandler(new MouseWheelHandler() {
			public void onMouseWheel(MouseWheelEvent event) {
				log.info("mouse wheel");
				if(!editMode) {
					int x = event.getRelativeX(canvas1.getElement());
					int y = event.getRelativeY(canvas1.getElement());
					
					if(event.isNorth()) {
						zoom = zoom + .1;
						x = x + (int)((double)x*.1);
						y = y + (int)((double)y*.1);
					} else if(event.isSouth()) {
						zoom = zoom - .1;
						x = x - (int)((double)x*.1);
						y = y - (int)((double)y*.1);
					}
					
					if(x>scrollPanel.getOffsetWidth()) {
						x = x - (scrollPanel.getOffsetWidth()/2);
					} else {
						x=0;
					}
					
					if(y>scrollPanel.getOffsetHeight()) {
						y = y - (scrollPanel.getOffsetHeight()/2);
					} else {
						y=0;
					}
				
					clear();
					
					int twidth = (int)((double)width*zoom);
			    	int theight = (int)((double)height*zoom);
			    	
			    	log.info("Width = " + twidth);
			    	log.info("Height = " + theight);
			    	
			    	absolutePanel.setPixelSize(twidth, theight);
			    	
			    	canvas0.setPixelSize(twidth, theight);
					canvas0.setCoordinateSpaceHeight(theight);
					canvas0.setCoordinateSpaceWidth(twidth);
					
					canvas1.setPixelSize(twidth, theight);
					canvas1.setCoordinateSpaceHeight(theight);
					canvas1.setCoordinateSpaceWidth(twidth);
					
					context0.drawImage(ImageElement.as(image.getElement()),0,0, twidth, theight);
					
					draw();
					
					scrollPanel.setHorizontalScrollPosition(x);
					scrollPanel.setVerticalScrollPosition(y);
					
					if(currShape!=null) {
						currShape.draw();
					}
				
				}
			}
		});
		
		absolutePanel.add(canvas0,0,0);
		absolutePanel.add(canvas1,0,0);
		
		ImagePreloader.load(image.getUrl(), new ImageLoadHandler() {
		    public void imageLoaded(ImageLoadEvent event) {
		        if (event.isLoadFailed()) {log.severe("Load Failed!");}
		        else{
		        	width = (int)((double)event.getDimensions().getWidth() * zoom);
		        	height = (int)((double)event.getDimensions().getHeight() * zoom);
		        	
		        	absolutePanel.setPixelSize(width, height);
		        	
		        	canvas0.setPixelSize(width, height);
		    		canvas0.setCoordinateSpaceHeight(height);
		    		canvas0.setCoordinateSpaceWidth(width);
		    		
		    		canvas1.setPixelSize(width, height);
		    		canvas1.setCoordinateSpaceHeight(height);
		    		canvas1.setCoordinateSpaceWidth(width);
		    		
		    		context0.drawImage(ImageElement.as(image.getElement()),0,0, width, height);
		        	
		        }
		           
		    }
		});
		
		scrollPanel.setPixelSize(Window.getClientWidth()-50, Window.getClientHeight()-100);
		scrollPanel.setWidget(absolutePanel);
		
		linkShapeDialog.update(0, 0);
		linkShapeDialog.show();
		
		clear();
		draw();
		
		initWidget(scrollPanel);
		
	}
	
	public void setCurrShape(LinkShape linkShape) {
		prevShape = currShape;
		if(prevShape!=null) {
			prevShape.unhighlight();
		}
		currShape = linkShape;
		if(currShape!=null) {
			currShape.highlight();
		}
		
	}
	
	public void addLink() {
		LinkShape linkShape = new LinkShape(canvasPanel, scrollPanel.getHorizontalScrollPosition()+(scrollPanel.getOffsetWidth()/2), scrollPanel.getVerticalScrollPosition()+(scrollPanel.getOffsetHeight()/2));
		linkShapeDialog.addLinkShape(linkShape);
		
	}
	
	public int roundIt(int x) {
		x = x + (spacing/2); // add 5 (half of 10), x now equals 132
		x = x / spacing; // divide by 10, yielding 13 (division of ints throws away the decimal part)
		x = x * spacing; // multiply by 10, giving you 130
		
		return x;

	}
	
	public void draw() {
    	for(int i=0; i<linkShapeDialog.flexTable.getRowCount(); i++) {
    		LinkShape linkShape = ((LinkShapeLabel) linkShapeDialog.flexTable.getWidget(i, 0)).linkShape;
    		linkShape.draw();
    		
    	}
	}	
	
	public void clear() {
		context1.clearRect(0,0,canvas1.getCoordinateSpaceWidth(),canvas1.getCoordinateSpaceHeight());
//		currShape = null;
//		shapes.clear();
		
	}
	
	private void updateScrollPanel(double x, double y) {
		
		log.info("horizPos = " + x);
		log.info("vertPos = " + y);
		
		log.info("maxHoriz = " + scrollPanel.getMaximumHorizontalScrollPosition());
		log.info("maxVert = " + scrollPanel.getMaximumVerticalScrollPosition());
		log.info("canvasHoriz = " + canvas1.getCoordinateSpaceWidth());
		log.info("canvasVert = " + canvas1.getCoordinateSpaceHeight());
		
		int hPos = (int)(x*scrollPanel.getMaximumHorizontalScrollPosition());
		int vPos = (int)(y*scrollPanel.getMaximumVerticalScrollPosition());
		
		log.info("set Horiz = " + hPos);
		log.info("Set Vert = " + vPos);
		
		scrollPanel.setHorizontalScrollPosition(hPos);
		scrollPanel.setVerticalScrollPosition(vPos);
		
	}

}
