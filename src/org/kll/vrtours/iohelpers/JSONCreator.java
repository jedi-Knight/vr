package org.kll.vrtours.iohelpers;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;
import processing.data.XML;
import org.json.JSONObject;

import android.os.Environment;

public class JSONCreator {
	
	//TODO: manipulate XML directly instead of converting to ArrayList<XML> first..!!
	
	
	
	String xapi;
	//float lat, lon;
	PApplet p;
	
	
	public JSONCreator (PApplet p, String xapi){
		this.p=p;
		this.xapi = xapi; 
		
		
		
	}
	
	
	public ArrayList<XML> loadMarkerData(){
		return parseOSMData();
		            //check for null return on call..
	}
	
	
	public XML loadXML(){
		//return p.loadXML(xapi);  TODO: use this line instead of the following line:
//		if(p.parseBoolean(1)){
//			XML xml = p.loadXML(xapi);
//			//xml.removeChild(xml.getChild("?xml"));
//			for(int i=0; i<5; i++){
//				p.println("testNode: "+i+": "+xml.getChild(i).getName());
//				boolean t = (xml.getChild(i).getName()=="#text");
//				p.println("testNode: "+i+": null?? "+t);
//			}
//			return xml;
//		}
		//return removeOSMMetaData(p.loadXML(xapi));
		XML xml = p.loadXML(xapi);
		
		
		
		return p.parseXML(xml.format(-1));
		
	}
	
