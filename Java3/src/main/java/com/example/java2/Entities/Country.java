package com.example.java2.Entities;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document
public class Country {
@Id
private String CountryName;
private ArrayList<City> Cities;
private int timesvisited;
private int timeslived;
public Country(String CountryName, ArrayList<City> Cities) {
	this.CountryName = CountryName;
	this.Cities=new ArrayList<City>();
	this.Cities=Cities;
	this.timesvisited =0;
	this.timeslived =0;
}
public String getCountryName() {
	return CountryName;
}
public void setCountryName(String countryName) {
	CountryName = countryName;
}
public ArrayList<City> getCities() {
	return Cities;
}
public void setCities(City cities) {
		if(this.Cities.contains(cities)) {
		}
		else {
			Cities.add(cities);
		}
	
}
public int getTimesvisited() {
	return timesvisited;
}
public void setTimesvisited(int timesvisited) {
	this.timesvisited += timesvisited;
}
public int getTimeslived() {
	return timeslived;
}
public void setTimeslived(int timeslived) {
	this.timeslived += timeslived;
}
@Override
public String toString() {
	return "Country [CountryName=" + CountryName + ", Cities=" + Cities + ", timesvisited=" + timesvisited
			+ ", timeslived=" + timeslived + "]";
}
}
