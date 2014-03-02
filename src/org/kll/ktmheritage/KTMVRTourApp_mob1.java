//TODO
//TODO
//TODO
//TODO



package org.kll.ktmheritage;

import java.util.ArrayList;

import ketai.ui.KetaiGesture;

import org.kll.vrtours.iohelpers.Constants;
import org.kll.vrtours.iohelpers.JSONCreator;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.XML;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.view.MotionEvent;

import com.modestmaps.InteractiveMap;
import com.modestmaps.geo.Location;
import com.modestmaps.providers.MaperitiveMBTilesProvider;

public class KTMVRTourApp_mob1 extends PApplet {

final String DEVURL = Constants.DEVURL;

InteractiveMap map;

boolean mapDrawnSwitch = false;

boolean panoViewSwitch = false;

String panoUrl = null;

KetaiGesture gesture;

PImage splash;

Location locationCenter;

int zoom;

MaperitiveMBTilesProvider tiles;

String dbPath;

JSONCreator data;

boolean slippyDragged = false;

float minLat = 27.65631f, maxLat = 27.74325f, minLon = 85.27948f, maxLon = 85.3557f;

GetOSM templeDataXML =  new GetOSM(DEVURL ,"historic", "*", minLon, minLat, maxLon, maxLat, this);  //TODO: eliminate the need to pass PApplet as a parameter
XML OSMData;


ArrayList<Icon> heritageIcon = new ArrayList<Icon>();
ArrayList<XML> heritageSites = new ArrayList<XML>();

public void setup() {
	
	println("resuming..??");
	

	splash = loadImage("splash.png");
	
	println("DevURL: " + DEVURL);

		
	dbPath = Environment.getExternalStorageDirectory()+"/vrtourdownloads/";
	
	println("DATABASE STORAGE PATH: "+dbPath);
	
	
	tiles = new MaperitiveMBTilesProvider(this, "kalkattekaiyyo", dbPath);
	locationCenter = new Location(27.6736f, 85.3249f);
	zoom = 16;
	map = new InteractiveMap(this, tiles);
	map.setCenterZoom(locationCenter, zoom);
  
  templeDataXML.getOSM();		
	  
  heritageSites = templeDataXML.loadOSMData();
  gesture = new KetaiGesture(this);
}

public void draw() {

	if(map==null){
		map = new InteractiveMap(this, tiles);
		map.setCenterZoom(locationCenter, zoom);
	}
	
	try{	
		map.draw();

 
  for (int i=0; i<heritageSites.size(); i++){
	  
    Icon heritageIconXY = new Icon(heritageSites.get(i), map, this);
    heritageIcon.add(heritageIconXY);
    heritageIcon.get(i).drawIcon();
  }
  
  Location MapBottomRight = map.pointLocation(width, height), MapTopLeft = map.pointLocation(0, 0);  //gets map coordintate (lat,lon) for current screen corners

  float MapLeftEdge = MapTopLeft.lon , MapTopEdge = MapTopLeft.lat , MapRightEdge = MapBottomRight.lon , MapBottomEdge = MapBottomRight.lat; //gets lat and lon for current view map edges 
  
  
  if(!slippyDragged){
    heritageIcon.clear();
    if(MapLeftEdge < minLon) {map.tx -= map.sc*0.1f*(minLon - MapLeftEdge)/map.sc;}
    if(MapBottomEdge < minLat) {map.ty += map.sc*0.1f*(minLat - MapBottomEdge)/map.sc;}
    if(MapRightEdge > maxLon) {map.tx -= map.sc*0.1f*(maxLon - MapRightEdge)/map.sc;}
    if(MapTopEdge > maxLat) {map.ty += map.sc*0.1f*(maxLat - MapTopEdge)/map.sc;}
  }


  
  locationCenter = map.pointLocation(width/2, height/2);
  zoom = map.getZoom();

  }catch(Exception e){
	  println("ERROR!!");
	  e.printStackTrace();
  }
  
}


public void mouseDragged() {
	heritageIcon.clear();
    slippyDragged = true;
    
    try{
    	map.mouseDragged(); 
    }catch(Exception e){
    	println("Wait till tile render completes!!");
    } 
    
    
    

}

public void mouseReleased(){
  slippyDragged = false;
  map=null;
}




    public void onPinch(float x, float y, float r){
    	
    	 if (r > 0 && map.sc <65537) {
    	    map.sc *= 1.05;
    	   
    	  }
    	  else if (r < 0 && map.sc >6000) {
    	    map.sc *= 1.0/1.05; 
    	    
    	  }
    }

    public boolean surfaceTouchEvent(MotionEvent event) {

    	try{  
        	
        	  super.surfaceTouchEvent(event);

        	  
        	  return gesture.surfaceTouchEvent(event);
        	}catch(Exception e){
        		println("Wait till sketch loads!!\n" + e.getMessage());
        		return false;
        	}
    }
    
    //doesnt fix the crash:
    @TargetApi(Build.VERSION_CODES.HONEYCOMB) public void onRestart(){
    	super.onRestart();
    	if(android.os.Build.VERSION.SDK_INT >=android.os.Build.VERSION_CODES.HONEYCOMB){
    		this.recreate();
    	}
    }
    
  

}
