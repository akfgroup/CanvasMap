package com.dve.client;

import java.util.Date;
import java.util.logging.Logger;

import com.dve.client._views.LoginView;
import com.dve.client.canvas.dialog.CanvasDialog;
import com.dve.client.canvas.dialog.CanvasLabel;
import com.dve.client.canvas.screen.CanvasScreen;
import com.dve.client.login.Login;
import com.dve.client.managers.admin.AdminDialog;
import com.dve.client.managers.image.ImageManagerDialog;
import com.dve.client.resource.CanvasResourcePanel;
import com.dve.client.selector.SC;
import com.dve.client.selector.SCL;
import com.dve.client.utilities.FormUtilities;
import com.dve.client.utilities.ServiceUtilities;
import com.dve.equip.client.resources.BldgEqpResource;
import com.dve.equip.client.resources.BldgResource;
import com.dve.equip.client.resources.FlrResource;
import com.dve.equip.client.resources.OrgResource;
import com.dve.equip.client.resources.ResourcePanel;
import com.dve.equip.client.resources.RmResource;
import com.dve.shared.dto.building.DTOBuilding;
import com.dve.shared.dto.canvas.DTOCanvases;
import com.dve.shared.dto.org.DTOOrg;
import com.dve.shared.dto.org.DTOOrgs;
import com.dve.shared.dto.user.DTOUser;
import com.dve.shared.enums.ASSETS;
import com.google.code.gwt.storage.client.Storage;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
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
	
	FlexTable assetTable = new FlexTable();
	
	Label assetType1 = new Label("Asset Type");
	Label assetType2 = new Label();
	
	Label assetName1 = new Label("Asset Name");
	Label assetName2 = new Label();
	
	HorizontalPanel topPanel = new HorizontalPanel();
	VerticalPanel centerPanel = new VerticalPanel();
	HorizontalPanel btnPanel = new HorizontalPanel();
	
	LoginView loginView;
	
	boolean devMode = false;
	
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
				if(!devMode) {
					dologin();
				} else {
					DTOUser dtoUser = new DTOUser();
					dtoUser.setUserID(1);
					dtoUser.setUserName("dve");
					dtoUser.setFirstName("David");
					dtoUser.setLastName("Edelstein");
					dtoUser.setEmailAddress("dedelstein@akf-eng.com");
					dtoUser.setSandBoxProjId(23);
					dtoUser.setOrgId(1);
					SC.setCurrentUser(dtoUser);
					startUp();
					
				}
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

			log.info("CanvasMap.checkWithServerIfSessionIsStillLegal() sessionID = " + Storage.getLocalStorage().getItem("sid"));
			Login.checkRemoteSession(Storage.getLocalStorage().getItem("sid"));

		} else {
			log.info("CanvasMap.checkWithServerIfSessionIsStillLegal() sessionID = NULL!");
			displaylogin(true);
		}

	}
	
	public void displaylogin(boolean reset) {
		log.info("CanvasMap.displaylogin() 22");
		
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
		
		assetTable.setBorderWidth(1);
		assetTable.setWidget(0,0,assetType1);
		assetTable.setWidget(0,1,assetType2);
		assetTable.setWidget(0,2,assetName1);
		assetTable.setWidget(0,3,assetName2);
		
		assetTable.getFlexCellFormatter().setWidth(0,0,"100px");
		assetTable.getFlexCellFormatter().setWidth(0,2,"100px");
		
		topPanel.add(assetTable);
		
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
		
		Event.addNativePreviewHandler(new NativePreviewHandler() {
			public void onPreviewNativeEvent(NativePreviewEvent event) {
				if(event.getTypeInt()==Event.ONKEYDOWN) {
					if(event.getNativeEvent().getKeyCode()==17) {
						if(SCL.getCurrPrimeCanvas()!=null) {
							SCL.getCurrPrimeCanvas().showLinks();
						}
					}
				}
				if(event.getTypeInt()==Event.ONKEYUP) {
					if(event.getNativeEvent().getKeyCode()==17) {
						SCL.getCurrPrimeCanvas().hideLinks();
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
	
	public void updateAssetLabel(String assetType, String assetName) {
		assetType2.setText(assetType);
		assetName2.setText(assetName);
		
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
