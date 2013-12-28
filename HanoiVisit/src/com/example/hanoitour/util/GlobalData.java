package com.example.hanoitour.util;

import java.util.List;

import com.example.hanoitour.paser.POIData;


public class GlobalData {
	private static List<POIData> listTitles;  
	  
	public static void setListPOI(List<POIData> titles) {  
	    GlobalData.listTitles = titles;  
	}  
	  
	public static List<POIData> getListPOI() {  
	    return GlobalData.listTitles;  
	}  
}
