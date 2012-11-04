package com.dve.client.canvas.screen;

import java.util.logging.Logger;

import com.dve.client.canvas.dialog.CanvasLabel;
import com.dve.client.link.LinkShape;
import com.dve.client.selector.SCL;
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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;


public class CanvasScreen extends Composite {

	CanvasScreen canvasPanel;

	ScrollPanel scrollPanel = new ScrollPanel();

	Canvas canvas0;
	Canvas canvas1;
	Context2d context0;
	public Context2d context1;

	public boolean editMode = false;

	boolean mouseDn;
	int mouseDnX, mouseDnY;

	public int spacing = 1;
	public double zoom = 1.0;

	AbsolutePanel absolutePanel = new AbsolutePanel();

	int width, height;

	Logger log = Logger.getLogger(CanvasScreen.class.getName());


	public CanvasScreen() {
		canvasPanel = this;

		canvas0 = Canvas.createIfSupported();
		canvas1 = Canvas.createIfSupported();
		context0 = canvas0.getContext2d();
		context1 = canvas1.getContext2d();

		canvas0.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		canvas0.getElement().getStyle().setBorderWidth(2, Unit.PX);
		canvas0.getElement().getStyle().setBorderColor("black");

		canvas1.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(!editMode) {
					int x = event.getRelativeX(canvas0.getCanvasElement());
					int y = event.getRelativeY(canvas0.getCanvasElement());

					if(SCL.getCurrPrimeCanvas()!=null) {
						CanvasLabel canvasLabel = SCL.getCurrPrimeCanvas().contains(x, y);
						if(canvasLabel!=null) {
							SCL.getCanvasDialog().openCanvas(canvasLabel);
						}
					}
				}
			}
		});

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
				
				int x = event.getRelativeX(canvas0.getCanvasElement());
				int y = event.getRelativeY(canvas0.getCanvasElement());

				if(editMode) {
					if(SCL.getCurrSecCanvas()!=null) {
						LinkShape linkShape = SCL.getCurrSecCanvas().getLinkShape();
						if(linkShape==null) {
							linkShape = new LinkShape(SCL.getCanvasScreen());
							SCL.getCurrSecCanvas().setLinkShape(linkShape);
						}
						SCL.getCurrSecCanvas().getLinkShape().nodeDown(x,y);
					}

				} 
			}
		});

		canvas1.addMouseMoveHandler(new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent event) {
				if(editMode && mouseDn && SCL.getCurrSecCanvas()!=null) {
					int x = roundIt(event.getRelativeX(canvas0.getCanvasElement()));
					int y = roundIt(event.getRelativeY(canvas0.getCanvasElement()));
					SCL.getCurrSecCanvas().getLinkShape().nodeMove(x,y);
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

					if(SCL.getCurrSecCanvas()!=null) {
						SCL.getCurrSecCanvas().getLinkShape().nodeUp(x,y);

					}
				}
			}
		});

		canvas1.addMouseWheelHandler(new MouseWheelHandler() {
			public void onMouseWheel(MouseWheelEvent event) {
				
				int x = event.getRelativeX(canvas1.getElement());
				int y = event.getRelativeY(canvas1.getElement());
				
				int osx = scrollPanel.getHorizontalScrollPosition();
				int osy = scrollPanel.getVerticalScrollPosition();
				
				int nsx = osx;
				int nsy = osy;
				
				if(event.isNorth()) {
					zoom = zoom + .1;
					nsx = nsx + (int)((double)nsx*.1) + (int)(Math.abs(x-Window.getClientWidth()/2)*.1);
					nsy = nsy + (int)((double)nsy*.1) + (int)(Math.abs(y-Window.getClientHeight()/2)*.1);
				} else if(event.isSouth()) {
					zoom = zoom - .1;
					nsx = nsx - (int)((double)nsx*.1) - (int)(Math.abs(x-Window.getClientWidth()/2)*.1);
					nsy = nsy - (int)((double)nsy*.1) - (int)(Math.abs(x-Window.getClientWidth()/2)*.1);
				}

				updateImage();
			}

		});

		absolutePanel.add(canvas0,0,0);
		absolutePanel.add(canvas1,0,0);

		scrollPanel.setPixelSize(Window.getClientWidth(), Window.getClientHeight());
		scrollPanel.setWidget(absolutePanel);

		initWidget(scrollPanel);

	}

	public int roundIt(int x) {
		x = x + (spacing/2); // add 5 (half of 10), x now equals 132
		x = x / spacing; // divide by 10, yielding 13 (division of ints throws away the decimal part)
		x = x * spacing; // multiply by 10, giving you 130

		return x;

	}

	public void draw() {
		if(SCL.getCurrPrimeCanvas()!=null) {
			Timer t = new Timer() {
				public void run() {
					SCL.getCurrPrimeCanvas().drawLinks();
				}
			};
			t.schedule(10);		
		}

	}	

	public void clear() {
		context1.clearRect(0,0,canvas1.getCoordinateSpaceWidth(),canvas1.getCoordinateSpaceHeight());

	}

	public void updateImage() {
		log.info("UpdateImage");
		if(SCL.getCurrPrimeCanvas()!=null && SCL.getCurrPrimeCanvas().getDtoCanvas().getImageId()!=-1) {

			SCL.getWaiting().show();
			
			width = (int)((double)SCL.getCurrPrimeCanvas().getImgWidth() * zoom);
			height = (int)((double)SCL.getCurrPrimeCanvas().getImgHeight() * zoom);

			absolutePanel.setPixelSize(width, height);

			canvas0.setPixelSize(width, height);
			canvas0.setCoordinateSpaceHeight(height);
			canvas0.setCoordinateSpaceWidth(width);

			canvas1.setPixelSize(width, height);
			canvas1.setCoordinateSpaceHeight(height);
			canvas1.setCoordinateSpaceWidth(width);

			context0 = canvas0.getContext2d();
			context0.drawImage(ImageElement.as(SCL.getCurrPrimeCanvas().getImage().getElement()),0,0, width, height);

			Timer t = new Timer(){
				@Override
				public void run() {
					SCL.getCurrPrimeCanvas().drawLinks();
					SCL.getWaiting().close();
				}
			};
			t.schedule(100);
			
		} else {
			log.info("clearing");
			context0.clearRect(0,0,canvas0.getOffsetWidth(), canvas0.getOffsetHeight());
			context1.clearRect(0,0,canvas1.getCoordinateSpaceWidth(),canvas1.getCoordinateSpaceHeight());

		}
		SCL.getCanvasDialog().getZoomLA().setText("Zoom = " + zoom);
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
