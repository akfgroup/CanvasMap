package com.dve.client.selector;

import com.dve.client.canvas.CanvasScreen;
import com.dve.client.canvas.dialog.CanvasDialog;
import com.dve.client.canvas.dialog.CanvasLabel;
import com.dve.client.canvas.dialog.Waiting;
import com.dve.client.image.ImageDialog;
import com.dve.client.link.LinkDialog;
import com.dve.client.link.LinkLabel;
import com.dve.shared.dto.canvas.DTOCanvases;

public class SCL {
	
	private static CanvasDialog canvasDialog;
	private static CanvasScreen canvasScreen;
	
	private static CanvasLabel rootLabel;
	
	private static CanvasLabel currPrimeCanvas;
	private static CanvasLabel prevPrimeCanvas;
	
	private static CanvasLabel currSecCanvas;
	private static CanvasLabel prevSecCanvas;
	
	private static LinkLabel currLinkNode;
	private static LinkLabel prevLinkNode;
	
	private static Waiting waiting;
	
	
	public static CanvasDialog getCanvasDialog() {
		return canvasDialog;
	}
	public static void setCanvasDialog(CanvasDialog canvasDialog) {
		SCL.canvasDialog = canvasDialog;
	}
	public static CanvasScreen getCanvasScreen() {
		return canvasScreen;
	}
	public static void setCanvasScreen(CanvasScreen canvasScreen) {
		SCL.canvasScreen = canvasScreen;
	}
	public static CanvasLabel getRootLabel() {
		return rootLabel;
	}
	public static void setRootLabel(CanvasLabel rootLabel) {
		SCL.rootLabel = rootLabel;
	}
	public static CanvasLabel getCurrPrimeCanvas() {
		return currPrimeCanvas;
	}
	public static void setCurrPrimeCanvas(CanvasLabel currCanvas) {
		SCL.currPrimeCanvas = currCanvas;
	}
	public static CanvasLabel getPrevPrimeCanvas() {
		return prevPrimeCanvas;
	}
	public static void setPrevPrimeCanvas(CanvasLabel prevCanvas) {
		SCL.prevPrimeCanvas = prevCanvas;
	}
	public static CanvasLabel getCurrSecCanvas() {
		return currSecCanvas;
	}
	public static void setCurrSecCanvas(CanvasLabel currSecCanvas) {
		SCL.currSecCanvas = currSecCanvas;
	}
	public static CanvasLabel getPrevSecCanvas() {
		return prevSecCanvas;
	}
	public static void setPrevSecCanvas(CanvasLabel prevSecCanvas) {
		SCL.prevSecCanvas = prevSecCanvas;
	}
	public static LinkLabel getCurrLinkNode() {
		return currLinkNode;
	}
	public static void setCurrLinkNode(LinkLabel currLinkNode) {
		SCL.currLinkNode = currLinkNode;
	}
	public static LinkLabel getPrevLinkNode() {
		return prevLinkNode;
	}
	public static void setPrevLinkNode(LinkLabel prevLinkNode) {
		SCL.prevLinkNode = prevLinkNode;
	}
	public static Waiting getWaiting() {
		if(waiting==null) {
			waiting = new Waiting();
		}
		return waiting;
	}
	public static void setWaiting(Waiting waiting) {
		SCL.waiting = waiting;
	}
	
	
	
}
