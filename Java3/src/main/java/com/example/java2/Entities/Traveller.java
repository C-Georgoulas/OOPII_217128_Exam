package com.example.java2.Entities;

import java.io.IOException;
import java.util.ArrayList;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.java2.RetrieveData.OpenData;
import com.example.java2.Entities.Enums.CafeBarRestaur;
import com.example.java2.Entities.Enums.Museums;
@Document
public class Traveller implements Comparable {
	
    private String timestamp;
	@Id
    private String id;
	@Indexed
	private String name;
	private String username;
	private int age;
	private  String city;
	private String preferedWeather;
	private double currentlat;
	private double currentlon;
	private Museums preferedMuseums;
	private CafeBarRestaur preferedCafesRestaurantsBars;
	public ArrayList<String> preferedCities;
	public String visit;
	private String CountryName;
	public String getCountryName() {
		return CountryName;
	}



	public void setCountryName(String countryName) {
		CountryName = countryName;
	}



	/**
	 * @param name
	 * @param age
	 * @param city
	 * @param preferedWeather
	 * @param preferedMuseums
	 * @param preferedCafesRestaurantsBars
	 * @param appid
	 * @throws IOException 
	 */
	public Traveller(String name, int age ,String city, String preferedWeather,Enums.Museums preferedMuseums,Enums.CafeBarRestaur preferedCafesRestaurantsBars,ArrayList<String> preferedCities,String username) throws IOException {
		super();
		this.id="" + System.currentTimeMillis();
		this.timestamp="" + System.currentTimeMillis();
		this.name = name;
		this.age = age;
		this.city=city;
		this.preferedWeather = preferedWeather;
		this.preferedMuseums = preferedMuseums;
		this.preferedCafesRestaurantsBars = preferedCafesRestaurantsBars;
		this.preferedCities=preferedCities;
		this.username=username;
		OpenData.TravellersCityCoordinates(this,city);	
	}



	public String getTimestamp() {
		return timestamp;
	}



	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public String getPreferedWeather() {
		return preferedWeather;
	}

	public void setPreferedWeather(String preferedWeather) {
		this.preferedWeather = preferedWeather;
	}

	public double getCurrentlat() {
		return currentlat;
	}

	public void setCurrentlat(double currentlat) {
		this.currentlat = currentlat;
	}

	public double getCurrentlon() {
		return currentlon;
	}

	public void setCurrentlon(double currentlon) {
		this.currentlon = currentlon;
	}
	
	

	
	
	
	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}

	public String CompareCities(ArrayList<City> cities) {
		ArrayList<Float> similarities=new ArrayList<Float>();
		City cityfound=null;
	     float a=0;
		for(City city :cities) {
			similarities.add(Similarity(city,this));
			if(similarities.get(similarities.size()-1)>=a){
				cityfound=city;
				a=similarities.get(similarities.size()-1);
			}
		}
		return  cityfound.getCityname();
	}
   
	public  String CompareCities(ArrayList<City> cities, boolean weather) {
		ArrayList<Float> similarities=new ArrayList<Float>();
		City cityfound=null;
	     float a=0;
		for(City city :cities) {
			if((weather==true && !city.getWeather().contentEquals("Rain"))||weather==false){
			similarities.add(Similarity(city,this));
			if(similarities.get(similarities.size()-1)>=a){
				cityfound=city;
				a=similarities.get(similarities.size()-1);
			}
		}
		}
		return cityfound.getCityname();
	}
	
	public Museums getPreferedMuseums() {
		return preferedMuseums;
	}


	public void setPreferedMuseums(Museums preferedMuseums) {
		this.preferedMuseums = preferedMuseums;
	}


	public CafeBarRestaur getPreferedCafesRestaurantsBars() {
		return preferedCafesRestaurantsBars;
	}


	public void setPreferedCafesRestaurantsBars( CafeBarRestaur preferedCafesRestaurantsBars) {
		this.preferedCafesRestaurantsBars = preferedCafesRestaurantsBars;
	}

	
	public ArrayList<String> getPreferedCities() {
		return preferedCities;
	}

	public void setPreferedCities(ArrayList<String> preferedCities) {
		this.preferedCities = preferedCities;
	}
	

	public String getVisit() {
		return visit;
	}

	public void setVisit(String visit) {
		this.visit = visit;
	}

	public static double distance(double lat1, double lat2, double lon1,
	        double lon2, double el1, double el2) {

	    final int R = 6371; // Radius of the earth

	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters

	    double height = el1 - el2;

	    distance = Math.pow(distance, 2) + Math.pow(height, 2);

	    return Math.sqrt(distance);
	}


	

	protected float Similarity(City city, Traveller traveller) {
		int points;
		if(traveller.getPreferedCafesRestaurantsBars().toString().equals("notinterested")) {
			points=3;
		}
		else if(traveller.getPreferedCafesRestaurantsBars().toString().equals("interested")) {
			if(city.getCafesRestaurantsBars()>=0 &&city.getCafesRestaurantsBars()<=10) {
				points=1;
			}
			else if(city.getCafesRestaurantsBars()>=11 && city.getCafesRestaurantsBars()<=20) {
				points=2;
			}
			else {
				points=3;
			}
		}
		else {
			if(city.getCafesRestaurantsBars()>=0 &&city.getCafesRestaurantsBars()<=10) {
				points=1;
			}
			else if(city.getCafesRestaurantsBars()>=11 && city.getCafesRestaurantsBars()<=20){
				points=2;
			}
			else {
				points=3;
			}
		}
		//museums
		if(traveller.getPreferedMuseums().toString().equals("notinterested")) {
			points+=3;
		}
		else if(traveller.getPreferedMuseums().toString().equals("interested")) {
			if(city.getMuseums()>=0 &&city.getCafesRestaurantsBars()<=5) {
				points+=1;
			}
			else if(city.getCafesRestaurantsBars()>=6 && city.getCafesRestaurantsBars()<=15) {
				points+=2;
			}
			else {
				points+=3;
			}
		}
		else {
			if(city.getCafesRestaurantsBars()>=0 &&city.getCafesRestaurantsBars()<=5) {
				points+=1;
			}
			else if(city.getCafesRestaurantsBars()>=6 && city.getCafesRestaurantsBars()<=15){
				points+=2;
			}
			else {
				points+=3;
			}
		}
		return (float) points/6;
	}



	@Override
	public String toString() {
		return "Traveller [id=" + id + ", name=" + name + ", age=" + age + ", city=" + city + ", preferedWeather="
				+ preferedWeather + ", currentlat=" + currentlat + ", currentlon=" + currentlon + ", preferedMuseums="
				+ preferedMuseums + ", preferedCafesRestaurantsBars=" + preferedCafesRestaurantsBars
				+ ", preferedCities=" + preferedCities + ", visit=" + visit + "]";
	}



	@Override
	public int  compareTo(Object arg0) {
		if(arg0 instanceof Traveller) {
			Traveller traveller =(Traveller) arg0;
			if(this.getName().compareTo(traveller.getName())==1){
				return 1;
			}
		}
		return -1;
	}
}
