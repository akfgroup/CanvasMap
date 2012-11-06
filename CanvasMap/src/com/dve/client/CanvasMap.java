package com.dve.client;

import java.util.Date;
import java.util.logging.Logger;

import com.dve.client._views.LoginView;
import com.dve.client.canvas.dialog.CanvasDialog;
import com.dve.client.canvas.dialog.CanvasLabel;
import com.dve.client.canvas.screen.CanvasScreen;
import com.dve.client.login.Login;
import com.dve.client.resource.CanvasResourcePanel;
import com.dve.client.selector.SC;
import com.dve.client.selector.SCL;
import com.dve.client.utilities.FormUtilities;
import com.dve.client.utilities.ServiceUtilities;
import com.dve.shared.dto.canvas.DTOCanvases;
import com.google.code.gwt.storage.client.Storage;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.VerticalPanel;


public class CanvasMap implements EntryPoint {
	
	CanvasMap canvasMap;
	
	CanvasDialog canvasDialog;
	CanvasBreadCrumb breadCrumb;
	
	VerticalPanel mainPanel = new VerticalPanel();
	
	TabBar tabBar = new TabBar();
	
	HorizontalPanel topPanel = new HorizontalPanel();
	VerticalPanel centerPanel = new VerticalPanel();
	HorizontalPanel btnPanel = new HorizontalPanel();
	
	LoginView loginView = new LoginView();
	
	Logger log = Logger.getLogger(CanvasMap.class.getName());
	

	public void onModuleLoad() {
		log.info("On ModuleLoad()");
		canvasMap = this;
		SC.setCanvasMap(canvasMap);
		SC.setContextName("/canvasmap");
		
		if(GWT.isProdMode()) {
			GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				@Override
				public void onUncaughtException(Throwable e) {
					log.severe("uncaugheException : " + buildStackTrace(e, "RuntimeException:\n"));

				}
			});
		}
		
		
		DeferredCommand.addCommand(new Command() { 
			public void execute() {
				dologin();
			}
		}); 
		
	}
	
	private void dologin() {
		checkWithServerIfSessionIsStillLegal();

	}
	
	public void checkWithServerIfSessionIsStillLegal() {
		if(Storage.getLocalStorage().getItem("sid") != null && 
				Storage.getLocalStorage().getItem("to") != null &&
				Long.parseLong(Storage.getLocalStorage().getItem("to"))>System.currentTimeMillis()) {

			log.info("FormsUI_v2.checkWithServerIfSessionIsStillLegal() sessionID = " + Storage.getLocalStorage().getItem("sid"));
			Login.checkRemoteSession(Storage.getLocalStorage().getItem("sid"));

		} else {
			log.info("FormsUI_v2.checkWithServerIfSessionIsStillLegal() sessionID = NULL!");
			displaylogin(true);
		}

	}
	
	public void displaylogin(boolean reset) {
		log.info("FormsUI_v2.displaylogin() 22");
		
		SC.setCurrentUser(null);

		RootLayoutPanel.get().clear();
		if(reset) {
			loginView.reset();
		}
		RootLayoutPanel.get().add(loginView);
		RootLayoutPanel.get().setWidgetTopHeight(loginView, 75, Unit.PX, 336, Unit.PX);
		RootLayoutPanel.get().setWidgetLeftWidth(loginView, 50, Unit.PX, 100, Unit.PCT);

	}
	
	private void startUp() {
		canvasDialog = new CanvasDialog();
		breadCrumb = new CanvasBreadCrumb();
		
		SCL.setCanvasDialog(canvasDialog);
		SCL.setBreadCrumb(breadCrumb);
		
		tabBar.addTab("Map");
		tabBar.addTab("Resource's");
		
		tabBar.getElement().getStyle().setBackgroundColor("transparent");
		tabBar.selectTab(0);
		
		topPanel.add(tabBar);
		topPanel.add(breadCrumb);
		
		mainPanel.setWidth("100%");
		mainPanel.add(topPanel);
		centerPanel.setWidth("100%");
		mainPanel.add(centerPanel);
		mainPanel.add(btnPanel);
		
		tabBar.addSelectionHandler(new SelectionHandler() {
			public void onSelection(SelectionEvent event) {
				if(SCL.getCurrPrimeCanvas()!=null) {
					if(tabBar.getSelectedTab()==0) {
						centerPanel.clear();
						centerPanel.add(SCL.getCurrPrimeCanvas().getCanvasScreen());

					} else if(tabBar.getSelectedTab()==1) {
						centerPanel.clear();
						centerPanel.add(SCL.getCurrPrimeCanvas().getResourcePanel());

					}
				}
			}
		});
		
		RootPanel.get().clear();
		RootPanel.get().add(mainPanel);
		
		SCL.getCanvasDialog().center();
		
		getRootCanvases();
		
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
	
	public void setAppVersion(String version) {
		log.info("Editor.dologin() setAppVersion()");
		Storage.getLocalStorage().setItem("version", version);
		
		SC.setSessionId(stripHTML(SC.getSessionId()));
		log.info("Editor.dologin() onSubmitComplete() sessionID = " + SC.getSessionId());
		if(!SC.getSessionId().equals("failed")) {
			final long DURATION = 1000 * FormUtilities.getSessionTimeOut() + System.currentTimeMillis();
			Storage.getLocalStorage().setItem("sid", SC.getSessionId());
			Storage.getLocalStorage().setItem("to", Long.toString(DURATION));
			checkWithServerIfSessionIsStillLegal();

		} 
		else {
			displaylogin(true);
			loginView.setError();

		}

		String clientVersion = Storage.getLocalStorage().getItem("version");

		if(clientVersion!=null && !clientVersion.equals(version)) {
			log.info("Client is out of date!");
			log.info("The current version = " + version + ", reloading!");
			Long DURATION = new Long("2592000000");
			Date expires = new Date(System.currentTimeMillis() + DURATION);
			Storage.getLocalStorage().setItem("version", version);
			Window.Location.reload();
			checkWithServerIfSessionIsStillLegal();

		}


	}
	
	public void setCanvasScreen(CanvasScreen canvasScreen) {
		centerPanel.clear();
		centerPanel.add(canvasScreen);
		
	}
	
	public void setResourcePanel(CanvasResourcePanel resourcePanel) {
		centerPanel.clear();
		centerPanel.add(resourcePanel);
		
	}
	
	public void getRootCanvases() {
		AsyncCallback callback = new AsyncCallback() {
			public void onFailure(Throwable caught) {
				log.severe(caught.getMessage());

			}

			public void onSuccess(Object result) {
				log.info("here");
				centerPanel.clear();
				CanvasLabel rootLabel = new CanvasLabel(null);
				SCL.setRootLabel(rootLabel);
				
				SCL.setCurrPrimeCanvas(null);
				SCL.setCurrSecCanvas(null);
				
				rootLabel.setDtoCanvases((DTOCanvases) result);
				
				SCL.getBreadCrumb().updateRoot();
				SCL.getCanvasDialog().updateRootCanvas();
	
			}
		};

		ServiceUtilities.getEquipService().getRootCanvases(callback);

	}


	public void display() {
		new Timer(){
			public void run() {
				startUp();
			}	
		}.schedule(50);
		
	}
	
	private String stripHTML(String sessionID) {
		return sessionID.replaceAll("\\<[^>]*>","");
	}
	

}
