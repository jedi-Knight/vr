package org.kll.ktmheritage;

import java.util.*;

import processing.core.*;
import processing.data.XML;

/**
 * @author jedi-Knight
 *
 */
public class GetCentroid{      
	//TODO find out if there's a better way to make use of ArrayList, like, creating an ArrayList of Array (can it store multiple data types?? string, int, float etc in the same array??), than passing each Heritage Site information as an array of Strings..is it a better way to do it?? is there another better way??
	
	private XML OSMDataArg;
	private PApplet p1;
	
	GetCentroid(XML OSMData, PApplet p) {                  
		//TODO PApplet parameter being used for debugging purposes only..eliminate on final build.

		OSMDataArg = OSMData;
		p1 = p;
		
	}
	
			
public ArrayList<String[]> getCentroid (){
	
	XML IconXMLNode;
	String[] returnedCentroid ={};
	ArrayList<String[]> storedCentroidArray = new ArrayList();
	storedCentroidArray.add(0,new String[] {"null","null","null","null"});
	
	for (int i=1; i<OSMDataArg.getChildren().length; i+=2){
      IconXMLNode = OSMDataArg.getChild(i);
      //PApplet.println("["+i+"]\n"+OSMData.getChild(i));
      returnedCentroid = calcCentroid(IconXMLNode, OSMDataArg, p1);
      if(IconXMLNode.getName().equals("way")) storedCentroidArray.add(returnedCentroid);  //eliminate need to pass OSMData as parameter..instead target the global OSMData xml from within the getCentroid function..
      //p1.println("SIZE:: "+storedCentroidArray.get(storedCentroidArray.size()-1)[0]+","+storedCentroidArray.get(storedCentroidArray.size()-1)[1]+","+storedCentroidArray.get(storedCentroidArray.size()-1)[2]+","+storedCentroidArray.get(storedCentroidArray.size()-1)[3]);
      //p.println("yo ho returnedCentroid: " + returnedCentroid[0]+","+returnedCentroid[1]+","+returnedCentroid[2]+","+returnedCentroid[3]);  

	}
	//storedCentroidArray.add(returnedCentroid);
	//storedCentroidArray.add(returnedCentroid);
	//p1.println("yo ho SIZE "+storedCentroidArray.get(0)[1]);
	return storedCentroidArray;
}
	
private String[] calcCentroid(XML way, XML OSMData, PApplet p){
		//p.println("getCentroid function called..!!");
	  float coordLat=0, coordLon=0;
	  int totalnum=0;
	  String artefact = "", name = "";
	  //p.println(way.listChildren());
	  //println(way.toString());
	  for (int j=0; j<way.getChildren().length-1; j+=2){    //optimize this loop
	      
	  //println("yahasamma chalyo"+j);
	 // println(way.getChild(j+1).toString());
	          if(way.getChild(j+1).hasAttribute("ref")){
	            totalnum += 1;
	            //println(totalnum);
	            //p.println("yaha k bhayo yarr");
	              String nd_ref = way.getChild(j+1).getString("ref");  //use getInt()..number too large for getInt()..any other number type??
	                   //p.println(nd_ref);
	                   // if (boolean(nd_ref)){ NOT NEEDED
	                              for (int k=0; k<OSMData.getChildren().length-1; k+=2){   //add node elimination code in this loop..?? yes!! and also optimize this loop..use another XML parser library
	                                        //p.println("here: "+ OSMData.getChild(k+1).toString()); 
	                                        //println("does this node have attribute 'id??' "+OSMData.getChild(k+1).hasAttribute("id"));
	                                        if(OSMData.getChild(k+1).getName().equals("node")){
	                                          //println(OSMData.getChild(k+1).getName(), OSMData.getChild(k+1).getString("id"), nd_ref);
	                                          //println(OSMData.getChild(k+1).getString("id"));
	                                              if(nd_ref.equals(OSMData.getChild(k+1).getString("id"))){
	                                                coordLat+=OSMData.getChild(k+1).getFloat("lat");
	                                                coordLon+=OSMData.getChild(k+1).getFloat("lon");
	                                                //println(OSMData.getChild(k+1).getFloat("lat"),OSMData.getChild(k+1).getFloat("lon"));
	                                                 //p.println(coordLat+" , "+coordLon);
	                                                 
	                                              }
	                                             
	                                        }
	                              }
	                      
	            }
	            else if(way.getChild(j+1).hasAttribute("k")){
	                    String OSMKey = way.getChild(j+1).getString("k");
	                    //println(OSMKey);
	                    //println(OSMKey);
	                    
	                    if (OSMKey.equals("historic")) artefact = way.getChild(j+1).getString("v");
	                    else if (OSMKey.equals("name")) name= way.getChild(j+1).getString("v");
	                    
	                    //println(way.getChild(j+1).getString("v"));
	            }
	  }
	  
	 
	  coordLat /= totalnum;
	  coordLon /= totalnum;
	  
	  
	  
	  
	  
	  
	  //draw the Icons: ELIMINATE THIS CODE..WRITE CODE FOR DRAWING ICONS IN THE draw() CLASS OF THE SKETCH
	              //  if (boolean(artefact.length()) && boolean(name.length())){
	              //    Icon newIcon = new Icon(artefact, coordLat, coordLon, name);
	              //  }else if (boolean(artefact.length())){
	              //    Icon newIcon = new Icon(artefact, coordLat, coordLon);
	              //  }else if (boolean(name.length())){
	              //    Icon newIcon = new Icon(coordLat, coordLon, name);
	              //  }else{
	              //    Icon newIcon = new Icon(coordLat, coordLon);
	              //  }



	  String[] returnStringArray = {artefact, p.str(coordLat), p.str(coordLon), name};
	  
	  //String[] returnStringArray = {"artefact", "27.65633", "85.27950", "name"};
	  //p.println(returnStringArray[0] +" , "+ returnStringArray[1] +" , "+ returnStringArray[2] +" , "+ returnStringArray[3]);
	  return returnStringArray;
	    
	}

}
