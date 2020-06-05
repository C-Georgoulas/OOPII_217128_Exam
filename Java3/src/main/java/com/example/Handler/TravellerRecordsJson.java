package com.example.Handler;
import com.example.java2.Entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
public class TravellerRecordsJson {
 
	
public static void SaveTravellers(List<Traveller> travellers){
    ObjectMapper objectMapper = new ObjectMapper();
	 try {
	        objectMapper.writeValue(new File(""), travellers);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
}

}
