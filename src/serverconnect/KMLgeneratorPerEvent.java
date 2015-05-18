package serverconnect;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.IconStyle;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Style;

public class KMLgeneratorPerEvent extends KMLgeneratorPerUser {

	public KMLgeneratorPerEvent(String _historyURL, String _path,String _eventURL) {
		super(_historyURL, _path,_eventURL);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean createKMLTimeStamp() {
		Map<EventDate, LatLng> sortedLatLngs = super.readData();
		Map<String, LatLng> buoysLatLng = super.readBuoys();
		if(!isReadSucceed())return false;
		return this.createKMLTimeStamp(sortedLatLngs,buoysLatLng);
	}
	
	
	@Override
	protected boolean createKMLTimeStamp(Map<EventDate, LatLng> sortedLatLngs,
		Map<String, LatLng> buoysLatLng) {
		Iterator<Map.Entry<EventDate, LatLng>> startFinishLines = sortedLatLngs.entrySet().iterator();
		
		
		Iterator<Map.Entry<EventDate, LatLng>> useIterator = sortedLatLngs.entrySet().iterator();
		Map.Entry<EventDate, LatLng> userEntry = (Map.Entry<EventDate, LatLng>) useIterator.next();
		
		Iterator<Map.Entry<String, LatLng>> buoyIterator = buoysLatLng.entrySet().iterator();
		Map.Entry<String, LatLng> buoyEntry = (Map.Entry<String, LatLng>)buoyIterator.next();
		String when = "";
		
		Kml kml = new Kml();
		Document doc = kml.createAndSetDocument();
		doc.setName("TimeStamp");
		doc.setDescription("Event# : " + getEvent());
		
		//start line style
		Style startLine = doc.createAndAddStyle();
		startLine.setId("startLine");
		IconStyle icStart = startLine.createAndSetIconStyle();
		icStart.setScale(1);
		icStart.createAndSetIcon().setHref("http://bmr.comuv.com/flag_start.png");
		
		//finish line style
		Style finishLine = doc.createAndAddStyle();
		finishLine.setId("finishLine");
		IconStyle icFinish = finishLine.createAndSetIconStyle();
		icFinish.setScale(1);
		icFinish.createAndSetIcon().setHref("http://bmr.comuv.com/flag_finish.png");
		
		
		
		
		
		
		// buoysStyle
		Style buoysStyle = createBuoyStyle(doc);
		for (int i = 0; i < getUserSize(); i++) {
			Style style = doc.createAndAddStyle();
			style.setId("style"+i);
			IconStyle ics = style.createAndSetIconStyle();
			ics.setScale(1);
			ics.createAndSetIcon().setHref("http://bmr.comuv.com/boats_icons/ic_sailor_"+i+".png");
		}
		//Line MARKS
		
		Placemark lineMark = doc.createAndAddPlacemark();
		Map.Entry<EventDate, LatLng> startFinishLinesEntry = (Map.Entry<EventDate, LatLng>) startFinishLines.next();
		lineMark.setStyleUrl("#"+startLine.getId());
		lineMark.setName("Start Line!");
		lineMark.setDescription("Start Line!");
		lineMark.createAndSetPoint().addToCoordinates(startFinishLinesEntry.getValue().getLng(), startFinishLinesEntry.getValue().getLat());
		
		
		//USER MARKS
		
		Placemark timeMarks = doc.createAndAddPlacemark();
		
		
		
		while (useIterator.hasNext()) {
			String userName = userEntry.getKey().getUser();
			timeMarks = doc.createAndAddPlacemark();
			timeMarks.setName(userName);
			createPlacemarkDescription(timeMarks,userEntry);
			when = userEntry.getKey().getDate() + "T" + userEntry.getKey().getTime()
					+ "Z";
			timeMarks.createAndSetTimeStamp().setWhen(when);
			timeMarks.setStyleUrl("#style" + getUserIndex(userName));
			timeMarks.createAndSetPoint().addToCoordinates(
					userEntry.getValue().getLng(), userEntry.getValue().getLat());
			
			userEntry = (Map.Entry<EventDate, LatLng>) useIterator.next();
		}
		
		while(buoyIterator.hasNext()){
			timeMarks = doc.createAndAddPlacemark();
			timeMarks.setStyleUrl("#" + buoysStyle.getId());
			timeMarks.createAndSetPoint().addToCoordinates(
					buoyEntry.getValue().getLng(), buoyEntry.getValue().getLat());
			buoyEntry = (Map.Entry<String, LatLng>)buoyIterator.next();
		}
		try {
			String timeStamp = new SimpleDateFormat("ddMMyy_HHmmss").format(new Date());
			File f = new File(getPath() + "/" + getEvent() + "_" + timeStamp+ "_WithTimeStamp.kml");
			kml.marshal(f);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	
	}
	
	

}
