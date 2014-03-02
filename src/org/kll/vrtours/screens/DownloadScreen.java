package org.kll.vrtours.screens;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.XML;

import org.kll.vrtours.geo.GeoCalc;
import org.kll.vrtours.iohelpers.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DownloadScreen extends PApplet implements Serializable {
	
	
	PApplet p = this;
	String url;
	String fileName = null;
	boolean bmgExists = false;
	DownloaderTask task = new DownloaderTask();
	XML markerXML;
	JSONObject json;
	String jsonDataFile;
	JSONObject flagNamesJSON;
	
	public void setup(){
		background(255);
		frameRate(1);
		println("setup");
		
		textSize(32*height/300);
		fill(0);
		
		
		println("getting URL from serializable");
		Intent i = getIntent();
		markerXML = parseXML((String) i.getSerializableExtra("markerXML"));

		url = markerXML.getString("place");
		
		
		
		println("downloading panorama..");
		
		
		
				task.execute();

		
	}
	
	public void draw(){
		text("Downloading Virtual Tour Data..", 10, height/2-16*height/300, width, 96);
		if (fileName!=null){

			
			try {
				json = json(fileName+"data");
				println(json.toString());
				println("flagNames: "+flagNamesJSON.toString());
				byte[] b = json.toString().replace("\\/", "/").getBytes();
				jsonDataFile = fileName.replace("/"+url.replace(" ", "_"), "/")+".jsondata";
				OutputStream o = createOutput(jsonDataFile);
				o.write(b);
				o.flush();
				o.close();
			}catch(JSONException e){
				println("json parse error!!");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				println("IO ERROR !!");
				e.printStackTrace();
			}
	
			
			Intent i = new Intent(this, org.kll.ktmheritagevrtour.PlayerActivity.class);
			
			i.putExtra("PanoramaScript", jsonDataFile);
			i.putExtra("flagNamesXML", flagNamesJSON.toString());
			startActivity(i);
		
			fileName = null;
		}
		
	}
	
	public void onPause(){
		super.onPause();
		finish();
	}
	
	
	public JSONObject json(String fileName) throws JSONException{
		
		println("DownloadScreen.json() started!! with parameter: fileName: "+fileName);
		
		JSONObject json = new JSONObject();
		XML osmXML = loadXML(Environment.getExternalStorageDirectory()+"/vrtourdownloads/osmdata");
		XML panoXML = loadXML(fileName);
		
		println("osmXML and panoXML loaded!!");
		
		flagNamesJSON = new JSONObject();
		
		json.put("urlBase", "file:///sdcard/vrtourdownloads");
		println("urlbase added");
		
		json.put("type", "spherical2");    //add method for other types..type data to be stored along with panopic on server..
		println("type added");
		
		json.put("sensorialRotation", false);
		println("sensorialRotation added");
		
		json.put("divisions", new JSONObject("{scrolling : 60}"));
		println("divisions added");
		
		json.put("scrolling", new JSONObject("{enabled : false}"));
		println("scrolling added");
		
		json.put("inertia", new JSONObject("{enabled : false, interval: 3}"));
		println("inertia added");
		
		json.put("accelerometer", new JSONObject("{enabled : false, interval : 0.033, sensitivity : 10.0, leftRightEnabled : true, upDownEnabled : false}"));
		println("accelerometer added");
		
		println("adding..image url: "+url.replace(" ", "_"));
		json.put("images", new JSONObject().put("image", url.replace(" ", "_")));
		println("images added..image url: "+url);
		
		json.put("camera", new JSONObject("{vlookat: 0, hlookat: 0, atvmin: -90, atvmax: 90, athmin: -180, athmax: 180, rotationSensitivity:100, fovSensitivity:50}"));
		println("camera added");

		println("json object initiated with base data!! now creating hotspot array..");
		
		JSONArray hotspots = new JSONArray();
		JSONArray namesArray = new JSONArray();
		int id = 0;
		GeoCalc geoCalc;
		
		for(int i=0; i<osmXML.getChildCount(); i++){
			println("osmXML.getChildCount: "+osmXML.getChildCount());
			XML node = osmXML.getChild(i);
			println(node);
			if(node.hasAttribute("place")){
				if(node.getString("place").equals(url)){
					JSONObject flag = new JSONObject();
					flag.put("id", ++id);
					geoCalc = new GeoCalc(panoXML.getFloat("lat"),panoXML.getFloat("lon"),node.getFloat("lat"),node.getFloat("lon"));
					
					flag.put("ath", geoCalc.hangle()-panoXML.getFloat("dir")+0.5);
					flag.put("atv", 0);
					
					flag.put("width", 0.08);
					flag.put("height", 0.08);
					flag.put("image","assets/hotspot.png");
					
					hotspots.put(flag);
					
					namesArray.put(node.getString("name"));
					
				}
			}
		}
		
		println("now adding hotspot array data to json..");
		json.put("hotspots", hotspots);
		
		flagNamesJSON.put("names", namesArray);
		println("returning json..!!");
		
		return json;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


	
	
	
	private class DownloaderTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			System.out.println("background task");
			println(url);
			// TODO Auto-generated method stub
			BitmapDownloader bmgDownloader = new BitmapDownloader(url);
			bmgDownloader.p = p;
			fileName = bmgDownloader.getBmg();;
//			while(!bmgDownloader.fileExists) println("waiting..");
			return null;
		}
		
		
	}

}
