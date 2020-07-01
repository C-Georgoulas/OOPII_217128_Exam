package com.example.java2.Entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class BasicSetOperations {
	private String filepath1="/home/ioannispanagiotopoulos/Desktop/file1.txt";
	private String filepath2="/home/ioannispanagiotopoulos/Desktop/file2.txt";
public List<List<City>> Handle() throws IOException {
	List<City> citiesfrom1=new ArrayList<City>();
	List<City> citiesfrom2=new ArrayList<City>();
	List<String> list1 = new ArrayList<String>();
	BufferedReader reader = new BufferedReader(new FileReader(filepath1));
	String line;
	while ((line = reader.readLine()) != null) {
	    list1.add(line);
	}
	reader.close();
	List<String> list2 = new ArrayList<String>();
	BufferedReader reader2 = new BufferedReader(new FileReader(filepath2));
	String line2;
	while ((line2 = reader2.readLine()) != null) {
	    list2.add(line2);
	}
	reader2.close();
	List<String> cut = new ArrayList<String>();
	for(String string:list1) {
	cut.add(string.substring(7,string.lastIndexOf(']')));
	String delimited[] = new String[5];
	delimited=cut.get(cut.size()-1).split(", ");
	int i=0;
	Integer museums = null;
	String cityname = null;
	Integer cafesRestaurantsBars = null;
	String weather = null;
    Double lattitude = null;
	Double Longtitude;
	for (String del:delimited) {
		String delimited2[]=new String[2];
		delimited2=del.split("=");
		
		   if(i==0) {
		    
		    	museums=Integer.parseInt(delimited2[1]);
		    }
		    if(i==1) {
		    	
		    	cityname=delimited2[1];
		    }
		    if(i==2) {
		    	
		    	cafesRestaurantsBars=Integer.parseInt(delimited2[1]);
		    }
		    if(i==3) {
		    
		    	weather=delimited2[1];
		    }
		    if(i==4) {
		    	lattitude=Double.parseDouble(delimited2[1]);
		    }
		    if(i==5) {
		    	Longtitude=Double.parseDouble(delimited2[1]);
		    	City CityOperation =new City(museums,cityname,cafesRestaurantsBars,weather,lattitude,Longtitude,0,0,"Name");
		    	citiesfrom1.add(CityOperation);
		    }
	    i++;
	}
	System.out.println();
	
	}
	List<String> cut2 = new ArrayList<String>();
	Integer museums = null;
	String cityname = null;
	Integer cafesRestaurantsBars = null;
	String weather = null;
    Double lattitude = null;
	Double Longtitude;
	for(String string:list2) {
		cut2.add(string.substring(7,string.lastIndexOf(']')));
		String delimited[] = new String[5];
		delimited=string.split(", ");
		
		delimited=cut.get(cut.size()-1).split(", ");
		//attributes to constructor
		int i=0;
		for (String del:delimited) {
			String delimited2[]=new String[2];
			delimited2=del.split("=");
			
		    if(i==0) {
		    	
		    	museums=Integer.parseInt(delimited2[1]);
		    }
		    if(i==1) {
		    	
		    	cityname=delimited2[1];
		    }
		    if(i==2) {
		    	cafesRestaurantsBars=Integer.parseInt(delimited2[1]);
		    }
		    if(i==3) {
		    	weather=delimited2[1];
		    }
		    if(i==4) {
		    	lattitude=Double.parseDouble(delimited2[1]);
		    }
		    if(i==5) {
		    	Longtitude=Double.parseDouble(delimited2[1]);
		    	City CityOperation =new City(museums,cityname,cafesRestaurantsBars,weather,lattitude,Longtitude,0,0,"name");
		    	citiesfrom2.add(CityOperation);
		    }
		
			i++;
		}
	}
	
List<List<City>> results = new ArrayList<>();
results.add(union(citiesfrom1,citiesfrom2));

results.add(intersection(citiesfrom1,citiesfrom2));
return results;
    
}


//public List<String> union(List<String> list1, List<String> list2) {
   
//}
public <T> List<T> union(List<T> list1, List<T> list2) {
    Set<T> set = new HashSet<T>();

    set.addAll(list1);
    set.addAll(list2);

    return new ArrayList<T>(set);
}

public <T> List<T> intersection(List<T> list1, List<T> list2) {
    List<T> list = new ArrayList<T>();

    for (T t : list1) {
        if(list2.contains(t)) {
            list.add(t);
        }
    }

    return list;
}
}

