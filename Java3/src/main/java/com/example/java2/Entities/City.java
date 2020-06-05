package com.example.java2.Entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class City {
	public static ArrayList<City> CitiesSession;
	public City( int museums, String name, int cafesRestaurantsBars, String weather, Double lat, Double lon,int timesvisited,int timeslived,String CountryName) {
		super();
		this.museums = museums;
		this.cityname = name;
		this.cafesRestaurantsBars = cafesRestaurantsBars;
		this.weather = weather;
		this.lat = lat;
		this.lon = lon;
		this.timesvisited=timesvisited;
		this.timeslived=timeslived;
		this.CountryName=CountryName;
		this.name= lat.toString()+lon.toString();
		
	}
	public String getName() {
		
		return this.name;
	}
	public void setCityname(String name) {
		this.cityname = name;
	}
	@Indexed
	private int museums;
	private String cityname;
	public static ArrayList<City> getCitiesSession() {
		return CitiesSession;
	}
	public static void setCitiesSession(ArrayList<City> citiesSession) {
		CitiesSession = citiesSession;
	}
	public String getCityname() {
		return this.cityname;
	}
	public void setName(String latlon) {
		this.name = latlon;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public void setisLon(Double lon) {
		this.lon = lon;
	}
	private int cafesRestaurantsBars;
	private String weather;
	private Double lat;
	private Double lon;
	private int timesvisited;
	private int timeslived;
	private String CountryName;
	@Id
	private String name;
	public int getTimesvisited() {
		return timesvisited;
	}
	public void setTimesvisited(int timesvisited) {
		this.timesvisited +=timesvisited;
	}
	public String getCountryName() {
		return CountryName;
	}
	public void setCountryName(String countryName) {
		CountryName = countryName;
	}
	public int getTimeslived() {
		return timeslived;
	}
	public void setTimeslived(int timeslived) {
		this.timeslived +=timeslived;
	}
	public int getMuseums() {
		return museums;
	}

	public void setMuseums(int museums) {
		this.museums = museums;
	}

	public int getCafesRestaurantsBars() {
		return cafesRestaurantsBars;
	}

	public void setCafesRestaurantsBars(int cafesRestaurantsBars) {
		this.cafesRestaurantsBars = cafesRestaurantsBars;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
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
	public Traveller FreeTicket(List<Traveller> travellers) {
		System.out.println("size"+travellers.size());
        Traveller travellerFound = null;
        Business businessFound=null;
        float maxSimilarity=0.0f;
        float maxSimilarityb=0.0f;
        float prevsimilarity=0.0f;
        ArrayList<Traveller> similaritypoints=new ArrayList<Traveller>();
       for(Traveller traveller:travellers) {
           if(!( traveller instanceof Business && traveller.getClass()==Business.class)){
           if(traveller.Similarity(this,traveller)>=maxSimilarity) {
               maxSimilarity=traveller.Similarity(this,traveller);
               travellerFound=traveller;
               if(traveller.Similarity(this,traveller)==maxSimilarity) {
               if(maxSimilarity==prevsimilarity) {
                    similaritypoints.add(traveller);
               }
               else {
                   prevsimilarity=traveller.Similarity(this,traveller);
                   similaritypoints.removeAll(similaritypoints);
                   similaritypoints.add(traveller);
               }
               }
       }
           }
           else {
               if(traveller.Similarity(this,traveller)>=maxSimilarityb) {
            	   maxSimilarityb=traveller.Similarity(this,traveller);
                   businessFound=(Business) traveller;
               }
           }
       }
           if(businessFound!=null) {
           similaritypoints.add(businessFound);
           }
           float maxdist=0.0f;
           if(similaritypoints.size()>0) {
           for(Traveller Travellers:similaritypoints) {
               if(maxdist<DistanceCalculator.distance(this.getLat(),this.getLon(),Travellers.getCurrentlat(),Travellers.getCurrentlon(),"K") ) {
                   maxdist=(float) DistanceCalculator.distance(this.getLat(),this.getLon(),Travellers.getCurrentlat(),Travellers.getCurrentlon(),"K");
                   travellerFound=Travellers;
               }
           }
   }
           return travellerFound;
      
  
   }
	@Override
	public boolean equals(Object o) {
		 if (o == this) { 
	            return true; 
	        } 
		 if (!(o instanceof City)) { 
			 
	            return false; 
	        } 
		 else {
			 City newcity =(City) o;
			 if(this.getLat()==newcity.getLat() && this.getLon()==newcity.getLon()) {
				 return true;
			 }	
			 else {
				 return false;
			 }
		 }
	}
	@Override
	public String toString() {
		return "City [ museums=" + museums + ", name=" + name + ", cafesRestaurantsBars="
				+ cafesRestaurantsBars + ", weather=" + weather + ", lat=" + lat + ", lon=" + lon + "]";
	}
	
}

	
	
	
	

