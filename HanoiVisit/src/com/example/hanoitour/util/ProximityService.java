package com.example.hanoitour.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.impl.conn.DefaultClientConnection;
import org.xmlpull.v1.XmlPullParserException;

import com.example.hanoitour.WebActivity;
import com.example.hanoitour.paser.POIData;
import com.example.hanoitour.paser.POIParser;
import com.example.hanoivisit.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

public class ProximityService extends Service{
	String proximitysd = "com.apps.ProximityService";
	int n = 0;
	private BroadcastReceiver mybroadcast;
	private LocationManager locationManager;
	MyLocationListener locationListenerp;
	String pix, wiki, desc, name;
	double lat, lng;
	List<POIData> output;

	//lat='21.0386461' lon='105.8225199 ==progre
	//21.0391954 ;   105.8181869 = my house
	//21.0185499; 105.7951298 = my office
	public ProximityService() {


	}
	@Override
	public void onCreate() {
		mybroadcast = new ProximityIntentReceiver();
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		//parsing
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


		float radius = output.get(0).getTrigger();
		long expiration = -1;
		System.out.println("List of POI is : " + output.size());

		for(POIData poi : output){
			lat = poi.getLocation().getLatitude();
			lng = poi.getLocation().getLongitude();
			pix = poi.getPicture();
			wiki = poi.getWiki();
			desc = poi.getDescription();
			name = poi.getName();

			String proximitys = "com.example.hanoitour.ProximityService"+n;
			IntentFilter filter = new IntentFilter(proximitys);
			registerReceiver(mybroadcast, filter );

			Intent intent = new Intent(proximitys);
			intent.putExtra("name", name);
			intent.putExtra("pix", pix);
			intent.putExtra("desc", desc);
			intent.putExtra("wiki", wiki);

			PendingIntent proximityIntent = PendingIntent.getBroadcast(this, n, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			locationManager.addProximityAlert(lat, lng, radius, expiration, proximityIntent);
			n++;
		}
	}
	@Override
	public void onDestroy() {
		Toast.makeText(this, "Proximity Service Stopped", Toast.LENGTH_LONG).show();
		try{
			unregisterReceiver(mybroadcast);
		}catch(IllegalArgumentException e){
			Log.d("reciever",e.toString());
		}


	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//Toast.makeText(this, "Proximity Service Started", Toast.LENGTH_LONG).show();
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public class ProximityIntentReceiver extends BroadcastReceiver{
		private static final int NOTIFICATION_ID = 1000;
		@SuppressWarnings("deprecation")
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub

			String key = LocationManager.KEY_PROXIMITY_ENTERING;

			Boolean entering = arg1.getBooleanExtra(key, false);

			if(entering == true){
				String name = arg1.getExtras().getString("name");
				String desc = arg1.getExtras().getString("desc");
				String wiki = arg1.getExtras().getString("wiki");
				String pix = arg1.getExtras().getString("pix");

				Intent notifIntent = new Intent(getApplicationContext(), WebActivity.class);
				notifIntent.putExtra("name", name);
				notifIntent.putExtra("pix", pix);
				notifIntent.putExtra("wiki", wiki);
				notifIntent.putExtra("desc", desc);
				
				
				NotificationCompat.Builder builder =  
			            new NotificationCompat.Builder(getApplicationContext())  
			            .setSmallIcon(R.drawable.notification)  
			            .setContentTitle("Entering proximity")  
			            .setTicker("Entering proximity!!!")
			            .setAutoCancel(true)
			            .setWhen(System.currentTimeMillis())
			            .setContentText("You are approaching POI " + name + " . Click to View");  

				PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notifIntent, PendingIntent.FLAG_CANCEL_CURRENT);         
			    builder.setContentIntent(pendingIntent);  

			    // Add as notification  
			    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
			    manager.notify(NOTIFICATION_ID, builder.build());  
				

				SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		        boolean isSound = sharedPref.getBoolean("pref_sound", false);
		        boolean isVibrate = sharedPref.getBoolean("pref_vibrate", false);
		        
		        if(isSound == true && isVibrate == false){
		        	final MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beep);
		    		mediaPlayer.start();
		    		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
		    			
		    			@Override
		    			public void onCompletion(MediaPlayer mp) {
		    				// TODO Auto-generated method stub
		    				mediaPlayer.stop();
		    				mediaPlayer.release();
		    				
		    			}
		    		});
		        }else if(isSound == false && isVibrate == true){
		        	Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		    		v.vibrate(500);
		        }else if(isSound == true && isVibrate == true){
		        	final MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beep);
		    		mediaPlayer.start();
		    		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
		    			
		    			@Override
		    			public void onCompletion(MediaPlayer mp) {
		    				// TODO Auto-generated method stub
		    				mediaPlayer.stop();
		    				mediaPlayer.release();
		    				
		    			}
		    		});
		    		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		    		v.vibrate(500);
		        }
		        
	        }
		}
	}

	public class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location arg0) {
			// TODO Auto-generated method stub
			//Toast.makeText(getApplicationContext(), "I was here", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}

	}

}
