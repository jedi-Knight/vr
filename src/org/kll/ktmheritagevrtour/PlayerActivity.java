/*
 * PanoramaGL library
 * Version 0.1
 * Copyright (c) 2010 Javier Baez <javbaezga@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
 /*This code and the PanoramaGL library has been modified by Pratik Gautam <gautamxpratik@gmail.com>
 * Licensed under the GPLv3 License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://gplv3.fsf.org/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 

package org.kll.ktmheritagevrtour;

import java.util.Random;


import javax.microedition.khronos.opengles.GL10;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import processing.data.XML;

import com.panoramagl.PLCubicPanorama;
import com.panoramagl.PLCylindricalPanorama;
import com.panoramagl.PLILoader;
import com.panoramagl.PLIPanorama;
import com.panoramagl.PLIView;
import com.panoramagl.PLImage;
import com.panoramagl.PLJSONLoader;
import com.panoramagl.PLSpherical2Panorama;
import com.panoramagl.PLSphericalPanorama;
import com.panoramagl.PLView;
import com.panoramagl.PLViewEventListener;
import com.panoramagl.enumeration.PLCubeFaceOrientation;
import com.panoramagl.hotspots.PLHotspot;
import com.panoramagl.ios.structs.CGPoint;
import com.panoramagl.structs.PLPosition;
import com.panoramagl.utils.PLUtils;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View.OnClickListener;

public class PlayerActivity extends PLView
{
	/**constants*/
	
	private static final int kHotspotIdMin = 1;
	private static final int kHotspotIdMax = 1000;
	
	/**member variables*/
	
	private Random random = new Random();
	
	/**init methods*/
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        JSONArray names1;
        
        try{
        	JSONObject flagNamesJSON = new JSONObject((String) getIntent().getSerializableExtra("flagNamesXML"));
        	names1 = flagNamesJSON.getJSONArray("names");
        }catch(JSONException e){
        	names1 = null;
        	System.out.println("JSONParsingError");
        }
        
        final JSONArray names = names1;
        names1 = null;
        String fileName = (String) getIntent().getSerializableExtra("PanoramaScript");

        this.load(new PLJSONLoader(this, fileName));
        this.setListener(new PLViewEventListener()
        {
        	@Override
    		public void onDidClickHotspot(PLIView pView, PLHotspot hotspot, CGPoint screenPoint, PLPosition scene3DPoint)
        	{
        		try{
        		Toast.makeText(pView.getActivity(), names.getString((int) (hotspot.getIdentifier()-1)), Toast.LENGTH_SHORT).show();
        		}catch(JSONException e){
        			System.out.println("Error parsing flagNamesJSON !!");
        		}
        	}
		});
    }
    
    /**
     * This event is fired when OpenGL renderer was created
     * @param gl Current OpenGL context
     */
    @Override
	protected void onGLContextCreated(GL10 gl)
	{
    	super.onGLContextCreated(gl);
	}
    
    public void onPause(){
    	super.onPause();
    	finish();
    }
}
