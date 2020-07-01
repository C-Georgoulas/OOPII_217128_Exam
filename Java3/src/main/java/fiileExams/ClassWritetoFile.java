package fiileExams;

import java.util.List;
import java.util.ListIterator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import com.example.java2.Entities.City;

import com.example.java2.Repositories.CityRepository;

public class ClassWritetoFile {
	private static final String filepath="/home/ioannispanagiotopoulos/Desktop/file";
	   private static CityRepository cityrepository;
	   private static PrintWriter pw;
	   private static PrintWriter pw2;
	   
    public static String InsertFromDb(List<City> citiestofile,List<String> filenames) {
 
     
        //for(int size=0;size<citiesfromdb.size()/2;size++) {
    	if(!citiestofile.isEmpty() &&filenames.size()==2) {
        ListIterator<City> cityiterator=citiestofile.listIterator();
        int split=citiestofile.size()/2;

        int size=0;
        try {
        	File f = new File(filenames.get(0));
        	if(f.exists() && !f.isDirectory()) { 
        	    // do something
			pw = new PrintWriter(new FileOutputStream(filenames.get(0)));
        	}
        	else {
        		pw = new PrintWriter(new FileOutputStream(filepath+"1.txt" ));
        	}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
        	File f = new File(filenames.get(1));
        	if(f.exists() && !f.isDirectory()) { 
        	    // do something
			pw2 = new PrintWriter(new FileOutputStream(filenames.get(1)));
        	}
        	else {
        		
			pw2 = new PrintWriter(new FileOutputStream(filepath+"2.txt"));
        	}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        	while(cityiterator.hasNext()) {
        		if(size<split) {
        			if(size==0) {
        				pw.write(cityiterator.next().toString());
        			}
        			else {
        		WriteObjectToFile(cityiterator.next(),1);
        			}
        		size++;
        		}
        		else {
        			if(size==split) {
        				pw2.write(cityiterator.next().toString());
        			}
        			else {
        		WriteObjectToFile(cityiterator.next(),2);
        			}
        		size++;
        		}
        	}
        	pw.close();
        	pw2.close();
        	return "Successfuly submitted files";
    }
    	else {
    		return "Empty list";
    	}
        	
        	
    }
 
    public static void WriteObjectToFile(Object serObj,int option) {
    	if(option==1) {
 
        try {
        	String tmp = serObj.toString();
        	pw.println("");
            pw.append(tmp);
          /*  FileOutputStream fileOut = new FileOutputStream(filepath+filearg.toString()+".txt");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            System.out.println(serObj);
            objectOut.writeObject(serObj);
            objectOut.close();
            System.out.println("The Object  was succesfully written to a file");*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	}
    	else {
    		try {
            	String tmp = serObj.toString();
            	pw2.println("");
                pw2.append(tmp);
              /*  FileOutputStream fileOut = new FileOutputStream(filepath+filearg.toString()+".txt");
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                System.out.println(serObj);
                objectOut.writeObject(serObj);
                objectOut.close();
                System.out.println("The Object  was succesfully written to a file");*/
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    	}
    }

}
