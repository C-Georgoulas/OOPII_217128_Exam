package com.example.Handler;

import java.io.IOException;
import java.util.ArrayList;

import com.example.java2.Entities.City;
import com.example.java2.Entities.Country;
import com.example.java2.Entities.Enums;
import com.example.java2.Repositories.CityRepository;
import com.example.java2.Repositories.CountryRepository;
import com.example.java2.RetrieveData.OpenData;

public class  LocationsHandler {
	
	public static City CityCheck(String city,boolean CityInDb,CityRepository cr,Enums.LiveorVisitorNeither LiveOrVisitorNeither) throws IOException {
		City citydb;
	
		if(CityInDb && LiveOrVisitorNeither.toString().contentEquals("live") ) { //living insert
	        	citydb=cr.findById(city).get(0);
	        	City citytmp=OpenData.RetrieveData(citydb.getCityname(),1,0);
	        	if(citytmp!=null) {
	            citydb.setWeather(citytmp.getWeather());
	        	citydb.setTimeslived(1);
	        	}
	        	else {
	        		citydb=null;
	        	}
	            cr.update(citydb);
	            
	            }
	            else if(CityInDb==false && LiveOrVisitorNeither.toString().contentEquals("live")) {
	            	citydb=OpenData.RetrieveData(city,1,0);
	            	 cr.save(citydb);
	            }
	            else if(CityInDb==true && LiveOrVisitorNeither.toString().contentEquals("visit")) {
	            	citydb=cr.findById(city).get(0);
	            	City citytmp=OpenData.RetrieveData(citydb.getCityname(),0,1);
	            	if(citytmp!=null) {
	    	            citydb.setWeather(citytmp.getWeather());
	    	        	citydb.setTimesvisited(1);
	    	        	}
	    	        	else {
	    	        		
	    	        	}
		            cr.update(citydb);
	            }
	            else if(CityInDb==false && LiveOrVisitorNeither.toString().contentEquals("visit")){
	            	citydb=OpenData.RetrieveData(city,0,1);
	            	 cr.save(citydb);
	            }
	            else if(CityInDb==false && LiveOrVisitorNeither.toString().contentEquals("neither")){
	            	citydb=OpenData.RetrieveData(city,0,0);
	            	 cr.save(citydb);
	            }
	            else {
	            	citydb=cr.findById(city).get(0);
	            	City citytmp=OpenData.RetrieveData(citydb.getCityname(),0,0);
	            	if(citytmp!=null) {
	    	            citydb.setWeather(citytmp.getWeather());
	    	        	citydb.setTimeslived(citytmp.getTimesvisited());
	    	        	}
	    	        	else {
	    	        		citydb=null;
	    	        	}
		            cr.update(citydb);
	            }
		  return citydb;
	}
	public static void CountryCheck(City city,boolean CountryInDb,CountryRepository countryr,Enums.LiveorVisitorNeither LiveOrVisitorNeither) throws IOException {
		Country country;
		ArrayList<City> tmpcity=new ArrayList<City>();
		if(CountryInDb && LiveOrVisitorNeither.toString().contentEquals("live") ) { //living insert
			      country=countryr.findById(city.getCountryName()).get(0);
				 country.setCities(city);
				 country.setTimeslived(1);
				 countryr.update(country);
	            }
	            else if(CountryInDb==false && LiveOrVisitorNeither.toString().contentEquals("live")) {
	            	tmpcity.add(city);
	            	 country=new Country(city.getCountryName(),tmpcity);
	            	 country.setTimeslived(1);
	            	 countryr.save(country);
	            }
	            else if(CountryInDb==true && LiveOrVisitorNeither.toString().contentEquals("visit")) {
	            	   country=countryr.findById(city.getCountryName()).get(0);
	  				 country.setCities(city);
	  				 country.setTimesvisited(1);
	  				 countryr.update(country);
	  	            countryr.update(country);
	            }
	            else if(CountryInDb==false && LiveOrVisitorNeither.toString().contentEquals("visit")){
	            	tmpcity.add(city);
	            	 country=new Country(city.getCountryName(),tmpcity);
	            	 country.setTimesvisited(1);
	            	 ;
	            	 countryr.save(country);
	            }
	            else if(CountryInDb==false && LiveOrVisitorNeither.toString().contentEquals("neither")){
	            	tmpcity.add(city);
	            	 country=new Country(city.getCountryName(),tmpcity);
	            	 //DbCountry.setTimesvisited(0);
	            	 countryr.save(country);
	            }
	            else {
	            
	                country=countryr.findById(city.getCountryName()).get(0);
					 country.setCities(city);
					 //country.setTimeslived(0);
		            countryr.update(country);
	            }
	}
	}

