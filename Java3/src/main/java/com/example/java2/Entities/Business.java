package com.example.java2.Entities;

import java.io.IOException;
import java.util.ArrayList;


public class Business extends Traveller {

	public Business(String name, int age ,String city, String preferedWeather,Enums.Museums preferedMuseums,Enums.CafeBarRestaur preferedCafesRestaurantsBars,ArrayList<String> preferedCities,String username) throws IOException {
		super(name, age, city, preferedWeather, preferedMuseums, preferedCafesRestaurantsBars,preferedCities,username);
		}
	
	@Override
	protected  float Similarity(City  city,Traveller traveller) {
		double km=DistanceCalculator.distance(city.getLat(), city.getLon(), traveller.getCurrentlat(), traveller.getCurrentlon(), "K");
		
		return (float) km;
		
	}
	@Override
    public String CompareCities(ArrayList<City> cities) {
		float min=1000000;
		City nearestcity=null;
		for(City city: cities) {
			if( Similarity(city,this)<min) {
				min=Similarity(city,this);
				nearestcity=city;
			}
		}
		
		return nearestcity.getName();
	}
	@Override
    public String CompareCities(ArrayList<City> cities, boolean weather) {
		float min=1000000;
		City nearestcity=null;
		for(City city: cities) {
			if( Similarity(city,this)<min && !city.getWeather().equals("Rain")) {
				min=Similarity(city,this);
				nearestcity=city;
			}
		}
		return nearestcity.getCityname();
	}

}
