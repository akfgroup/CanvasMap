package com.dve.client.selector;

import com.dve.client.canvas.CanvasPanel;
import com.dve.client.image.ImageDialog;
import com.dve.client.link.LinkDialog;

public class SCL {
	
	private static LinkDialog linkDialog;
	private static ImageDialog imageDialog;
	private static CanvasPanel canvasPanel;
	
	
	public static LinkDialog getLinkDialog() {
		return linkDialog;
	}
	public static void setLinkDialog(LinkDialog linkDialog) {
		SCL.linkDialog = linkDialog;
	}
	public static ImageDialog getImageDialog() {
		return imageDialog;
	}
	public static void setImageDialog(ImageDialog imageDialog) {
		SCL.imageDialog = imageDialog;
	}
	public static CanvasPanel getCanvasPanel() {
		return canvasPanel;
	}
	public static void setCanvasPanel(CanvasPanel canvasPanel) {
		SCL.canvasPanel = canvasPanel;
	}
	
	
}
