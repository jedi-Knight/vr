package org.kll.ktmheritage;

import java.util.HashMap;
import java.util.Map;

import processing.core.PImage;
import processing.data.XML;
import android.content.Intent;


import com.modestmaps.InteractiveMap;
import com.modestmaps.core.Point2f;
import com.modestmaps.geo.Location;


public class Icon{  
	  PImage imgIcon;
	  GetScreenLocationForCoordinate screenXY;
//	  Point2f screenXY;
	  String panoURL = null;
	  String name;
	  KTMVRTourApp_mob1 p;
	  
	  float lat;
	  float lon;
	  XML markerXML;
	  
	  InteractiveMap map;
	  
	  
	  Icon(XML markerXML, InteractiveMap map, KTMVRTourApp_mob1 p){
		  
		  this.markerXML = markerXML;
		  
		  this.p = p;
		  this.map = map;
		  
		  
		  
	  }
	  
	  public void drawIcon(){
		  
		  this.lat = markerXML.getFloat("lat");
		  this.lon = markerXML.getFloat("lon");
//		  screenXY = map.locationPoint(new Location(lat, lon));
		  screenXY = new GetScreenLocationForCoordinate(map, lat, lon, p);
		  
		  if(!(screenXY.x()>=0 && screenXY.y()>=0 && screenXY.x()<=p.width && screenXY.y()<=p.height)) return; //if site is outside screen, return
		  
			  
		  this.name = markerXML.getString("name");
	
		if(markerXML.hasAttribute("place")){
			imgIcon = p.loadImage("heritage.png");
		}else{
			imgIcon = p.loadImage("historic.png");
		}
	    p.image(imgIcon, screenXY.x(), screenXY.y());
	    //p.println("icon drawn");
	    
	    p.panoViewSwitch = loadPano();
	    
	 
	    }
	  
	  
public String panoView;
	  
	  public boolean loadPano(){
		  
	
		  
		  boolean mouseOver = p.mouseX>screenXY.x()-20 && p.mouseX< screenXY.x()+imgIcon.width-20 && p.mouseY>screenXY.y()-20 && p.mouseY< screenXY.y()+imgIcon.height-20;
		    if(mouseOver){
		    	//p.println("icon pressed!!");
		      if(p.mousePressed){
		    	  p.fill(255,255,255,150);
		    	  p.noStroke();
		    	  p.rect(screenXY.x(), screenXY.y(), imgIcon.width, imgIcon.height);
		    	  
		    	  
		    	  if(markerXML.hasAttribute("place")){
		    	  
		    	  
			    	  if(name!=null){
				    	  Intent i = new Intent(p, org.kll.vrtours.screens.DownloadScreen.class);
				    	  i.putExtra("markerXML", markerXML.format(-1));
				    	  p.startActivity(i);
				    	  return true;
			    	  }
		    	  
		    	  }

		      }
		    }
		    return false;
	  }
	  
}
