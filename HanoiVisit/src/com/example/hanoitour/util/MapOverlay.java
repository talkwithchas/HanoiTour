package com.example.hanoitour.util;

import java.util.ArrayList;
import java.util.List;

import org.mapsforge.android.maps.overlay.ItemizedOverlay;
import org.mapsforge.android.maps.overlay.OverlayItem;

import com.example.hanoitour.WebActivity;
import com.example.hanoitour.paser.POIData;
import com.example.hanoivisit.R;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

public class MapOverlay extends ItemizedOverlay{
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	Context mContext;
	
	
	public MapOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	// Removes overlay item i
    public void removeItem(){
    	mOverlays.removeAll(mOverlays);
        populate();
    }
	protected OverlayItem createItem(int arg0) {
		// TODO Auto-generated method stub
		return mOverlays.get(arg0);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
	}
	String name = null;
	String pix = null;
	String desc = null;;
	String wiki = null;
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
	  List<POIData> poi = GlobalData.getListPOI();
	  String strCom = item.getTitle();
	  for(POIData p : poi){
		  if(p.getName().equalsIgnoreCase(strCom)){
			    name = p.getName();
			    pix = p.getPicture();
			    desc = p.getDescription();
			    wiki = p.getWiki();
		  }
	  }
	  if(strCom.equalsIgnoreCase("My Location")){
		  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		  dialog.setTitle(item.getTitle());
		  dialog.setMessage(item.getSnippet());
		  dialog.setIcon(R.drawable.user);
		  dialog.show();
		  
	  }else{
		  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		  dialog.setNegativeButton(R.string.action_dismiss, new DialogInterface.OnClickListener() {
	          public void onClick(DialogInterface dialog, int id) {
	              // User cancelled the dialog
	        	  dialog.dismiss();
	          }
	      });
		  dialog.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
	          public void onClick(DialogInterface dialog, int id) {
	        	  Intent i = new Intent(mContext, WebActivity.class);
	        	  i.putExtra("name", name);
	        	  i.putExtra("pix", pix);
	        	  i.putExtra("desc", desc);
	        	  i.putExtra("wiki", wiki);
	        	  mContext.startActivity(i); 
	          }
	      });
		  
		  dialog.setTitle(item.getTitle());
		  dialog.setMessage(item.getSnippet());
		  dialog.setIcon(R.drawable.list_img);
		  dialog.show();
	  }
	 
	
	  
	  return true;
	}

}
