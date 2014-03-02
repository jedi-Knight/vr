package org.kll.vrtours.iohelpers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import processing.core.*;

public class BitmapDownloader {
	
	String url = Constants.DEVURL;
	
	public String fileName = null;
	public boolean fileExists = false;

	public PApplet p;
	File file;
	public String dataFileName = null;
	
	public BitmapDownloader(String url, String download_to){
		this.url = url.trim();
		
		
		for (int i = url.length()-1; i>-0; i--){
			if(url.charAt(i)=='/'){
				this.fileName = url.substring(i+1);
				break;
			}
		}
		fileName = download_to + fileName;
		p.println("FILENAME: "+fileName);
	}
	
	
	public BitmapDownloader(String url){
		url = url.replace(" ", "_");
		
		fileName = url;
		
		this.url += url;
		
		File dir = new File (Environment.getExternalStorageDirectory(),"vrtourdownloads");
		dir.mkdirs();
		p.println("URL: "+ this.url);
		p.println("FILENAME: "+fileName);
		dataFileName = fileName;
		fileName = dir+"/"+fileName;
		p.println("FILENAME: "+fileName);
	}
	

	public byte[] download(){  //TODO: should throw exception if called from UI Thread
		p.println("downloading from: "+ url);
		p.println("downloading to: "+ fileName);
		byte[] b = null;
		byte[] d = null;
		try{
			p.println("downloading panopic data from: "+url);
			b = p.loadBytes(url);
			d = p.loadBytes(url+"data");
			p.println("panopic data downloaded from server!! ");
			p.println("downloaded!! local file: " + fileName);
		}catch(Exception e){
			p.println("not connected!! Exception: "+ e.getMessage());
		}
		
		if (d != null){
			p.println("now creating output file..");
			OutputStream o = p.createOutput (fileName);
			p.println("output file created!!");
			try {
				o.write(b);
				o.flush();
				o.close();
				b = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				p.println("error writing bitmap");
			}
			
			p.println("now creating output file for data..");
			o = p.createOutput (fileName+"data");
			p.println("output file created!!");
			try {
				o.write(d);
				o.flush();
				o.close();
				d = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				p.println("error writing vrtour data");
			}
			
		}else{
			p.println("download(): no data for vrtour!!");
			return null;
		}
		
		byte[] bmg = p.loadBytes(fileName);
		if(bmg==null){
			p.println("downloaded bitmap not valid!!");
		}
		fileExists = true;
		return bmg;
		
	}
	
	
	public String getBmg(){
		
				p.println("loading Bitmap from bitmap file: "+ fileName);
				Bitmap bmg = BitmapFactory.decodeFile(fileName);
				
				if(bmg==null){
					p.println("local bitmap file doesn't exist..downloading from server: "+url);
					if(download()!=null) return fileName; //check for null return in bitmap receiver code..
				}
				fileExists = true;
				bmg = null;
				return fileName;
	}
		
		
	
}
