package com.pepcus.appstudent.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class to keep common utility methods
 * 
 * @author Shubham Solanki
 * @since 12-02-2018
 *
 */
public class CommonUtil {
	
	/**
	 * Method to convert Date to String 
	 * 
	 * @param date
	 * @return
	 */
	public static String convertDateToString(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(date);
	}

}
