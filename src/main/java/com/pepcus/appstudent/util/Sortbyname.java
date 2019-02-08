package com.pepcus.appstudent.util;

import java.util.Comparator;

public class Sortbyname implements Comparator<String> 
{ 
    
    public int compare(String a, String b) 
    {
    	try{
    	if(a.trim() != null && a.trim().length() > 2 && b.trim() != null && b.trim().length() > 2){
    		String compareOne = (a.toLowerCase().split(",")[1].replaceAll("\\s","")+a.trim().toLowerCase().split(",")[2].replaceAll("\\s","")).trim();
    		String compareTwo = (b.toLowerCase().split(",")[1].replaceAll("\\s", "")+b.trim().toLowerCase().split(",")[2].replaceAll("\\s","")).trim();
    	return compareOne.compareTo(compareTwo);
    	}
    	else{
    		return -1;
    		}
    	
    	}catch (Exception e) {

    		e.printStackTrace();
    	}
		return 0;
    } 
}