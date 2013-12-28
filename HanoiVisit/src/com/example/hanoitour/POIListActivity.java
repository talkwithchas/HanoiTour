package com.example.hanoitour;

import java.util.ArrayList;

import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.core.GeoPoint;

import com.example.hanoitour.paser.POIData;
import com.example.hanoitour.util.GlobalData;
import com.example.hanoivisit.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class POIListActivity extends Activity {
	ArrayList<String> names;
	ListView l;
	EditText inputSearch;
	ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poilist);
		names = new ArrayList<String>();
		for(POIData n : GlobalData.getListPOI()){
 		   names.add(n.getName());
		}
		l = (ListView) findViewById(R.id.list_view);
		inputSearch = (EditText) findViewById(R.id.inputSearch);
		
		adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, R.id.textView1, names);
		l.setAdapter(adapter);
		
		l.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
            	double lat = GlobalData.getListPOI().get(pos).getLocation().getLatitude();
            	double lon = GlobalData.getListPOI().get(pos).getLocation().getLongitude();
            	String name = GlobalData.getListPOI().get(pos).getName();
            	Intent i = new Intent();
            	i.putExtra("lat", lat);
            	i.putExtra("lon", lon);
            	i.putExtra("name", name);
            	setResult(RESULT_OK,i);     
            	finish();
			}
	    });
		
		inputSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				POIListActivity.this.adapter.getFilter().filter(s);
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.poilist, menu);
		return true;
	}
	

}
