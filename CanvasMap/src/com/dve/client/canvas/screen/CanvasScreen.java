package com.dve.client.canvas.screen;

import gwt.g2d.client.graphics.ImageLoader;
import gwt.g2d.client.graphics.ImageLoader.CallBack;

import java.util.logging.Logger;

import com.dve.client.canvas.dialog.CanvasLabel;
import com.dve.client.link.LinkShape;
import com.dve.client.selector.SCL;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
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
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class CanvasScreen extends Composite {

	CanvasScreen canvasScreen;
	CanvasLabel canvasLabel;
	
	CanvasLabel currLabel;
	CanvasLabel prevLabel;

	ScrollPanel scrollPanel = new ScrollPanel();
	
	Canvas canvas0;
	Canvas canvas1;
	Context2d context0;
	public Context2d context1;

	boolean mouseDn;
	int mouseDnX, mouseDnY;

	public int spacing = 1;
	public double zoom = 1.0;
	
	double xscale, yscale;

	VerticalPanel vertPanel = new VerticalPanel();
	
	AbsolutePanel absolutePanel = new AbsolutePanel();
	VerticalPanel imgPanel = new VerticalPanel();
	ImageElement imageElement;
	
	int width, height;

	Logger log = Logger.getLogger(CanvasScreen.class.getName());


	public CanvasScreen(final CanvasLabel canvasLabel) {
		canvasScreen = this;
		this.canvasLabel = canvasLabel;

		canvas0 = Canvas.createIfSupported();
		canvas1 = Canvas.createIfSupported();
		context0 = canvas0.getContext2d();
		context1 = canvas1.getContext2d();

		canvas0.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		canvas0.getElement().getStyle().setBorderWidth(2, Unit.PX);
		canvas0.getElement().getStyle().setBorderColor("black");

		canvas1.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(!SCL.getCanvasDialog().isEdit()) {
					int x = event.getRelativeX(canvas0.getCanvasElement());
					int y = event.getRelativeY(canvas0.getCanvasElement());

					if(SCL.getCurrPrimeCanvas()!=null) {
						CanvasLabel temp = canvasLabel.contains(x, y);
						if(temp!=null) {
							SCL.getCanvasDialog().openCanvas(temp);
						}
					}
				}
			}
		});

		canvas1.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if(SCL.getCanvasDialog().isEdit()) {
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

				if(SCL.getCanvasDialog().isEdit()) {
					if(SCL.getCurrSecCanvas()!=null) {
						LinkShape linkShape = SCL.getCurrSecCanvas().getLinkShape();
						if(linkShape==null) {
							linkShape = new LinkShape();
							SCL.getCurrSecCanvas().setLinkShape(linkShape);
						}
						SCL.getCurrSecCanvas().getLinkShape().nodeDown(x,y);
					}

				} 
			}
		});

		canvas1.addMouseMoveHandler(new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent event) {
				if(SCL.getCanvasDialog().isEdit() && mouseDn && SCL.getCurrSecCanvas()!=null) {
					int x = roundIt(event.getRelativeX(canvas0.getCanvasElement()));
					int y = roundIt(event.getRelativeY(canvas0.getCanvasElement()));
					SCL.getCurrSecCanvas().getLinkShape().nodeMove(x,y);
				}
				if(!SCL.getCanvasDialog().isEdit() && mouseDn) {
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
				if(!SCL.getCanvasDialog().isEdit()) {
					int x = event.getRelativeX(canvas0.getCanvasElement());
					int y = event.getRelativeY(canvas0.getCanvasElement());
					
					if(SCL.getCurrPrimeCanvas()!=null) {
						prevLabel = currLabel;
						CanvasLabel temp = canvasLabel.contains(x, y);
						if(temp!=null && temp!=currLabel) {
							currLabel = temp;
							currLabel.highlighLink();
						} else if(temp==null && currLabel!=null) {
							currLabel.unhighlight();
							currLabel=null;
						}
						if(prevLabel!=null && prevLabel!=currLabel) {
							prevLabel.unhighlightLink();
							prevLabel=null;
						}
					} 
				}
			}
		});


		canvas1.addMouseUpHandler(new MouseUpHandler() {
			public void onMouseUp(MouseUpEvent event) {
				mouseDn = false;
				if(SCL.getCanvasDialog().isEdit()) {
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
				event.preventDefault();
				
				if(event.isNorth()) {
					if(zoom<10)
					zoom = zoom + .1;
				} else if(event.isSouth()) {
					if(zoom>=.2) {
						zoom = zoom - .1;
					}
				}
				
				xscale = (double)scrollPanel.getHorizontalScrollPosition()/(double)scrollPanel.getMaximumHorizontalScrollPosition();
				yscale = (double)scrollPanel.getVerticalScrollPosition()/(double)scrollPanel.getMaximumVerticalScrollPosition();
				
				updateImage();
			}

		});
		
		vertPanel.add(absolutePanel);
		vertPanel.add(imgPanel);
		imgPanel.setVisible(false);

		absolutePanel.add(canvas0,0,0);
		absolutePanel.add(canvas1,0,0);

		scrollPanel.setPixelSize(Window.getClientWidth()-(int)(Window.getClientWidth()*.05), Window.getClientHeight()-(int)(Window.getClientHeight()*.05));
		scrollPanel.setWidget(vertPanel);

		initWidget(scrollPanel);

	}

	public int roundIt(int x) {
//		x = x + (spacing/2); // add 5 (half of 10), x now equals 132
//		x = x / spacing; // divide by 10, yielding 13 (division of ints throws away the decimal part)
//		x = x * spacing; // multiply by 10, giving you 130

		return x;

	}

	public void draw() {
		Timer t = new Timer() {
			public void run() {
				SCL.getCurrPrimeCanvas().drawLinks();
			}
		};
		t.schedule(10);		

	}	

	public void clear() {
		context1.clearRect(0,0,canvas1.getCoordinateSpaceWidth(),canvas1.getCoordinateSpaceHeight());

	}

	public void updateImage() {
		log.info("UpdateImage");
		
		if(canvasLabel.getDtoCanvas().getImageId()!=-1) {
			
			SCL.getWaiting().show();
			if(imageElement==null) {
				
				Image image = new Image();
				image.setUrl("getImage?nimage=" + canvasLabel.getDtoCanvas().getImageId() + "." + canvasLabel.getDtoCanvas().getImageType());
				log.info("Url = " + image.getUrl());
				
				ImageLoader.loadImages(image.getUrl(), new CallBack() {
					public void onImagesLoaded(ImageElement[] imageElements) {
						log.info("processing imageElement");
						
						imageElement = imageElements[0];
						
//						imgPanel.getElement().appendChild(imageElement);
						
						udpateImage2();
						
					}
				});
				
				
			} else {
				udpateImage2();
				
			}
			
		} else {
			log.info("clearing");
			context0.clearRect(0,0,canvas0.getOffsetWidth(), canvas0.getOffsetHeight());
			context1.clearRect(0,0,canvas1.getCoordinateSpaceWidth(),canvas1.getCoordinateSpaceHeight());

		}
		String zoomLA = Double.toString(zoom);
		if(zoomLA.length()>4) {
			zoomLA = zoomLA.substring(0,3);
		}
		SCL.getCanvasDialog().getZoomLA().setText("Zoom = " + zoomLA);
	}
	
	private void udpateImage2() {

		int imgWidth = (int)((double)imageElement.getWidth());
		int imgHeight = (int)((double)imageElement.getHeight());

		width = (int)((double)imgWidth * zoom);
		height = (int)((double)imgHeight * zoom);

		absolutePanel.setPixelSize(width, height);

		canvas0.setPixelSize(width, height);
		canvas0.setCoordinateSpaceHeight(height);
		canvas0.setCoordinateSpaceWidth(width);

		canvas1.setPixelSize(width, height);
		canvas1.setCoordinateSpaceHeight(height);
		canvas1.setCoordinateSpaceWidth(width);

		context0 = canvas0.getContext2d();
		context0.drawImage(imageElement,0,0, width, height);
		
		int hpos = (int)((double)scrollPanel.getMaximumHorizontalScrollPosition()*xscale);
		int vpos = (int)((double)scrollPanel.getMaximumVerticalScrollPosition()*yscale);
		
		scrollPanel.setHorizontalScrollPosition(hpos);
		scrollPanel.setVerticalScrollPosition(vpos);

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				canvasLabel.drawLinks();
				SCL.getWaiting().close();

			}
		});


	}
	
	public CanvasLabel getCanvasLabel() {
		return canvasLabel;
	}


}
