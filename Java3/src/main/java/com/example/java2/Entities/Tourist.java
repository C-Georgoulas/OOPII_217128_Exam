package com.example.java2.Entities;

import java.io.IOException;
import java.util.ArrayList;

import com.example.java2.Entities.Enums.CafeBarRestaur;
import com.example.java2.Entities.Enums.Museums;

public class Tourist extends Traveller {
	  
	
	
	
	public Tourist(String name, int age ,String city, String preferedWeather,Enums.Museums preferedMuseums,Enums.CafeBarRestaur preferedCafesRestaurantsBars,ArrayList<String> preferedCities,String username) throws IOException{
		super(name,age,city,preferedWeather,preferedMuseums,preferedCafesRestaurantsBars,preferedCities,username);
	 }
	
	
	@Override
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
		return cityfound.getCityname();
	}
	@Override
    public String CompareCities(ArrayList<City> cities, boolean weather) {
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
}


