package com.example.java2.RetrieveData;
import com.example.java2.Entities.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.java2.Api.Weather.*;
import com.example.java2.Api.Wikipedia.*;
public class OpenData {

/**Retrieves weather information, geotag (lan, lon) and a Wikipedia article for a given city.
* @param city The Wikipedia article and OpenWeatherMap city. 
* @param country The country initials (i.e. gr, it, de).
* @param appid Your API key of the OpenWeatherMap.
 * @return */
	public static int StringNumber(String apiString,String category ) {
		int i = 0;
		Pattern p = Pattern.compile(category);
		Matcher m = p.matcher( apiString );
		while (m.find()) {
		    i++;
		}
		return i;
	}
	public static City RetrieveData(String city,int timeslived,int timesvisited) throws  IOException {
		 ObjectMapper mapper = new ObjectMapper(); 
		 OpenWeatherMap weather_obj=null; 
		 MediaWiki mediaWiki_obj=mapper.readValue(new URL("https://en.wikipedia.org/w/api.php?action=query&prop=extracts&titles="+city+"&format=json&formatversion=2"),MediaWiki.class);	
		 try {
			 weather_obj=mapper.readValue(new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=a9414d4e18e890688dc1c0ab7d4db7ba"), OpenWeatherMap.class);
		 }
		 catch(java.io.FileNotFoundException e) {
			 weather_obj=mapper.readValue(new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=49d866500fb5a40646e72e85e7d95c29"), OpenWeatherMap.class);
		 }
		 int museums=StringNumber(mediaWiki_obj.getQuery().getPages().get(0).getExtract(),"Museum");
		 int cafes=StringNumber(mediaWiki_obj.getQuery().getPages().get(0).getExtract(),"Cafe");
		 int restaurants=StringNumber(mediaWiki_obj.getQuery().getPages().get(0).getExtract(),"restaurant");
		 int bars=StringNumber(mediaWiki_obj.getQuery().getPages().get(0).getExtract(),"Bar");
		 int cafeRestaurantBar = cafes+restaurants+bars;
		 String weather=weather_obj.getWeather().get(0).getMain();
		 double lat= weather_obj.getCoord().getLat();
		 double lon= weather_obj.getCoord().getLon();
		 String countryname=weather_obj.getSys().getCountry();
		 City city_info = new City(museums,city,cafeRestaurantBar,weather,lat,lon,timesvisited,timeslived,countryname);
		 return city_info;
	}
	public  static  void TravellersCityCoordinates(Traveller traveller,String city) throws IOException {
		ObjectMapper mapper = new ObjectMapper(); 
		OpenWeatherMap weather_obj = mapper.readValue(new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=a9414d4e18e890688dc1c0ab7d4db7ba"),OpenWeatherMap.class);
		double lat= weather_obj.getCoord().getLat();
		double lon= weather_obj.getCoord().getLon();
		String countryname =weather_obj.getSys().getCountry();
		traveller.setCurrentlat(lat);
		traveller.setCurrentlon(lon);
		traveller.setCountryName(countryname);
	}
}