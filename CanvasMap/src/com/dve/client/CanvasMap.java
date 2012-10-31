package com.dve.client;

import java.util.logging.Logger;

import com.dve.client.canvas.CanvasScreen;
import com.dve.client.canvas.dialog.CanvasDialog;
import com.dve.client.selector.SC;
import com.dve.client.selector.SCL;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.VerticalPanel;


public class CanvasMap implements EntryPoint {
	
	CanvasMap canvasMap;
	
	CanvasScreen canvasScreen = new CanvasScreen();
	CanvasDialog canvasDialog = new CanvasDialog();
	
	VerticalPanel mainPanel = new VerticalPanel();
	
	TabBar tabBar = new TabBar();
	CanvasBreadCrumb breadCrumb = new CanvasBreadCrumb();
	
	HorizontalPanel topPanel = new HorizontalPanel();
	VerticalPanel centerPanel = new VerticalPanel();
	HorizontalPanel btnPanel = new HorizontalPanel();
	
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
			SC.setContextName("/canvasmap");
		}

		startUp();
		
	}
	
	private void startUp() {
		canvasMap = this;
		
		SCL.setCanvasDialog(canvasDialog);
		SCL.setCanvasScreen(canvasScreen);
		SCL.setBreadCrumb(breadCrumb);
		
		tabBar.addTab("Map");
		tabBar.addTab("Resource's");
		
		tabBar.getElement().getStyle().setBackgroundColor("transparent");
		tabBar.selectTab(0);
		
		topPanel.add(tabBar);
		topPanel.add(breadCrumb);
		
		mainPanel.add(topPanel);
		mainPanel.add(centerPanel);
		mainPanel.add(btnPanel);
		
		centerPanel.add(canvasScreen);
		
		RootPanel.get().add(mainPanel);
		
		SCL.getCanvasDialog().center();
		
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
