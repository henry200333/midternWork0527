package model;


import java.sql.Timestamp;

public class WeatherReportBean {

	private String lacationName;
	private int geocode;
	private double lat;
	private double lon;
	private String elementName;
	private String description;
	private Timestamp dateTime;
	private Short value;
	private String measures;
	
	@Override
	public String toString() {
		return "{\"locationName\":\"" + lacationName + "\",\"geocode\":\"" + geocode + "\",\"lat\":\"" + lat + "\",\"lon\":\""
				+ lon + "\",\"element\":\"" + elementName + "\",\"description\":\"" + description + "\",\"dateTime\":\"" + dateTime
				+ "\",\"value\":\"" + value + "\",\"measures\":\"" + measures+"\"}";
	}
	public String getLacationName() {
		return lacationName;
	}
	public void setLacationName(String lacationName) {
		this.lacationName = lacationName;
	}
	public int getGeocode() {
		return geocode;
	}
	public void setGeocode(int geocode) {
		this.geocode = geocode;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getDateTime() {
		return dateTime;
	}
	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}
	public Short getValue() {
		return value;
	}
	public void setValue(Short value) {
		this.value = value;
	}
	public String getMeasures() {
		return measures;
	}
	public void setMeasures(String measures) {
		this.measures = measures;
	}
	
	
	
	
}