	public ArrayList<XML> parseOSMData(){
		
		//JSONObject json = null;
		
		XML site = loadXML();
		
		ArrayList<XML> markerList = new ArrayList<XML>();
		//for(int i=0; i<3; i++)
		//System.out.println("xmlSites: "+site.getChild(site.getChildCount()-1).getChild(i));
		
//		for(int i=0; i<3; i++)
//			p.println("JSONCreator: parseOSMData: xapi: notCleared: "+site.getChild(i).getName());
		
//		for(int i=0; i<site.getChildren("relation").length; i++)
//			for(int j=0; j<site.getChildren("relation")[i].getChildCount(); j++)
				//System.out.println("JSONCreator.parseOSMData(): \n"+site.getChildren("relation")[i].getChild(j).getName());
		
		if(!site.getName().equals("null")){
		
				//XML[] siteArray = site.getChildren("relation");
				ArrayList<XML> siteList = new ArrayList<XML>();
				try{
					siteList = xmlArrayToArrayList(site.getChildren("relation"));
					
				}catch(Exception e){
					System.out.println("JSONCREATOR.parseOSMData(): error!!");//p.println(site.toString());
				}
				
				System.out.println("siteList item:");
				System.out.println("siteList item: "+siteList.get(0));
				
				
				
				
				
				/**create an ArrayList of nodes with information in them (other than location information)**/
				
				ArrayList<XML> siteNodeList = new ArrayList<XML>();
				
				//ArrayList<XML> siteWayList = new ArrayList<XML>();
				
				//ArrayList<XML> siteRelationList = new ArrayList<XML>();
				
				
				//for(int i=0; i<10; i++) //System.out.println("null node??: "+site.getChild(i));
				
				for (int i=0; i<site.getChildCount(); i++){
					XML node = site.getChild(i);
					//System.out.println("parseXML: node added: "+node.toString());
					if(!node.getName().equals("node")) break;
					if(node.hasChildren()) siteNodeList.add(node);
				}
				/****/
		
		
		
		markerList = generateMarkerXMLList(siteNodeList, siteList);
		
		String dataToStore = "<osm>";
		for(int i=0; i<markerList.size(); i++){
			//System.out.println("markerList: "+markerList.get(i).format(-1));
			dataToStore+=(markerList.get(i).format(-1));
		}
		dataToStore+=("</osm>");
		
		byte[] b = dataToStore.getBytes();
		
		File osmData = new File(Environment.getExternalStorageDirectory()+"/vrtourdownloads", "osmdata");
		OutputStream o = p.createOutput(osmData);
		try{
			o.write(b);
			o.flush();
			o.close();
			//File toClear = new File("xapi.osm");
			byte[] c = (new String("<null/>")).getBytes();
			OutputStream o1 = p.createOutput(xapi);
			o1.write(c);
			o1.flush();
			o1.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		}else{
			site = p.loadXML(Environment.getExternalStorageDirectory()+"/vrtourdownloads/osmdata");
			XML[] markers = site.getChildren();
			System.out.println("osmdata xml size: "+markers.length);
			for(int i=0; i<markers.length; i++){
				markerList.add(markers[i]);
				System.out.println("parsing: "+markerList.get(i));
			}
		}
		
		
		
		
		
		
		
		
		return markerList;
		
	}
	
	/**create an ArrayList of xml (with relation tag type="Heritage Site") data for each heritage site**/
	public ArrayList<XML> xmlArrayToArrayList(XML[] xmlArray){
		//System.out.println("JSONCreator: xmlArrayToArrayList method started!!: "+xmlArray[0].toString());
		ArrayList<XML> returnArrayList = new ArrayList<XML>();
		for(int i=0; i<xmlArray.length; i++){
			//System.out.println("JSONCreator.xmlArrayToArrayList(): xmlArray["+i+"].getChildCount() = "+xmlArray[i].getChildCount());
			XML node = xmlArray[i].getChild(xmlArray[i].getChildCount()-1);
			System.out.println("JSONCreator.xmlArrayToArrayList(): "+node);
			
			if(node.getString("k").equals("type") && node.getString("v").equals("Heritage Site")){
				returnArrayList.add(xmlArray[i]);
				//System.out.println("node added: "+xmlArray[i].toString());
			}
		}
		//cleanSiteList(returnArrayList);
		return returnArrayList;
	}
	/****/
	
	
	
	
	/**create an ArrayList of XML nodes for each POI**/
	public ArrayList<XML> generateMarkerXMLList(ArrayList<XML> siteNodeList, ArrayList<XML> siteList){
		System.out.println("JSONCreator: generateMarkerXMLList(): started!! siteList.size(): "+siteList.size());
		ArrayList<XML> markerList =  new ArrayList<XML>();
		//boolean addAllNodesSwitch = true;
		for(int i=0; i<siteList.size(); i++){  //get ith heritage site relation (reach: relation xml)
			
			XML siteXML = siteList.get(i);
			for(int j=0; j<siteXML.getChildCount(); j++){  //get jth node within ith heritage site relation (reach: each xml node within relation xml)
				//if(!siteNodeList.get(0).hasAttribute("wayCentroid")){  //TODO: (redundant code?? just send the way centroid data in xml packets similar to node data..this check is instead required for relations rather than ways) check if input siteNodeList is derived from OSM ways: check if the input siteNodeList contains calculated centroid data for OSM ways (and is therefore a list of OSM way objects with location of their centroids)
				XML markerNode = siteXML.getChild(j);
				//System.out.println("JSONCreator.generateMarkerXMLList(): testing condition: !markerNode().getName().equals('member'): break;");
				if(!markerNode.getName().equals("member")) break;  //member definitions are towards the beginning of relation (xml structure), tags are towards the end..
				//System.out.println("JSONCreator.generateMarkerXMLList(): contiuing!!");
				if(!siteNodeList.get(0).getName().equals("relation")){	//TODO: add code to add relation data..
					
					//if(!markerNode.getName().equals("member")) break;  //member definitions are towards the beginning of relation (xml structure), tags are towards the end..
					
					if(markerNode.getString("type").equals("node")){
						
						
						/****TODO: REWRITE THIS SECTION****/
						for(int k=0; k<siteNodeList.size(); k++){
							XML siteNode = siteNodeList.get(k);
							
							
							
							if(siteNode.getString("id").equals(markerNode.getString("ref"))){
								XML markerXML = new XML("marker");
								//TODO: use ENUM and use try..catch for these:
								markerXML.setString("lat", siteNode.getString("lat"));
								markerXML.setString("lon", siteNode.getString("lon"));
								markerXML.setString("name", getChildNodeWithAttribute(siteNode, "k", "name").getString("v"));
								markerXML.setString("place", getChildNodeWithAttribute(siteXML, "k", "name").getString("v"));
								markerXML.setString("heritagesite", getChildNodeWithAttribute(siteXML, "k", "name:heritagesite").getString("v"));
								markerList.add(markerXML);
								siteNodeList.remove(k);
								System.out.println("k: "+k);
								k--;
								System.out.println("k: "+k);
								break;
							}
//							}else if(addAllNodesSwitch){
//								XML markerXML = new XML("marker");
//								markerXML.setString("lat", siteNode.getString("lat"));
//								markerXML.setString("lon", siteNode.getString("lon"));
//								markerXML.setString("name", getChildNodeWithAttribute(siteNode, "k", "name").getString("v"));
//								markerList.add(markerXML);
//								siteNodeList.remove(k);
//								System.out.println("k: "+k);
//								k--;
//								System.out.println("k: "+k);
//								addAllNodesSwitch = false;
//							}
							
						}
						/********/
					}
				}
			}
		}
		
		for(int i=0; i<siteNodeList.size(); i++){
			try{
			XML siteNode = siteNodeList.get(i);
			XML markerXML = new XML("marker");
			markerXML.setString("lat", siteNode.getString("lat"));
			markerXML.setString("lon", siteNode.getString("lon"));
			markerXML.setString("name", getChildNodeWithAttribute(siteNode, "k", "name").getString("v"));
			markerList.add(markerXML);
			}catch(Exception e){
				System.out.println("way-referenced node found!!");
			}
			
			//addAllNodesSwitch = false;
//			markerList.add(siteNodeList.get(i));
		}
		return markerList;
		
	}
	/****/
	
	
	
//	public Map<String, String> getElements(ArrayList<XML> siteList, String tag, String[] attributes){
//		
//		Map<String, String> getElements = new HashMap<String, String>();
//		XML[] xmlNodes;
//		
//		for(int i=0; i<siteList.size(); i++){
//			xmlNodes = siteList.get(i).getChildren(tag);
//			for(int j=0; j<xmlNodes.length; j++){
//				
//			}
//		}		
//		
//		return siteList;
//		
//	}
	
	
	
	
	public boolean hasAttributes(XML node, String[] attributes){
		for (int i=0; i<attributes.length; i++){
			if(!node.hasAttribute(attributes[i])) return false;
		}
		return true;
	}
	
	public XML getChildNodeWithAttribute(XML node, String k, String v){
		System.out.println("JSONCreator.getChildNodeWithAttribute()!! : "+node);
		System.out.println("JSONCreator.getChildNodeWithAttribute()!! : k="+k+", v="+v);
		for(int i=0; i<node.getChildCount(); i++){
			try{
				System.out.println("checking key value for node: "+node.getChild(i));
				if(node.getChild(i).getString(k).equals(v)) return node.getChild(i);
			}catch(Exception e){
				System.out.println(i+"'th node does not have attribute..trying next node");
			}
			//if(node.getString(k).equals(v)) return node;
		}
		//TODO: remove these lines:
		System.out.println("node with attribute not found..returning value of 'historic' tag!!");
		for(int i=0; i<node.getChildCount(); i++){
			try{
				System.out.println("checking key value for node: "+node.getChild(i));
				if(node.getChild(i).getString(k).equals("historic")) return node.getChild(i);
			}catch(Exception e){
				System.out.println(i+"'th node does not have attribute..trying next node");
			}
			//if(node.getString(k).equals(v)) return node;
		}
		
		System.out.println("node with attribute not found..RETURNING NULL!!");
		return null;
	}
	
	
	
	/****THE FOLLOWING CODE HAS BEEN MOVED TO GetOSM.java !!****/
	
	
//	/**Prepare OSM XML for use**/
//	
//	//remove metadata
//	public XML removeOSMMetaData(XML xml){
//		
//		for(int i=0; i<10; i++)
//			p.println("JSONCreator: xapi: notCleared: "+xml.getChild(i).getName());
//		
//		//xml = xmlClearWhiteSpace(xml); //clear whitespace
//		xml.removeChild(xml.getChild("note"));
//		xml.removeChild(xml.getChild("meta"));
//		
//		for(int i=0; i<10; i++)
//			p.println("JSONCreator: xapi: cleared: "+xml.getChild(i).getName());
//		
//		return xml;
//	}
//	
//	public XML xmlClearWhiteSpace(XML xml){
//	    
//	    for(int i=0; i<xml.getChildCount(); i++){
//	      //println("nodeName: "+xml.getChild(i).getName());
//	      XML node = xml.getChild(i);
//	      
//	      xml.removeChild(node);
//	      
//	      if(!node.getName().equals("#text")) xml.addChild(xmlClearWhiteSpace(node));
//	    }
//		
//		
//		
//	    return xml;
//	}
//	/****/
	
	/********/
	



}
