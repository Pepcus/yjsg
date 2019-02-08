package com.pepcus.appstudent.util;


/**
 * Class to keep all the constants used by application
 * 
 * @author Surabhi Bhawsar
 * @since 2017-11-09
 *
 */
public class ApplicationConstants {
    
    private ApplicationConstants () {
        
    }

  public static final String[] ATTENDANCE_REQUIRED_HEADERS = { "id", "day1", "day2", "day3", "day4",
            "day5", "day6", "day7","day8"};

  public static final String[] OPTIN_REQUIRED_HEADERS = { "id","optIn2019"};
  
  public static final String APPLICATION_FORCEDOWNLOAD="application/force-download";
  public static final String CONTENT_DISPOSITION="Content-Disposition";
  public static final String ATTACHMENT_FILENAME="attachment; filename=\"";
  public static final String DOUBL_QUOTE="\"";
  
}