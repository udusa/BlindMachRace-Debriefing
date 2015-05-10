package serverconnect;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.json.*;

import de.micromata.opengis.kml.v_2_2_0.*;

public class KMLgenerator {
	private String URL,path,event,user;
	private boolean readSucceed;
	public KMLgenerator(String _URL,String _path){
		URL = _URL;
		readSucceed=true;
		path=_path;
	}
	
	public boolean createKMLPath(){
		
		Map<EventDate,LatLng> sortedLatLngs = readData();
		if(!readSucceed)return false;
		return createKMLPath(sortedLatLngs);
		
	}
	
	public boolean createKMLTimeStamp(){
		Map<EventDate,LatLng> sortedLatLngs = readData();
		if(!readSucceed)return false;
		return createKMLTimeStamp(sortedLatLngs);
	}
	
	private Map<EventDate,LatLng> readData(){
		Map<EventDate,LatLng> sortedLatLngs = new TreeMap<EventDate,LatLng>();
		try {
			JSONObject jsonHistory = JsonReader.readJsonFromUrl(URL);
			JSONArray jsonArray = jsonHistory.getJSONArray("event");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = (JSONObject) jsonArray.get(i);
				//if (jsonObj.getString("info").equals(fullUserName)) {
					String lat = jsonObj.getString("lat");
					String lng = jsonObj.getString("lng");
					if (Double.parseDouble(lat) == 0 || Double.parseDouble(lng) == 0) {
						continue;
					}
					String time = jsonObj.getString("time");
					String date = jsonObj.getString("date");

					LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
					EventDate eventDate = new EventDate(time, date);
					sortedLatLngs.put(eventDate, latLng);
			}
            
		}
		catch (Exception e) {
			 readSucceed=false;
			 e.printStackTrace();
		}
		return sortedLatLngs;
	}

	private boolean createKMLPath(Map<EventDate,LatLng> sortedLatLngs){
		Iterator<Map.Entry<EventDate, LatLng>> i = sortedLatLngs.entrySet().iterator();
		Map.Entry<EventDate, LatLng> entry = (Map.Entry<EventDate, LatLng>) i.next();
		
		Kml kml = new Kml();
		Document doc = kml.createAndSetDocument();
		doc.setName("Path");
		doc.setDescription("Event# : "+event+" , Sailor : "+user);
		
		//style 3
		Style style3 = doc.createAndAddStyle();
		style3.setId("style3");
		IconStyle ics3 = style3.createAndSetIconStyle();
		ics3.setScale(1);
		ics3.createAndSetIcon().setHref("http://maps.google.com/mapfiles/kml/paddle/grn-blank.png");
		
		//style 2 line style
		Style style2 = doc.createAndAddStyle();
		style2.setId("style2");
		LineStyle lineStyle = new LineStyle();
		lineStyle.setColor("73FF0000");
		lineStyle.setWidth(5);
		style2.setLineStyle(lineStyle);
		
		//style 1
		Style style1 = doc.createAndAddStyle();
		style1.setId("style1");
		IconStyle ics2 = style1.createAndSetIconStyle();
		ics2.createAndSetIcon().setHref("http://maps.google.com/mapfiles/kml/paddle/red-stars.png");
		ics2.setScale(1);
		
		//Start placemark
		doc.createAndAddPlacemark().withName("FROM").withStyleUrl("#"+style3.getId()).createAndSetPoint()
		.addToCoordinates(entry.getValue().getLng(), entry.getValue().getLat());
		
		Placemark pathMarks = doc.createAndAddPlacemark();
		pathMarks.setName("PATH");
		pathMarks.setStyleUrl("#"+style2.getId());
		pathMarks.createAndSetExtendedData().createAndAddData("true").setName("_SnapToRoads");
		LineString lineString = pathMarks.createAndSetLineString();
		lineString.setTessellate(true);
		
		
		while(i.hasNext()){
			entry = (Map.Entry<EventDate, LatLng>) i.next();
			/*
			System.out.println();
			System.out.print(entry.getKey()+",");
			System.out.print(entry.getValue().getLat()+",");
			System.out.print(entry.getValue().getLng());
			*/
			if(!i.hasNext())break;
			lineString.addToCoordinates(entry.getValue().getLng(), entry.getValue().getLat());
			
		}
		doc.createAndAddPlacemark().withName("TO").withStyleUrl("#"+style1.getId()).createAndSetPoint()
		.addToCoordinates(entry.getValue().getLng(), entry.getValue().getLat());
		try {
			String timeStamp = new SimpleDateFormat("ddMMyy_HHmmss").format(new Date());
			File f = new File(path+"/"+event+"_"+user+"_"+timeStamp+"_OnlyPath.kml");
			kml.marshal(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean createKMLTimeStamp(Map<EventDate,LatLng> sortedLatLngs){
		Iterator<Map.Entry<EventDate, LatLng>> i = sortedLatLngs.entrySet().iterator();
		Map.Entry<EventDate, LatLng> entry = (Map.Entry<EventDate, LatLng>) i.next();
		String when="";
		
		Kml kml = new Kml();
		Document doc = kml.createAndSetDocument();
		doc.setName("Path");
		doc.setDescription("Event# : "+event+" , Sailor : "+user);
		
		//style 1
		Style style1 = doc.createAndAddStyle();
		style1.setId("style1");
		IconStyle ics1 = style1.createAndSetIconStyle();
		ics1.setScale(1);
		ics1.createAndSetIcon().setHref("http://maps.google.com/mapfiles/kml/paddle/grn-blank.png");
		
		//style 2
		Style style2 = doc.createAndAddStyle();
	    style2.setId("style2");
		IconStyle ics2 = style2.createAndSetIconStyle();
		ics2.setScale(1);
		ics2.createAndSetIcon().setHref("http://maps.google.com/mapfiles/kml/paddle/wht-blank.png");
		
		//style 3
		Style style3 = doc.createAndAddStyle();
	    style3.setId("style3");
		IconStyle ics3 = style3.createAndSetIconStyle();
		ics3.setScale(1);
		ics3.createAndSetIcon().setHref("http://maps.google.com/mapfiles/kml/paddle/red-blank.png");
		
		Placemark timeMarks = doc.createAndAddPlacemark();
		timeMarks.setName("FROM");
		when=entry.getKey().getDate()+"T"+entry.getKey().getTime()+"Z";
		timeMarks.createAndSetTimeStamp().setWhen(when);
		timeMarks.setStyleUrl("#"+style1.getId());
		timeMarks.createAndSetPoint().addToCoordinates(entry.getValue().getLng(), entry.getValue().getLat());
		
		while(i.hasNext()){
			entry = (Map.Entry<EventDate, LatLng>) i.next();
			if(!i.hasNext())break;
			timeMarks = doc.createAndAddPlacemark();
			when=entry.getKey().getDate()+"T"+entry.getKey().getTime()+"Z";
			timeMarks.createAndSetTimeStamp().setWhen(when);
			timeMarks.setStyleUrl("#"+style2.getId());
			timeMarks.createAndSetPoint().addToCoordinates(entry.getValue().getLng(), entry.getValue().getLat());
		}
		
		timeMarks = doc.createAndAddPlacemark();
		timeMarks.setName("TO");
		when=entry.getKey().getDate()+"T"+entry.getKey().getTime()+"Z";
		timeMarks.createAndSetTimeStamp().setWhen(when);
		timeMarks.setStyleUrl("#"+style3.getId());
		timeMarks.createAndSetPoint().addToCoordinates(entry.getValue().getLng(), entry.getValue().getLat());
		
		try {
			String timeStamp = new SimpleDateFormat("ddMMyy_HHmmss").format(new Date());
			File f = new File(path+"/"+event+"_"+user+"_"+timeStamp+"_WithTimeStamp.kml");
			kml.marshal(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
	
	public void setEvent(String event) {
		this.event=event;
	}

	public void setUser(String user) {
		this.user=user;
	}
	
	private class LatLng{
		double lat,lng;
		private LatLng(double _lat,double _lng){
			lat=_lat;
			lng = _lng;
		}
		public double getLat() {
			return lat;
		}
		public double getLng() {
			return lng;
		}
		@Override
		public String toString() {
			return "lat:"+lat+",Lng:"+lng;
		}
	}
	
	private class EventDate implements Comparable<Object>{
		String time,date;
		private EventDate(String _time,String _date){
			time=_time;
			date=_date;
		}
		public String getTime() {
			return time;
		}
		public String getDate() {
			return date;
		}
		
		@Override
		public String toString() {
			
			return "Time : "+time+" , Date : "+date;
		}
		@Override
		 public boolean equals(Object obj) {
		        if (this == obj)
		            return true;
		        if (obj == null)
		            return false;
		        if (getClass() != obj.getClass())
		            return false;
				return this.compareTo(obj)==0;
		 }
		
		@Override
		public int hashCode() {
			final int prime = 31;
		    int result = 1;
		    result = prime * result + ((time == null) ? 0 : time.hashCode());
		    result = prime * result + Integer.parseInt(time.replaceAll(":", ""));
		    return result;
		}
		@Override
		public int compareTo(Object o) {
			return Integer.parseInt(time.replaceAll(":", ""))-Integer.parseInt(((EventDate)o).time.replaceAll(":", ""));
		}

	}
}
