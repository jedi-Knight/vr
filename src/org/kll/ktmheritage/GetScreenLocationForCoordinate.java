package org.kll.ktmheritage;

import processing.core.PApplet;

import com.modestmaps.InteractiveMap;
import com.modestmaps.geo.Location;

//TODO: eliminate class and instead use InateractiveMap.locationPoint() method in modestmaps library

public class GetScreenLocationForCoordinate {    

		  
		  float ScrnX, ScrnY;
		  
		  Location ScreenTopLeft, ScreenBottomRight;
		  
		  GetScreenLocationForCoordinate (InteractiveMap map, float N, float E, PApplet p){  //constructor
			ScreenTopLeft = map.pointLocation(0,0);
		    ScreenBottomRight = map.pointLocation(p.width, p.height);
		    ScrnX = p.width * (E - ScreenTopLeft.lon)/(ScreenBottomRight.lon - ScreenTopLeft.lon);
		    ScrnY = p.height * (N - ScreenTopLeft.lat)/(ScreenBottomRight.lat - ScreenTopLeft.lat);
		  }
		  
		  public float x(){
		    return ScrnX;
		  }
		  public float y(){
		    return ScrnY;
		  }
		} 

