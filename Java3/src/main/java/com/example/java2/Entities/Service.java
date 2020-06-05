package com.example.java2.Entities;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import com.example.java2.RetrieveData.*;
import com.example.java2.Entities.Enums.CafeBarRestaur;
import com.example.java2.Entities.Enums.Museums;
public class Service {

	public static void main(String[] args) throws IOException , java.lang.ClassCastException {
		boolean flag=false;
		Scanner sc=new Scanner(System.in);
		int check=0;
		System.out.println("Which type are you?\n 1.Traveller\n2.Business\n3.Tourist\nPlease,select from 1-3 accordingly.");
		while(flag==false){
			try {
			check=sc.nextInt();}
			catch(NumberFormatException e) {
				System.out.println("We are sorry.Select an option from 1-3!");
			}
			if(check>=1&&check<=3) {
				flag=true;
			}
		}
		Scanner sc1=new Scanner(System.in);
		System.out.println("In which city do you live?");
		String currentcity;
		do {
		currentcity=sc1.nextLine();
		}while(currentcity==null && currentcity.equals(""));
	    String checkweather=null;
		int checkmuseumscafe;
		String weather;
		Museums museum=null;
		CafeBarRestaur cafebarrest=null;
		Scanner sc2=new Scanner(System.in);
	    System.out.println("Which weather do you prefer?\n1.Clear\n2.Clouds\n3.Rain\n4.Snow\n5.Any");
	    do {
	    	checkweather=sc2.nextLine();
	    }while(checkweather==null &&checkweather.trim().contentEquals(""));
	    
	    Scanner sc3=new Scanner(System.in);
		System.out.println("Do you prefer museums?\n1.Not top priorirty\n2.Interested\n3.Very Interested ");
		do {
		   checkmuseumscafe=sc3.nextInt();
		 }while(checkmuseumscafe<0 &&checkmuseumscafe>3);
		if(checkmuseumscafe==1) {
			museum=museum.notinterested;
		}
		 if(checkmuseumscafe==2) {
			 museum=museum.interested;
		 }
		 if(checkmuseumscafe==3) {
			 museum=museum.veryinterested;
		 }
		   Scanner sc4=new Scanner(System.in);
		System.out.println("Do you prefer Cafes,Bars and Restaurants?\n1.Not top priorirty\n2.Interested\n3.Very Interested ");
		      do {
			  checkmuseumscafe=sc4.nextInt();
			 }while(checkmuseumscafe<0 &&checkmuseumscafe>3);
		      if(checkmuseumscafe==1) {
					cafebarrest=cafebarrest.notinterested;
				}
				 if(checkmuseumscafe==2) {
					 cafebarrest=cafebarrest.interested;
				 }
				 if(checkmuseumscafe==3) {
					 cafebarrest=cafebarrest.veryinterested;
				 }
		ArrayList<String> preferedCities=new ArrayList<String>();
		preferedCities.add("Thessaloniki");
		preferedCities.add("Barcelona");
		preferedCities.add("London");
		preferedCities.add("Copenhagen");
		Traveller traveller2= new Traveller("John", 20,currentcity,checkweather,museum, cafebarrest,preferedCities,"ppp");
		Business traveller1=new Business("Kostas",19,currentcity,checkweather,museum, cafebarrest,preferedCities,"pppp");
		Tourist tourist=new Tourist("Kostas",19,currentcity,checkweather,museum, cafebarrest,preferedCities,"ppp");
	    //Traveller traveller2= new Traveller("John", 20,currentcity,checkweather,museum, cafebarrest,"6bac9239221efa2226041249811f67e6");
		System.out.println(traveller2.toString());
		System.out.println(traveller1.toString());
		ArrayList<City> cities= new ArrayList<City>();
		for (int i=0;i<preferedCities.size(); i++) {
			String city=preferedCities.get(i);
			cities.add(OpenData.RetrieveData(city,0,0));
		}
        traveller2.CompareCities(cities);
	}
}
