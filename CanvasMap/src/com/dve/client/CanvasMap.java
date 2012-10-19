package com.dve.client;

import java.util.logging.Logger;

import com.dve.client.canvas.CanvasPanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;


public class CanvasMap implements EntryPoint {
	
	CanvasMap canvasMap;
	CanvasPanel canvasPanel = new CanvasPanel();
	
	HorizontalPanel btnPanel = new HorizontalPanel();
	
	Button clearBtn = new Button("Clear");
	Button addBtn = new Button("Add Link");
	Button modeBtn = new Button("Edit Mode");
	
	Logger log = Logger.getLogger(CanvasMap.class.getName());
	
	

	public void onModuleLoad() {
		log.info("On ModuleLoad()");

		startUp();
		
	}
	
	private void startUp() {
		canvasMap = this;
		
		clearBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				canvasPanel.clear();
				
			}

		});
		
		addBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				canvasPanel.addLink();
				
			}
		});
		
		modeBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(canvasPanel.editMode) {
					canvasPanel.editMode = false;
					modeBtn.setText("User Mode");
					return;
				} 
				if(!canvasPanel.editMode) {
					canvasPanel.editMode = true;
					modeBtn.setText("Edit Mode");
					return;
				}
				
			}
		});
		
		btnPanel.add(clearBtn);
		btnPanel.add(addBtn);
		btnPanel.add(modeBtn);
		
		RootPanel.get().add(canvasPanel);
		RootPanel.get().add(btnPanel);
		
	}
	

	

}
