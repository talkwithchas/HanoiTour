package com.example.hanoitour.paser;


public class Location extends android.location.Location{

	//================================================================================
	// Internal attributes
	//================================================================================
	
	private float level;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	public Location() {
		this(0.0, 0.0, 0.0f);
	}
	
	public Location(double latitude, double longitude,float level) {
		this(latitude, longitude, level, 0);
	}
	
	public Location(double latitude, double longitude, float level, float bearing) {
		super("");
		this.setLatitude(latitude);
		this.setLongitude(longitude);
		this.setLevel(level);
		this.setBearing(bearing);
	}
	
	//================================================================================
	// Getters and Setters
	//================================================================================
	
	public float getLevel() {
		return level;
	}

	public void setLevel(float level) {
		this.level = level;
	}
}