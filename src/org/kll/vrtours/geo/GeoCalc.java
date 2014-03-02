package org.kll.vrtours.geo;

public class GeoCalc {

	long R = 6371*1000;		/****R in meters****/
	float lat0, lon0, lat1, lon1, dir;    /**lat,lon,dir: decimal degrees****/
	
	public GeoCalc(float lat0, float lon0, float lat1, float lon1){
		this.lat0 = lat0;
		this.lon0 = lon0;
		this.lat1 = lat1;
		this.lon1 = lon1;		
	}
	
	public float distance(){
		return (float) Math.acos(Math.sin(lat0)*Math.sin(lat1) + Math.cos(lat0)*Math.cos(lat1) * Math.cos(lon0-lon1)) * R;
	}
	
	public float hangle(){
//		double y = Math.sin(lon1-lon0) * Math.cos(lat1);
//		double x = Math.cos(lat0)*Math.sin(lat1) - Math.sin(lat0)*Math.cos(lat1)*Math.cos(lon1-lon0);
//		return (float) Math.toDegrees(Math.atan2(y, x));
		
		return (float) Math.toDegrees(Math.atan2((lon1-lon0), (lat1-lat0)));
		
		
	}
}
