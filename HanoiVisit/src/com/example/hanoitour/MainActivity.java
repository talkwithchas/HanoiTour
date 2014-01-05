package com.example.hanoitour;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.MapViewPosition;
import org.mapsforge.android.maps.overlay.Overlay;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.xmlpull.v1.XmlPullParserException;

import com.example.hanoitour.paser.POIData;
import com.example.hanoitour.paser.POIParser;
import com.example.hanoitour.util.GPS;
import com.example.hanoitour.util.GlobalData;
import com.example.hanoitour.util.MapOverlay;
import com.example.hanoitour.util.ProximityService;
import com.example.hanoivisit.R;

import android.os.Bundle;
import android.os.Environment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends MapActivity {
	MapView mapView;
	List<POIData> output;
	List<Overlay> mapOverlays;
	MapOverlay itemizedoverlay;
	OverlayItem overlayitem;
	ArrayList<String> names;
	boolean getResult;
	GPS gps;
	BoundingBox boundary;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
		AnniActivity.getInstance().finish();
		getResult = false;
		//xml passing
		InputStream is;
		try {
			is = getAssets().open("poi.osm");
			output = POIParser.parse(is);
			GlobalData.setListPOI(output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
				
		//maps
		mapView = (MapView) findViewById(R.id.mapView);
						
		mapView.setClickable(true);
		mapView.setBuiltInZoomControls(true);
				        
		String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
		System.out.println("Base Directory is : " + baseDir);     
		mapView.setMapFile(new File("/" + baseDir + "/" + "hanoi.map"));
		
		
		
		boundary = mapView.getMapDatabase().getMapFileInfo().boundingBox;
		
		
				
		//overlay
		mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.icon);
		itemizedoverlay = new MapOverlay(drawable, this);
		gps = new GPS(MainActivity.this);
		
		if(gps.canGetLocation){
			if(gps.getLatitude() == 0.0 && gps.getLongitude() == 0.0){
				Toast.makeText(getApplicationContext(), "Something went wrong, can't get your current location :(", Toast.LENGTH_LONG).show();
			}else{
				//check whether user is in hanoi
				if(boundary.contains(new GeoPoint(gps.getLatitude(), gps.getLongitude())) == true){
					itemizedoverlay.removeItem();
					GeoPoint point = new GeoPoint(gps.getLatitude(), gps.getLongitude());
		            overlayitem = new OverlayItem(point, "My Location", "Your Location is: \nLatitude: " + gps.getLatitude() + "\nLongitude: " + gps.getLongitude());
		            itemizedoverlay.addOverlay(overlayitem);
		    		mapOverlays.add(itemizedoverlay);
		    		mapView.setCenter(point);
		    		mapView.invalidate();
				}else{
					Toast.makeText(getApplicationContext(), "You are not in hanoi, can't show current location on map", Toast.LENGTH_LONG).show();
				}
			}
		}else{
			gps.showSettingsAlert();
		}
		Intent intent = new Intent(getApplicationContext(), ProximityService.class);
		this.startService(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_show_list:
				Intent i = new Intent(getApplicationContext(), POIListActivity.class);
				startActivityForResult(i, 1);
				
				break;
			case R.id.action_show_current_location:
				gps = new GPS(MainActivity.this);
				if(gps.canGetLocation){
					if(gps.getLatitude() == 0.0 && gps.getLongitude() == 0.0){
						Toast.makeText(getApplicationContext(), "Something went wrong, can't get your current location :(", Toast.LENGTH_LONG).show();
					}else{
						//check whether user is in hanoi
						if(boundary.contains(new GeoPoint(gps.getLatitude(), gps.getLongitude())) == true){
							itemizedoverlay.removeItem();
							GeoPoint point = new GeoPoint(gps.getLatitude(), gps.getLongitude());
				            overlayitem = new OverlayItem(point, "My Location", "Your Location is: \nLatitude: " + gps.getLatitude() + "\nLongitude: " + gps.getLongitude());
				            itemizedoverlay.addOverlay(overlayitem);
				    		mapOverlays.add(itemizedoverlay);
				    		mapView.setCenter(point);
				    		mapView.invalidate();
						}else{
							Toast.makeText(getApplicationContext(), "You are not in hanoi, can't show current location on map", Toast.LENGTH_LONG).show();
						}
					}
				}else{
					gps.showSettingsAlert();
				}
				break;
			case R.id.action_settings:
				Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
				startActivity(intent);
				break;
			
		}
		return false;
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		  if (requestCode == 1) {

		     if(resultCode == RESULT_OK){      
		         double lat = data.getDoubleExtra("lat", 0.0);
		         double lon = data.getDoubleExtra("lon", 0.0);
		         String name = data.getStringExtra("name");
		         getResult = true;
		         itemizedoverlay.removeItem();
		         GeoPoint point = new GeoPoint(lat, lon);
				 overlayitem = new OverlayItem(point, name, "Do you want to view details of this POI ?");
				 itemizedoverlay.addOverlay(overlayitem);
				 mapOverlays.add(itemizedoverlay);
				 mapView.setCenter(point);
				 mapView.invalidate();
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result
		    	 //Toast.makeText(getApplicationContext(), "No result returned", Toast.LENGTH_SHORT).show();
		     }
		  }
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(getResult == false){
			gps = new GPS(MainActivity.this);
			if(gps.canGetLocation){
				if(gps.getLatitude() == 0.0 && gps.getLongitude() == 0.0){
					Toast.makeText(getApplicationContext(), "Something went wrong, can't get your current location :(", Toast.LENGTH_LONG).show();
				}else{
					//check whether user is in hanoi
					if(boundary.contains(new GeoPoint(gps.getLatitude(), gps.getLongitude())) == true){
						itemizedoverlay.removeItem();
						GeoPoint point = new GeoPoint(gps.getLatitude(), gps.getLongitude());
			            overlayitem = new OverlayItem(point, "My Location", "Your Location is: \nLatitude: " + gps.getLatitude() + "\nLongitude: " + gps.getLongitude());
			            itemizedoverlay.addOverlay(overlayitem);
			    		mapOverlays.add(itemizedoverlay);
			    		mapView.setCenter(point);
			    		mapView.invalidate();
					}else{
						Toast.makeText(getApplicationContext(), "You are not in hanoi, can't show current location on map", Toast.LENGTH_LONG).show();
					}
				}
			}else{
				gps.showSettingsAlert();
			}
		}
	}
	

}
