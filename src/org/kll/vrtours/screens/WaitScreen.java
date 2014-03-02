package org.kll.vrtours.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WaitScreen extends Activity{
	
	String nextActivity;
	String panoramaFile;
	String prevActivity;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		Intent i = getIntent();
		panoramaFile = (String) i.getSerializableExtra("PanoramaFile");
		
	}
}