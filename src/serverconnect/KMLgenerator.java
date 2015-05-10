package serverconnect;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.json.*;

import de.micromata.opengis.kml.v_2_2_0.*;

public class KMLgenerator {
	private String URL,path,event,user;
	private boolean readSucceed;
	private Date d;
	public KMLgenerator(String _URL,String _path){
		URL = _URL;
		readSucceed=true;
		path=_path;
		d = new Date();
	}
	
	public boolean createKMLPath(){
		
		Map<String,LatLng> sortedLatLngs = readData();
		
		if(!readSucceed)return false;
		
		return createKMLPath(sortedLatLngs);
		
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
	
	private Map<String,LatLng> readData(){
		Map<String,LatLng> sortedLatLngs = new TreeMap<String,LatLng>();
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
                    //time = time.replace(":","");
					LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
					//System.out.println("Time : "+time+",latlng : "+latLng.toString());
					// Adds sailor's data to TreeMap.
					sortedLatLngs.put(time, latLng);
			}
            
		}
		catch (Exception e) {
			 readSucceed=false;
		}
		return sortedLatLngs;
	}

	private boolean createKMLPath(Map<String,LatLng> sortedLatLngs){
		Iterator<Map.Entry<String, LatLng>> i = sortedLatLngs.entrySet().iterator();
		Map.Entry<String, LatLng> entry = (Map.Entry<String, LatLng>) i.next();
		
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
			entry = (Map.Entry<String, LatLng>) i.next();
			System.out.println();
			System.out.print(entry.getKey()+",");
			System.out.print(entry.getValue().getLat()+",");
			System.out.print(entry.getValue().getLng());
			if(!i.hasNext())break;
			lineString.addToCoordinates(entry.getValue().getLng(), entry.getValue().getLat());
			
		}
		doc.createAndAddPlacemark().withName("TO").withStyleUrl("#"+style1.getId()).createAndSetPoint()
		.addToCoordinates(entry.getValue().getLng(), entry.getValue().getLat());
		try {
			kml.marshal(new File(path+"/KML_"+user+"_"+event+"_"+d.getTime()+".kml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void setEvent(String event) {
		// TODO Auto-generated method stub
		this.event=event;
	}

	public void setUser(String user) {
		// TODO Auto-generated method stub
		this.user=user;
	}
}
