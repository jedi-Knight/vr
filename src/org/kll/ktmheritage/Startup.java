package org.kll.ktmheritage;

import java.io.File;
import java.io.IOException;

import org.kll.vrtours.iohelpers.Constants;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;

import com.modestmaps.providers.connection.TileBaseHelper;

import processing.core.PApplet;

public class Startup extends PApplet {
	
	final String serverADD = Constants.DEVURL;
	boolean dbExists = false;
	//boolean dbClosed =false;
	PApplet p = this;
	String dbPath;
	TileBaseHelper tileDownloader = new TileBaseHelper(p, serverADD+"map.php");
	
	DownloaderTask task = new DownloaderTask();
	public void setup(){
		background(100,0,0);
		File dir = new File(Environment.getExternalStorageDirectory(),"vrtourdownloads");
		dir.mkdirs();
		dbPath = dir.toString()+"/";
		println("DATABASE STORAGE PATH: "+dbPath);
		
		println("Setting DATABASE STORAGE PATH AS: "+dbPath);
		tileDownloader.setDBPathAs(dbPath);
		task.execute();
	}
	
	public void draw(){
		background(100,0,0);
		textSize(30);
		//fill(0);
		text((int)(100*tileDownloader.getDownloadedBytes()/tileDownloader.getTotalBytes())+" %", width/2, height/2-20);
		line(0, height/2, width*tileDownloader.getDownloadedBytes()/tileDownloader.getTotalBytes(), height/2);
		
		if(dbExists){	
			println("launching map..");
			//tileDownloader.closeDatabase();
			Intent i = new Intent(this, org.kll.ktmheritage.KTMVRTourApp_mob1.class);
			startActivity(i);
		}else{
			//clear();
		}
		
		
		
	}
	
	public void onPause(){
		super.onPause();
		finish();
	}
	
	private class DownloaderTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void...arg0) {
			
		try{
			println("AsyncTask: loading database..!!");
			dbExists = tileDownloader.createDatabase();
			
			//tileDownloader.closeDatabase();
			println("AsyncTask: database closed..!!");
			//dbClosed = true;
		}catch(Exception e){
			println("AsyncTask: cannot connect to "+serverADD+", check internet connection..!! ");
			e.printStackTrace();
		}
			return null;
		}
		
	}

}
