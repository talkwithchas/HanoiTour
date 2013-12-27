package com.example.hanoitour.paser;

import android.location.Location;

public class POIData {
	private int id;
	private String name;
	private String picture;
	private Location location;
	private String description;
	private int trigger;
	private String wiki;
	
	public POIData(int id, String name, String picture, Location location, String description, int trigger, String wiki){
		this.id = id;
		this.name = name;
		this.picture = picture;
		this.location = location;
		this.description = description;
		this.trigger = trigger;
		this.wiki = wiki;
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getTrigger() {
		return trigger;
	}
	public void setTrigger(int trigger) {
		this.trigger = trigger;
	}
	public String getWiki() {
		return wiki;
	}
	public void setWiki(String wiki) {
		this.wiki = wiki;
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "id:"+id+", "+
				"name:"+name+", "+
				"decription:"+description+", "+
				"picture:"+picture+", "+
				"lat:"+location.getLatitude()+", "+
				"lon:"+location.getLongitude()+", "+
				"trigger:"+trigger+", "+
				"wiki:"+wiki;
	}
	

}
