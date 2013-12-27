package com.example.hanoitour.paser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;


public class POIParser {
	 final static String ns = null;
	 
	 public static List<POIData> parse(InputStream in) throws XmlPullParserException, IOException {
			
			if(in == null) {
				return null;
			}
	        try {

		        XmlPullParser parser = Xml.newPullParser();
		        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		        parser.setInput(in, null);
		        parser.nextTag();
		
		        return readFeed(parser);
	        } finally {
	            in.close();
	        }
		}
	 private static List<POIData> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
			List<POIData> entries = new ArrayList<POIData>();

		    parser.require(XmlPullParser.START_TAG, ns, "osm");
		    while (parser.next() != XmlPullParser.END_TAG) {
		        if (parser.getEventType() != XmlPullParser.START_TAG) {
		            continue;
		        }
		        String name = parser.getName();
		        if (name.equals("node")) {
		        	POIData entry = readEntry(parser);
		        	if(entry != null) {
		        		entries.add(entry);
		        	}
		        } else {
		            skip(parser);
		        }
		    }  
		    return entries;
	}
	 private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		    if (parser.getEventType() != XmlPullParser.START_TAG) {
		        throw new IllegalStateException();
		    }
		    int depth = 1;
		    while (depth != 0) {
		        switch (parser.next()) {
		        case XmlPullParser.END_TAG:
		            depth--;
		            break;
		        case XmlPullParser.START_TAG:
		            depth++;
		            break;
		        }
		    }
	}
	private static POIData readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
		    parser.require(XmlPullParser.START_TAG, ns, "node");

		    int id = 0;
			String names = null;
			String picture = null;
			Double lat = Double.valueOf(parser.getAttributeValue(null, "lat"));
		    Double lon = Double.valueOf(parser.getAttributeValue(null, "lon"));
			String description = null;
			int trigger = 0;
			String wiki = null;
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
		            continue;
		        }
				String name = parser.getName();
		        if (name.equals("tag") && parser.getAttributeValue(null, "k").equals("id")) {
		            id = Integer.valueOf(parser.getAttributeValue(null, "v"));
		            parser.nextTag();
		        }else if(name.equals("tag") && parser.getAttributeValue(null, "k").equals("name")){
		        	names = parser.getAttributeValue(null, "v");
		            parser.nextTag();
		        }else if(name.equals("tag") && parser.getAttributeValue(null, "k").equals("picture")){
		        	picture = parser.getAttributeValue(null, "v");
		            parser.nextTag();
		        }else if(name.equals("tag") && parser.getAttributeValue(null, "k").equals("description")){
		        	description = parser.getAttributeValue(null, "v");
		            parser.nextTag();
		        }else if(name.equals("tag") && parser.getAttributeValue(null, "k").equals("trigger")){
		        	trigger = Integer.valueOf(parser.getAttributeValue(null, "v"));
		            parser.nextTag();
		        }else if(name.equals("tag") && parser.getAttributeValue(null, "k").equals("wiki")){
			        	wiki = parser.getAttributeValue(null, "v");
			            parser.nextTag();
		        }else{
		        	skip(parser);
		        }
			}
			return id == 0 ? null : new POIData(id, names, picture,new Location(lat, lon, 0), description, trigger, wiki);
	}
}
