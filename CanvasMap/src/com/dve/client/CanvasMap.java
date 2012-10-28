package com.dve.client;

import java.util.logging.Logger;

import com.dve.client.canvas.CanvasScreen;
import com.dve.client.canvas.dialog.CanvasDialog;
import com.dve.client.image.ImageDialog;
import com.dve.client.link.LinkDialog;
import com.dve.client.selector.SCL;
import com.dve.shared.service.my.MyService;
import com.dve.shared.service.my.MyServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;


public class CanvasMap implements EntryPoint {
	
	CanvasMap canvasMap;
	
	CanvasScreen canvasScreen = new CanvasScreen();
	CanvasDialog canvasDialog = new CanvasDialog();
//	LinkDialog linkDialog = new LinkDialog(canvasPanel);
//	ImageDialog imageDialog = new ImageDialog();
	
	HorizontalPanel btnPanel = new HorizontalPanel();
	
//	Button clearBtn = new Button("Clear");
//	Button addBtn = new Button("Add Link");
//	Button testBtn = new Button("Test");
	
	Logger log = Logger.getLogger(CanvasMap.class.getName());
	

	public void onModuleLoad() {
		log.info("On ModuleLoad()");
		
		if(GWT.isProdMode()) {
			GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				@Override
				public void onUncaughtException(Throwable e) {
					log.severe("uncaugheException : " + buildStackTrace(e, "RuntimeException:\n"));

				}
			});
		}

		startUp();
		
	}
	
	private void startUp() {
		canvasMap = this;
		
		SCL.setCanvasDialog(canvasDialog);
//		SCL.setLinkDialog(linkDialog);
//		SCL.setImageDialog(imageDialog);
		SCL.setCanvasScreen(canvasScreen);
		
//		clearBtn.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				canvasPanel.clear();
//				
//			}
//
//		});
//		
//		addBtn.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
////				canvasPanel.addLink();
//				
//			}
//		});
//		
//		
//		testBtn.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				MyServiceAsync myService = (MyServiceAsync) GWT.create(MyService.class);
//				ServiceDefTarget endpoint = (ServiceDefTarget) myService;
//				String moduleRelativeURL = GWT.getModuleBaseURL() + "db";
//				endpoint.setServiceEntryPoint(moduleRelativeURL);
//
//				AsyncCallback callback = new AsyncCallback() {
//
//					public void onFailure(Throwable caught) {
//						log.severe(caught.getMessage());
//						caught.printStackTrace();
//
//					}
//
//					public void onSuccess(Object result) {
//						Window.alert((String)result);
//					
//					}
//				};
//
//				myService.checkAppVersion(callback);
//			}
//		});
//		
//		btnPanel.add(clearBtn);
//		btnPanel.add(addBtn);
//		btnPanel.add(testBtn);
		
		RootPanel.get().add(canvasScreen);
		
		SCL.getCanvasDialog().center();
//		SCL.getLinkDialog().show();
//		SCL.getImageDialog().center();
		
	}
	
	private String buildStackTrace(Throwable t, String log) {

		if (t != null) {
			log += t.getClass().toString();
			log += t.getMessage();
			//
			StackTraceElement[] stackTrace =
				t.getStackTrace();
			if (stackTrace != null) {
				StringBuffer trace = new
				StringBuffer();

				for (int i = 0; i < stackTrace.length;
				i++) {

					trace.append(stackTrace[i].getClassName() + "." +
							stackTrace[i].getMethodName() + "(" + stackTrace[i].getFileName() +
							":" + stackTrace[i].getLineNumber());
				}

				log += trace.toString();
			}
			//
			Throwable cause = t.getCause();
			if (cause != null && cause != t) {

				log += buildStackTrace(cause,
						"CausedBy:\n");

			}
		}
		return log;
	} 
	

	

}
