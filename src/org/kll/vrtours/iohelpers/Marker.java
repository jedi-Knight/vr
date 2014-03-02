package org.kll.vrtours.iohelpers;

import processing.core.PApplet;
import processing.data.XML;

public class Marker {

	XML xml;
	PApplet p;
	String xmlPath;
	Marker(PApplet p, String xmlPath){
		this.p = p;
		this.xmlPath = xmlPath;
	}
	
	
	public XML[] getMarkerLocationArray(){
		return p.loadXML(xmlPath).getChildren();
	}
	
}
