package org.kll.ktmheritage;



import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.kll.vrtours.iohelpers.JSONCreator;
import org.w3c.dom.Node;

import android.os.Environment;

import processing.core.*;
import processing.data.XML;

class GetOSM{
	  String API, OSMTag_key, OSMTag_value;
	  float left, top, right, bottom;
	  PApplet p1;
	  String DEVURL;
	  GetOSM(String DEVURL, String k, String v, float l, float b, float r, float t, PApplet p){
	    API = "http://www.overpass-api.de/api/xapi?*";
	    OSMTag_key = k;
	    OSMTag_value = v;
	    left = l;
	    bottom = b;
	    right = r;
	    top = t;
	    p1=p;
	    this.DEVURL = DEVURL;
	  }
	  public XML getOSM(){
	    //String URLString = API+"["+OSMTag_key+"="+OSMTag_value+"]["+"bbox="+left+","+bottom+","+right+","+top+"]";
	    String URLString = DEVURL + "osm.php";
	    System.out.println("URLString: "+URLString);
		  //p1.println(URLString);
//	    XML xapi;
	    
//	    	xapi = p1.loadXML(URLString);
	    XML xapi = null;
	    byte[] xapiBytes = null;
	    try{
	    	
//	    	xapi = p1.loadXML(URLString);
//	    	
//	    	while(xapi==null)System.out.println("waiting..");
	    	
	    	//URLString = "https://docs.google.com/uc?authuser=0&id=0BzVJmbAKs1tYOVNmZkhCczNxTWs&export=download";
	    	
	    	xapi = p1.loadXML(URLString);
	    	//while(xapi==null) System.out.println("waiting for osm data..");
	    	
	    	xapi = removeOSMMetaData(xapi);
	    	
	    	xapiBytes = xapi.toString().getBytes();
	    	
	    	
	    	
	    }catch(Exception e){
	    	//p1.println("OSM data not loaded..no internet connection!!");
	    	//p1.println(e.toString());
	    }
	    	
	    	if(xapiBytes != null){
	    		OutputStream localOSMData = p1.createOutput("xapi.osm");
	    		try{
	    			localOSMData.write(xapiBytes);
	    			localOSMData.flush();
	    			localOSMData.close();
	    		}catch(IOException e){
	    			//p1.println(e.toString());
	    		}
	    		
	    		
	    		
	    		
	    	}else{
	    		XML x = p1.parseXML("<osm attribute='qweqwe'></osm>");
	    		XML y = p1.parseXML("<node attribute='qewqe'></node>");
	    		
//	    		z.setPrefix("osm");
//	    		//z.
//	    		x.addChild(z.toString());
	    		x.setContent("<node/>");
	    		System.out.println(x);
	    		System.out.println("NULL RETURNED!!");
	    	}
	    	

	    	
		    	
		    	xapi = p1.loadXML("xapi.osm");
	    	
	    
	    return xapi;
	  }
	  
		/**Prepare OSM XML for use**/
//		
		//remove metadata
		public XML removeOSMMetaData(XML xml){
			
			for(int i=0; i<10; i++)
				p1.println("JSONCreator: xapi: notCleared: "+xml.getChild(i).getName());
			
			
			xml.removeChild(xml.getChild("note"));
			xml.removeChild(xml.getChild("meta"));
			
//			xml = xmlClearWhiteSpace(xml); TODO: clear whitespace
			
			for(int i=0; i<10; i++)
				p1.println("JSONCreator: xapi: cleared: "+xml.getChild(i).getName());
			
			return xml;
		}
//		
//		public XML xmlClearWhiteSpace(XML xml){
//		    int c = xml.getChildCount();
//		    //XML
//		    System.out.println("childNodes: "+xml.getChildCount());
//		    for(int i=0; i<c; i++){
//		      //println("nodeName: "+xml.getChild(i).getName());
//		      XML node = xml.getChild(0);
//		      
//		      xml.removeChild(node);
//		      
//		      System.out.println("getOSM nodeName: "+node.getName());
//		      if(!node.getName().equals("#text")) xml.addChild(xmlClearWhiteSpace(node));
//		    }
//			
//			System.out.println("returning xml: "+xml);
//			
//		    return xml;
//		}
		/****/
		
		
		
		public ArrayList<XML> loadOSMData(){
			JSONCreator data = new JSONCreator(p1, "xapi.osm");
			return data.loadMarkerData();
		}
	}
